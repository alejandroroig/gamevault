package com.aleroig.gamevault.actividad.mensajeria;

import com.aleroig.gamevault.actividad.ActividadService;
import com.aleroig.gamevault.config.RabbitMQConfig;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActividadEventConsumer {

    private final ActividadService actividadService;
    private final JsonMapper jsonMapper;

    @RabbitListener(queues = RabbitMQConfig.ACTIVIDAD_QUEUE)
    public void recibir(String payload) {
        try {
            ActividadEvent event = jsonMapper.readValue(payload, ActividadEvent.class);
            actividadService.registrar(event);
        } catch (JacksonException e) {
            throw new IllegalArgumentException("No se pudo leer el evento de actividad", e);
        }
    }
}