package com.aleroig.gamevault.catalogo;

import com.aleroig.gamevault.catalogo.dto.EstudioDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(EstudioController.class) // Solo levanta el entorno web para Estudios
@AutoConfigureMockMvc(addFilters = false)
public class EstudioControllerTest {
    @Autowired
    private MockMvc mockMvc; // Simula a Postman haciendo peticiones HTTP

    @MockitoBean
    private EstudioService estudioService; // Cortocircuitamos el servicio real

    @MockitoBean
    private CacheManager cacheManager;

    @MockitoBean
    private RedisConnectionFactory redisConnectionFactory;

    @Test
    void getAll_DebeDevolverListaDeEstudios_YStatus200() throws Exception {
        // 1. Arrange (Preparación)
        EstudioDTO nintendo = new EstudioDTO(1L, "Nintendo", "Japón");
        // Cuando el controlador llame al servicio, devolveremos esta lista estática
        when(estudioService.findAll()).thenReturn(List.of(nintendo));

        // 2 & 3. Act & Assert (Llamada HTTP y validaciones)
        // Fíjate cómo validamos NUESTROS datos (el DTO), no los del framework
        mockMvc.perform(get("/api/v1/estudios"))
                .andExpect(status().isOk()) // Esperamos HTTP 200
                .andExpect(content().contentType("application/json")) // Formato JSON
                .andExpect(jsonPath("$[0].nombre").value("Nintendo")) // Validamos nuestro contrato
                .andExpect(jsonPath("$[0].pais").value("Japón"));
    }
}
