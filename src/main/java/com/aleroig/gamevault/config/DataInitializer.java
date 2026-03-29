package com.aleroig.gamevault.config;

import com.aleroig.gamevault.catalogo.EstudioRepository;
import com.aleroig.gamevault.catalogo.EstudioService;
import com.aleroig.gamevault.catalogo.VideojuegoRepository;
import com.aleroig.gamevault.catalogo.VideojuegoService;
import com.aleroig.gamevault.catalogo.dto.EstudioDTO;
import com.aleroig.gamevault.catalogo.dto.VideojuegoCreateDTO;
import com.aleroig.gamevault.reviews.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final EstudioRepository estudioRepository;
    private final VideojuegoRepository videojuegoRepository;
    private final ReviewRepository reviewRepository;

    private final EstudioService estudioService;
    private final VideojuegoService videojuegoService;

    private record SeedData(List<EstudioDTO> estudios, List<VideojuegoCreateDTO> videojuegos) {
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Limpiando bases de datos para garantizar un estado predecible...");
        // El orden de borrado es vital: Primero los "hijos" y luego los "padres"
        reviewRepository.deleteAll(); // Borra Mongo
        videojuegoRepository.deleteAll(); // Borra Postgres (Hijo)
        estudioRepository.deleteAll(); // Borra Postgres (Padre)

        log.info("Iniciando el sembrado de datos (Data Seeding)...");

        // Jackson 3: Creamos el JsonMapper con soporte de fechas
        JsonMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();

        try (InputStream inputStream = getClass().getResourceAsStream("/datos_iniciales.json")) {
            SeedData data = mapper.readValue(inputStream, new TypeReference<SeedData>() {
            });

            // 1. Guardamos los estudios y almacenamos el resultado para saber sus IDs reales
            List<EstudioDTO> estudiosGuardados = new java.util.ArrayList<>();
            if (data.estudios() != null) {
                for (EstudioDTO estudio : data.estudios()) {
                    estudiosGuardados.add(estudioService.create(estudio));
                }
            }

            // 2. Insertamos los juegos usando el ID real de la base de datos
            if (data.videojuegos() != null && !estudiosGuardados.isEmpty()) {
                for (VideojuegoCreateDTO juego : data.videojuegos()) {

                    // El JSON dice "estudioId: 1". Le restamos 1 para coger la posición 0 de la lista.
                    int indiceReal = (int) (juego.estudioId() - 1);
                    Long idRealDeLaBD = estudiosGuardados.get(indiceReal).id();

                    // Como los records son inmutables, creamos uno nuevo con el ID corregido
                    VideojuegoCreateDTO juegoCorregido = new VideojuegoCreateDTO(
                            juego.titulo(),
                            juego.precio(),
                            juego.fechaLanzamiento(),
                            idRealDeLaBD, // <-- ¡Aquí inyectamos la magia!
                            juego.detallesPlataforma()
                    );

                    videojuegoService.create(juegoCorregido);
                }
            }

            log.info("¡Datos inyectados correctamente!");
        } catch (Exception e) {
            log.error("Error al inyectar los datos iniciales: {}", e.getMessage(), e);
        }
    }
}