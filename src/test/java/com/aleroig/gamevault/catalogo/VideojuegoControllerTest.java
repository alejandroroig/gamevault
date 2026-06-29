package com.aleroig.gamevault.catalogo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VideojuegoController.class)
class VideojuegoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VideojuegoService videojuegoService;

    @MockitoBean
    private CacheManager cacheManager;

    @MockitoBean
    private RedisConnectionFactory redisConnectionFactory;

    @Test
    void create_DebeDevolver400_CuandoElDtoNoEsValido() throws Exception {
        String body = """
                {
                  "titulo": "",
                  "precio": -5,
                  "fechaLanzamiento": null,
                  "estudioId": -1,
                  "detallesPlataforma": {}
                }
                """;

        mockMvc.perform(post("/api/videojuegos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Error de validación"))
                .andExpect(jsonPath("$.message").value("La petición contiene campos inválidos"))
                .andExpect(jsonPath("$.path").value("/api/videojuegos"))
                .andExpect(jsonPath("$.fields.titulo").exists())
                .andExpect(jsonPath("$.fields.precio").exists())
                .andExpect(jsonPath("$.fields.fechaLanzamiento").exists())
                .andExpect(jsonPath("$.fields.estudioId").exists());

        verifyNoInteractions(videojuegoService);
    }
}