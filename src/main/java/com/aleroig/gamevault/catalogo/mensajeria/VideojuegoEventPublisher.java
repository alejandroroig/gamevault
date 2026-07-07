package com.aleroig.gamevault.catalogo.mensajeria;

import com.aleroig.gamevault.catalogo.api.eventos.VideojuegoEvent;
import com.aleroig.gamevault.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@Service
@RequiredArgsConstructor
public class VideojuegoEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final JsonMapper jsonMapper;

    public void publicarVideojuegoCreado(Long videojuegoId, String titulo) {
        publicar(
                new VideojuegoEvent(VideojuegoEvent.VIDEOJUEGO_CREADO, videojuegoId, titulo),
                RabbitMQConfig.VIDEOJUEGO_CREADO_ROUTING_KEY
        );
    }

    public void publicarVideojuegoActualizado(Long videojuegoId, String titulo) {
        publicar(
                new VideojuegoEvent(VideojuegoEvent.VIDEOJUEGO_ACTUALIZADO, videojuegoId, titulo),
                RabbitMQConfig.VIDEOJUEGO_ACTUALIZADO_ROUTING_KEY
        );
    }

    public void publicarVideojuegoEliminado(Long videojuegoId, String titulo) {
        publicar(
                new VideojuegoEvent(VideojuegoEvent.VIDEOJUEGO_ELIMINADO, videojuegoId, titulo),
                RabbitMQConfig.VIDEOJUEGO_ELIMINADO_ROUTING_KEY
        );
    }

    private void publicar(VideojuegoEvent event, String routingKey) {
        try {
            String payload = jsonMapper.writeValueAsString(event);

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.VIDEOJUEGO_EXCHANGE,
                    routingKey,
                    payload
            );
        } catch (JacksonException e) {
            throw new IllegalStateException("No se pudo serializar el evento de videojuego", e);
        }
    }
}