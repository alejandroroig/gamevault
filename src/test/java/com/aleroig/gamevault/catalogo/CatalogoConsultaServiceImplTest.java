package com.aleroig.gamevault.catalogo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogoConsultaServiceImplTest {

    @Mock
    private VideojuegoRepository videojuegoRepository;

    @InjectMocks
    private CatalogoConsultaServiceImpl catalogoConsultaService;

    @Test
    void existeVideojuego_DebeDevolverTrue_CuandoExiste() {
        when(videojuegoRepository.existsById(1L)).thenReturn(true);

        boolean existe = catalogoConsultaService.existeVideojuego(1L);

        assertTrue(existe);
    }

    @Test
    void existeVideojuego_DebeDevolverFalse_CuandoNoExiste() {
        when(videojuegoRepository.existsById(99L)).thenReturn(false);

        boolean existe = catalogoConsultaService.existeVideojuego(99L);

        assertFalse(existe);
    }
}