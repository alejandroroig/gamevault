package com.aleroig.gamevault.reviews.mensajeria;

import com.aleroig.gamevault.reviews.ReviewService;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ReviewsVideojuegoEventConsumerTest {

    private final ReviewService reviewService = mock(ReviewService.class);
    private final JsonMapper jsonMapper = new JsonMapper();

    private final ReviewsVideojuegoEventConsumer consumer =
            new ReviewsVideojuegoEventConsumer(reviewService, jsonMapper);

    @Test
    void recibir_DebeBorrarReviews_CuandoRecibeVideojuegoEliminado() {
        String payload = """
                {
                  "tipo": "VIDEOJUEGO_ELIMINADO",
                  "videojuegoId": 1,
                  "titulo": "Hollow Knight"
                }
                """;

        consumer.recibir(payload);

        verify(reviewService).deleteByVideojuegoId(1L);
    }
}