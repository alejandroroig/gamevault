package com.aleroig.gamevault.catalogo;

import org.junit.jupiter.api.Test;
import com.aleroig.gamevault.catalogo.dto.VideojuegoFiltroDTO;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        mockMvc.perform(post("/api/v1/videojuegos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Error de validación"))
                .andExpect(jsonPath("$.message").value("La petición contiene campos inválidos"))
                .andExpect(jsonPath("$.path").value("/api/v1/videojuegos"))
                .andExpect(jsonPath("$.fields.titulo").exists())
                .andExpect(jsonPath("$.fields.precio").exists())
                .andExpect(jsonPath("$.fields.fechaLanzamiento").exists())
                .andExpect(jsonPath("$.fields.estudioId").exists());

        verifyNoInteractions(videojuegoService);
    }

    @Test
    void getAll_DebePasarFiltrosAlServicio() throws Exception {
        when(videojuegoService.findAllPaginated(
                any(VideojuegoFiltroDTO.class),
                any(Pageable.class)
        )).thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/api/v1/videojuegos")
                        .param("titulo", "hollow")
                        .param("estudioId", "1")
                        .param("precioMin", "10")
                        .param("precioMax", "30")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(videojuegoService).findAllPaginated(
                argThat(filtro ->
                        filtro.titulo().equals("hollow")
                                && filtro.estudioId().equals(1L)
                                && filtro.precioMin().compareTo(new BigDecimal("10")) == 0
                                && filtro.precioMax().compareTo(new BigDecimal("30")) == 0
                ),
                any(Pageable.class)
        );
    }

    @Test
    void delete_DebeDevolver204_CuandoSeEliminaVideojuego() throws Exception {
        mockMvc.perform(delete("/api/v1/videojuegos/1"))
                .andExpect(status().isNoContent());

        verify(videojuegoService).delete(1L);
    }
}