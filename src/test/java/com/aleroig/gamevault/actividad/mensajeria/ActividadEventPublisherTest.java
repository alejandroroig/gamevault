package com.aleroig.gamevault.actividad.mensajeria;

import com.aleroig.gamevault.config.RabbitMQConfig;
import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ActividadEventPublisherTest {

    private final RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
    private final JsonMapper jsonMapper = new JsonMapper();

    private final ActividadEventPublisher publisher =
            new ActividadEventPublisher(rabbitTemplate, jsonMapper);

    @Test
    void publicarVideojuegoCreado_DebeEnviarMensajeARabbitMQ() {
        publisher.publicarVideojuegoCreado(1L, "Hollow Knight");

        ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);

        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.ACTIVIDAD_EXCHANGE),
                eq(RabbitMQConfig.ACTIVIDAD_ROUTING_KEY),
                payloadCaptor.capture()
        );

        String payload = payloadCaptor.getValue();

        assertTrue(payload.contains("VIDEOJUEGO_CREADO"));
        assertTrue(payload.contains("Videojuego"));
        assertTrue(payload.contains("1"));
        assertTrue(payload.contains("Hollow Knight"));
    }
}