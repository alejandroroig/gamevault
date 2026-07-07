package com.aleroig.gamevault.catalogo;

import com.aleroig.gamevault.actividad.mensajeria.ActividadEventPublisher;
import com.aleroig.gamevault.reviews.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideojuegoServiceTest {

    @Mock
    private VideojuegoRepository videojuegoRepository;

    @Mock
    private EstudioRepository estudioRepository;

    @Mock
    private ReviewService reviewService;

    @Mock
    private ActividadEventPublisher actividadEventPublisher;

    @InjectMocks
    private VideojuegoService videojuegoService;

    @Test
    void delete_DebeBorrarReviewsYVideojuego_CuandoExiste() {
        when(videojuegoRepository.existsById(1L)).thenReturn(true);

        videojuegoService.delete(1L);

        verify(reviewService).deleteByVideojuegoId(1L);
        verify(videojuegoRepository).deleteById(1L);
        verify(actividadEventPublisher).publicarVideojuegoEliminado(1L);
    }

    @Test
    void delete_DebeLanzar404_CuandoVideojuegoNoExiste() {
        when(videojuegoRepository.existsById(99L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> videojuegoService.delete(99L)
        );

        assertEquals(404, exception.getStatusCode().value());
        verifyNoInteractions(reviewService);
        verify(videojuegoRepository, never()).deleteById(anyLong());
        verifyNoInteractions(actividadEventPublisher);
    }
}