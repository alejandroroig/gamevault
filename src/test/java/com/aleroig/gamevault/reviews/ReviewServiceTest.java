package com.aleroig.gamevault.reviews;

import com.aleroig.gamevault.catalogo.VideojuegoRepository;
import com.aleroig.gamevault.reviews.dto.ReviewCreateDTO;
import com.aleroig.gamevault.reviews.dto.ReviewResumenDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Habilita Mockito en JUnit 5
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository; // Base de datos simulada

    @Mock
    private VideojuegoRepository videojuegoRepository; // Base de datos simulada

    @InjectMocks
    private ReviewService reviewService; // El servicio REAL que vamos a probar

    @Test
    void create_DebeLanzarExcepcion_SiVideojuegoNoExiste() {
        // 1. Arrange (Preparar los datos)
        ReviewCreateDTO dto = new ReviewCreateDTO(99L, "Gamer", 10, "Genial");

        // Entrenamos al mock: "Si te preguntan por el ID 99, di que NO existe"
        when(videojuegoRepository.existsById(99L)).thenReturn(false);

        // 2. Act & Assert (Ejecutar y Comprobar a la vez)
        // Verificamos que al llamar a create() salte nuestra ResponseStatusException
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reviewService.create(dto);
        });

        // 3. Verificamos que el error devuelto sea exactamente un 404
        assertEquals(404, exception.getStatusCode().value());
    }

    @Test
    void getResumenByVideojuegoId_DebeCalcularTotalYMedia_CuandoExistenReviews() {
        Review review1 = new Review();
        review1.setVideojuegoId(1L);
        review1.setAutor("GamerPro");
        review1.setPuntuacion(10);
        review1.setComentario("Obra maestra absoluta.");

        Review review2 = new Review();
        review2.setVideojuegoId(1L);
        review2.setAutor("NoobMaster");
        review2.setPuntuacion(9);
        review2.setComentario("Difícil pero justo.");

        when(videojuegoRepository.existsById(1L)).thenReturn(true);
        when(reviewRepository.findByVideojuegoId(1L)).thenReturn(List.of(review1, review2));

        ReviewResumenDTO resumen = reviewService.getResumenByVideojuegoId(1L);

        assertEquals(1L, resumen.videojuegoId());
        assertEquals(2, resumen.totalReviews());
        assertEquals(9.5, resumen.puntuacionMedia(), 0.001);
    }

    @Test
    void getResumenByVideojuegoId_DebeDevolverMediaCero_CuandoNoHayReviews() {
        when(videojuegoRepository.existsById(1L)).thenReturn(true);
        when(reviewRepository.findByVideojuegoId(1L)).thenReturn(List .of());

        ReviewResumenDTO resumen = reviewService.getResumenByVideojuegoId(1L);

        assertEquals(1L, resumen.videojuegoId());
        assertEquals(0, resumen.totalReviews());
        assertEquals(0.0, resumen.puntuacionMedia(), 0.001);
    }

    @Test
    void deleteByVideojuegoId_DebeEliminarReviewsAsociadas() {
        when(reviewRepository.deleteByVideojuegoId(1L)).thenReturn(2L);

        long eliminadas = reviewService.deleteByVideojuegoId(1L);

        assertEquals(2L, eliminadas);
        verify(reviewRepository).deleteByVideojuegoId(1L);
    }
}
