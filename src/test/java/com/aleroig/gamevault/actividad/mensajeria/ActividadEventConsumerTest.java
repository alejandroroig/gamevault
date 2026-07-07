package com.aleroig.gamevault.actividad.mensajeria;

import com.aleroig.gamevault.actividad.ActividadService;
import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ActividadEventConsumerTest {

    private final ActividadService actividadService = mock(ActividadService.class);
    private final JsonMapper jsonMapper = new JsonMapper();

    private final ActividadEventConsumer consumer =
            new ActividadEventConsumer(actividadService, jsonMapper);

    @Test
    void recibir_DebeConvertirMensajeYRegistrarActividad() {
        String payload = """
                {
                  "tipo": "VIDEOJUEGO_ELIMINADO",
                  "entidad": "Videojuego",
                  "entidadId": "1",
                  "descripcion": "Se ha eliminado el videojuego con id 1"
                }
                """;

        consumer.recibir(payload);

        verify(actividadService).registrar(argThat(esEventoEliminacionVideojuego()));
    }

    private ArgumentMatcher<ActividadEvent> esEventoEliminacionVideojuego() {
        return event ->
                event.tipo().equals("VIDEOJUEGO_ELIMINADO")
                        && event.entidad().equals("Videojuego")
                        && event.entidadId().equals("1")
                        && event.descripcion().contains("Se ha eliminado el videojuego");
    }
}