package com.aleroig.gamevault.catalogo;

import com.aleroig.gamevault.catalogo.mensajeria.VideojuegoEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

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
    private VideojuegoEventPublisher videojuegoEventPublisher;

    @InjectMocks
    private VideojuegoService videojuegoService;

    @Test
    void delete_DebeBorrarVideojuegoYPublicarEvento_CuandoExiste() {
        Videojuego videojuego = new Videojuego();
        videojuego.setId(1L);
        videojuego.setTitulo("Hollow Knight");

        when(videojuegoRepository.findById(1L)).thenReturn(Optional.of(videojuego));

        videojuegoService.delete(1L);

        verify(videojuegoRepository).delete(videojuego);
        verify(videojuegoEventPublisher).publicarVideojuegoEliminado(1L, "Hollow Knight");
    }

    @Test
    void delete_DebeLanzar404_CuandoVideojuegoNoExiste() {
        when(videojuegoRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> videojuegoService.delete(99L)
        );

        assertEquals(404, exception.getStatusCode().value());
        verify(videojuegoRepository, never()).delete(any(Videojuego.class));
        verifyNoInteractions(videojuegoEventPublisher);
    }
}