package com.aleroig.gamevault.actividad.mensajeria;

import com.aleroig.gamevault.actividad.ActividadService;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ActividadVideojuegoEventConsumerTest {

    private final ActividadService actividadService = mock(ActividadService.class);
    private final JsonMapper jsonMapper = new JsonMapper();

    private final ActividadVideojuegoEventConsumer consumer =
            new ActividadVideojuegoEventConsumer(actividadService, jsonMapper);

    @Test
    void recibir_DebeRegistrarActividad_CuandoRecibeEventoDeVideojuego() {
        String payload = """
                {
                  "tipo": "VIDEOJUEGO_ELIMINADO",
                  "videojuegoId": 1,
                  "titulo": "Hollow Knight"
                }
                """;

        consumer.recibir(payload);

        verify(actividadService).registrar(
                "VIDEOJUEGO_ELIMINADO",
                "Videojuego",
                "1",
                "Se ha eliminado el videojuego Hollow Knight con id 1"
        );
    }
}