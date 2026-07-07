package com.aleroig.gamevault.actividad;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActividadService {

    private final ActividadRepository actividadRepository;

    @Async
    public void registrarVideojuegoCreado(Long videojuegoId, String titulo) {
        registrar(
                "VIDEOJUEGO_CREADO",
                "Videojuego",
                videojuegoId.toString(),
                "Se ha creado el videojuego " + titulo + " con id " + videojuegoId
        );
    }

    @Async
    public void registrarVideojuegoActualizado(Long videojuegoId, String titulo) {
        registrar(
                "VIDEOJUEGO_ACTUALIZADO",
                "Videojuego",
                videojuegoId.toString(),
                "Se ha actualizado el videojuego " + titulo + " con id " + videojuegoId
        );
    }

    @Async
    public void registrarVideojuegoEliminado(Long videojuegoId) {
        registrar(
                "VIDEOJUEGO_ELIMINADO",
                "Videojuego",
                videojuegoId.toString(),
                "Se ha eliminado el videojuego con id " + videojuegoId
        );
    }

    public List<ActividadResponseDTO> findUltimas() {
        return actividadRepository.findTop20ByOrderByFechaDesc()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private void registrar(String tipo, String entidad, String entidadId, String descripcion) {
        Actividad actividad = new Actividad();
        actividad.setTipo(tipo);
        actividad.setEntidad(entidad);
        actividad.setEntidadId(entidadId);
        actividad.setDescripcion(descripcion);
        actividad.setFecha(LocalDateTime.now());

        actividadRepository.save(actividad);
    }

    private ActividadResponseDTO mapToDTO(Actividad actividad) {
        return new ActividadResponseDTO(
                actividad.getId(),
                actividad.getTipo(),
                actividad.getEntidad(),
                actividad.getEntidadId(),
                actividad.getDescripcion(),
                actividad.getFecha()
        );
    }
}