package com.aleroig.gamevault.reviews;

import com.aleroig.gamevault.reviews.dto.ReviewResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VideojuegoReviewController.class)
class VideojuegoReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewService reviewService;

    @MockitoBean
    private CacheManager cacheManager;

    @MockitoBean
    private RedisConnectionFactory redisConnectionFactory;

    @Test
    void getByVideojuegoId_DebeDevolverReviews_CuandoElVideojuegoExiste() throws Exception {
        List<ReviewResponseDTO> reviews = List.of(
                new ReviewResponseDTO("abc123", 29L, "GamerPro", 10, "Obra maestra absoluta."),
                new ReviewResponseDTO("def456", 29L, "NoobMaster", 9, "Difícil pero justo.")
        );

        when(reviewService.findByVideojuegoId(29L)).thenReturn(reviews);

        mockMvc.perform(get("/api/videojuegos/29/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("abc123"))
                .andExpect(jsonPath("$[0].videojuegoId").value(29))
                .andExpect(jsonPath("$[0].autor").value("GamerPro"))
                .andExpect(jsonPath("$[0].puntuacion").value(10))
                .andExpect(jsonPath("$[0].comentario").value("Obra maestra absoluta."))
                .andExpect(jsonPath("$[1].autor").value("NoobMaster"));
    }
}