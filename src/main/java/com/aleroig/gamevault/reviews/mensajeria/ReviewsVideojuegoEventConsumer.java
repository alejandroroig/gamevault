package com.aleroig.gamevault.reviews.mensajeria;

import com.aleroig.gamevault.catalogo.api.eventos.VideojuegoEvent;
import com.aleroig.gamevault.config.RabbitMQConfig;
import com.aleroig.gamevault.reviews.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@Service
@RequiredArgsConstructor
public class ReviewsVideojuegoEventConsumer {

    private final ReviewService reviewService;
    private final JsonMapper jsonMapper;

    @RabbitListener(queues = RabbitMQConfig.REVIEWS_VIDEOJUEGO_QUEUE)
    public void recibir(String payload) {
        try {
            VideojuegoEvent event = jsonMapper.readValue(payload, VideojuegoEvent.class);

            if (VideojuegoEvent.VIDEOJUEGO_ELIMINADO.equals(event.tipo())) {
                reviewService.deleteByVideojuegoId(event.videojuegoId());
            }
        } catch (JacksonException e) {
            throw new IllegalArgumentException("No se pudo leer el evento de videojuego", e);
        }
    }
}