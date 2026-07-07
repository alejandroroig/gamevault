package com.aleroig.gamevault.catalogo.mensajeria;

import com.aleroig.gamevault.config.RabbitMQConfig;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import tools.jackson.databind.json.JsonMapper;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class VideojuegoEventPublisherTest {

    private final RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
    private final JsonMapper jsonMapper = new JsonMapper();

    private final VideojuegoEventPublisher publisher =
            new VideojuegoEventPublisher(rabbitTemplate, jsonMapper);

    @Test
    void publicarVideojuegoCreado_DebeEnviarEventoDeDominio() {
        publisher.publicarVideojuegoCreado(1L, "Hollow Knight");

        ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);

        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.VIDEOJUEGO_EXCHANGE),
                eq(RabbitMQConfig.VIDEOJUEGO_CREADO_ROUTING_KEY),
                payloadCaptor.capture()
        );

        String payload = payloadCaptor.getValue();

        assertTrue(payload.contains("VIDEOJUEGO_CREADO"));
        assertTrue(payload.contains("1"));
        assertTrue(payload.contains("Hollow Knight"));
    }
}