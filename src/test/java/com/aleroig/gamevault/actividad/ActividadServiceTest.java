package com.aleroig.gamevault.actividad;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ActividadServiceTest {

    @Mock
    private ActividadRepository actividadRepository;

    @InjectMocks
    private ActividadService actividadService;

    @Test
    void registrar_DebeGuardarActividad() {
        actividadService.registrar(
                "VIDEOJUEGO_ELIMINADO",
                "Videojuego",
                "1",
                "Se ha eliminado el videojuego Hollow Knight con id 1"
        );

        verify(actividadRepository).save(argThat(esActividadEliminacionVideojuego()));
    }

    private ArgumentMatcher<Actividad> esActividadEliminacionVideojuego() {
        return actividad ->
                actividad.getTipo().equals("VIDEOJUEGO_ELIMINADO")
                        && actividad.getEntidad().equals("Videojuego")
                        && actividad.getEntidadId().equals("1")
                        && actividad.getDescripcion().contains("Se ha eliminado el videojuego")
                        && actividad.getFecha() != null;
    }
}