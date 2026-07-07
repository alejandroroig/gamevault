package com.aleroig.gamevault.actividad.mensajeria;

import com.aleroig.gamevault.actividad.ActividadService;
import com.aleroig.gamevault.catalogo.api.eventos.VideojuegoEvent;
import com.aleroig.gamevault.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@Service
@RequiredArgsConstructor
public class ActividadVideojuegoEventConsumer {

    private final ActividadService actividadService;
    private final JsonMapper jsonMapper;

    @RabbitListener(queues = RabbitMQConfig.ACTIVIDAD_VIDEOJUEGO_QUEUE)
    public void recibir(String payload) {
        try {
            VideojuegoEvent event = jsonMapper.readValue(payload, VideojuegoEvent.class);
            actividadService.registrar(
                    event.tipo(),
                    "Videojuego",
                    event.videojuegoId().toString(),
                    crearDescripcion(event)
            );
        } catch (JacksonException e) {
            throw new IllegalArgumentException("No se pudo leer el evento de videojuego", e);
        }
    }

    private String crearDescripcion(VideojuegoEvent event) {
        return switch (event.tipo()) {
            case VideojuegoEvent.VIDEOJUEGO_CREADO ->
                    "Se ha creado el videojuego " + event.titulo() + " con id " + event.videojuegoId();
            case VideojuegoEvent.VIDEOJUEGO_ACTUALIZADO ->
                    "Se ha actualizado el videojuego " + event.titulo() + " con id " + event.videojuegoId();
            case VideojuegoEvent.VIDEOJUEGO_ELIMINADO ->
                    "Se ha eliminado el videojuego " + event.titulo() + " con id " + event.videojuegoId();
            default ->
                    "Evento de videojuego no reconocido para id " + event.videojuegoId();
        };
    }
}