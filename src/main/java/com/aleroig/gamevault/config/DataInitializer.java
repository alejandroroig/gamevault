package com.aleroig.gamevault.config;

import com.aleroig.gamevault.catalogo.EstudioRepository;
import com.aleroig.gamevault.catalogo.EstudioService;
import com.aleroig.gamevault.catalogo.VideojuegoService;
import com.aleroig.gamevault.catalogo.dto.EstudioDTO;
import com.aleroig.gamevault.catalogo.dto.VideojuegoCreateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final EstudioRepository estudioRepository;
    private final EstudioService estudioService;
    private final VideojuegoService videojuegoService;

    // Inyectamos el mapper de Spring
    private final ObjectMapper mapper;

    // Clase auxiliar interna para mapear el JSON entero de golpe
    private record SeedData(List<EstudioDTO> estudios, List<VideojuegoCreateDTO> videojuegos) {}

    @Override
    public void run(String... args) throws Exception {
        // Solo inyectamos datos si la base de datos está vacía
        if (estudioRepository.count() == 0) {
            log.info("Iniciando el sembrado de datos (Data Seeding)...");

            try (InputStream inputStream = getClass().getResourceAsStream("/datos_iniciales.json")) {
                SeedData data = mapper.readValue(inputStream, new TypeReference<SeedData>() {});

                // Guardamos usando los servicios para aprovechar nuestra lógica de negocio
                data.estudios().forEach(estudioService::create);
                data.videojuegos().forEach(videojuegoService::create);

                log.info("¡Datos inyectados correctamente!");
            } catch (Exception e) {
                log.error("Error al leer el archivo de datos iniciales: {}", e.getMessage());
            }
        } else {
            log.info("La base de datos ya contiene información. Se omite el Data Seeding.");
        }
    }
}
