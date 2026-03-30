package com.aleroig.gamevault.reviews;

import com.aleroig.gamevault.catalogo.VideojuegoRepository;
import com.aleroig.gamevault.reviews.dto.ReviewCreateDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
}
