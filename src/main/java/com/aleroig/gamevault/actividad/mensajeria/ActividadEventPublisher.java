package com.aleroig.gamevault.actividad.mensajeria;

import com.aleroig.gamevault.config.RabbitMQConfig;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActividadEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final JsonMapper jsonMapper;

    public void publicarVideojuegoCreado(Long videojuegoId, String titulo) {
        publicar(new ActividadEvent(
                "VIDEOJUEGO_CREADO",
                "Videojuego",
                videojuegoId.toString(),
                "Se ha creado el videojuego " + titulo + " con id " + videojuegoId
        ));
    }

    public void publicarVideojuegoActualizado(Long videojuegoId, String titulo) {
        publicar(new ActividadEvent(
                "VIDEOJUEGO_ACTUALIZADO",
                "Videojuego",
                videojuegoId.toString(),
                "Se ha actualizado el videojuego " + titulo + " con id " + videojuegoId
        ));
    }

    public void publicarVideojuegoEliminado(Long videojuegoId) {
        publicar(new ActividadEvent(
                "VIDEOJUEGO_ELIMINADO",
                "Videojuego",
                videojuegoId.toString(),
                "Se ha eliminado el videojuego con id " + videojuegoId
        ));
    }

    private void publicar(ActividadEvent event) {
        try {
            String payload = jsonMapper.writeValueAsString(event);

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ACTIVIDAD_EXCHANGE,
                    RabbitMQConfig.ACTIVIDAD_ROUTING_KEY,
                    payload
            );
        } catch (JacksonException | AmqpException e) {
            log.warn("No se pudo publicar el evento de actividad: {}", event, e);
        }
    }
}