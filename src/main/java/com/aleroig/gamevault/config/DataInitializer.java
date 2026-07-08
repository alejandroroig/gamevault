package com.aleroig.gamevault.config;

import com.aleroig.gamevault.catalogo.EstudioRepository;
import com.aleroig.gamevault.catalogo.EstudioService;
import com.aleroig.gamevault.catalogo.VideojuegoRepository;
import com.aleroig.gamevault.catalogo.VideojuegoService;
import com.aleroig.gamevault.catalogo.dto.EstudioDTO;
import com.aleroig.gamevault.catalogo.dto.VideojuegoCreateDTO;
import com.aleroig.gamevault.catalogo.dto.VideojuegoResponseDTO;
import com.aleroig.gamevault.reviews.ReviewRepository;
import com.aleroig.gamevault.reviews.ReviewService;
import com.aleroig.gamevault.reviews.dto.ReviewCreateDTO;
import com.aleroig.gamevault.seguridad.RolUsuario;
import com.aleroig.gamevault.seguridad.Usuario;
import com.aleroig.gamevault.seguridad.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "gamevault.seed.enabled", havingValue = "true")
public class DataInitializer implements CommandLineRunner {
    private final ReviewRepository reviewRepository;

    private final EstudioService estudioService;
    private final VideojuegoService videojuegoService;
    private final ReviewService reviewService;

    private final JdbcTemplate jdbcTemplate;

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private record SeedData(List<EstudioDTO> estudios, List<VideojuegoCreateDTO> videojuegos, List<ReviewCreateDTO> reviews) {}


    @Override
    public void run(String... args) throws Exception {
        sembrarUsuarios();
        log.info("Limpiando bases de datos...");
        reviewRepository.deleteAll();
        jdbcTemplate.execute("TRUNCATE TABLE videojuego, estudio RESTART IDENTITY CASCADE");
        // videojuegoRepository.deleteAll();
        // estudioRepository.deleteAll();

        log.info("Iniciando el sembrado de datos (Postgres + Mongo)...");
        JsonMapper mapper = JsonMapper.builder().findAndAddModules().build();

        try (InputStream inputStream = getClass().getResourceAsStream("/datos_iniciales.json")) {
            SeedData data = mapper.readValue(inputStream, new TypeReference<SeedData>() {});

            List<EstudioDTO> estudiosGuardados = new java.util.ArrayList<>();
            if (data.estudios() != null) {
                for (EstudioDTO estudio : data.estudios()) {
                    estudiosGuardados.add(estudioService.create(estudio));
                }
            }

            List<VideojuegoResponseDTO> juegosGuardados = new java.util.ArrayList<>();
            if (data.videojuegos() != null && !estudiosGuardados.isEmpty()) {
                for (VideojuegoCreateDTO juego : data.videojuegos()) {
                    Long idEstudioReal = estudiosGuardados.get((int) (juego.estudioId() - 1)).id();
                    VideojuegoCreateDTO juegoCorregido = new VideojuegoCreateDTO(
                            juego.titulo(), juego.precio(), juego.fechaLanzamiento(),
                            idEstudioReal, juego.detallesPlataforma()
                    );
                    juegosGuardados.add(videojuegoService.create(juegoCorregido));
                }
            }

            // Puente políglota: Usamos el ID real de Postgres para guardar en Mongo
            if (data.reviews() != null && !juegosGuardados.isEmpty()) {
                for (ReviewCreateDTO review : data.reviews()) {
                    Long idJuegoReal = juegosGuardados.get((int) (review.videojuegoId() - 1)).id();
                    ReviewCreateDTO reviewCorregida = new ReviewCreateDTO(
                            idJuegoReal, review.autor(), review.puntuacion(), review.comentario()
                    );
                    reviewService.create(reviewCorregida);
                }
            }
            log.info("¡Datos inyectados correctamente!");
        } catch (Exception e) {
            log.error("Error al inyectar datos: {}", e.getMessage(), e);
        }
    }

    private void sembrarUsuarios() {
        crearOActualizarUsuario("admin", "admin123", RolUsuario.ADMIN);
        crearOActualizarUsuario("user", "user123", RolUsuario.USER);
    }

    private void crearOActualizarUsuario(String username, String rawPassword, RolUsuario rol) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseGet(Usuario::new);

        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(rawPassword));
        usuario.setRol(rol);
        usuario.setActivo(true);

        usuarioRepository.save(usuario);
    }
}