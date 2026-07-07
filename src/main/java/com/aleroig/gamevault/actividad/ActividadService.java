package com.aleroig.gamevault.actividad;

import com.aleroig.gamevault.actividad.mensajeria.ActividadEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActividadService {

    private final ActividadRepository actividadRepository;

    public void registrar(ActividadEvent event) {
        Actividad actividad = new Actividad();
        actividad.setTipo(event.tipo());
        actividad.setEntidad(event.entidad());
        actividad.setEntidadId(event.entidadId());
        actividad.setDescripcion(event.descripcion());
        actividad.setFecha(LocalDateTime.now());

        actividadRepository.save(actividad);
    }

    public List<ActividadResponseDTO> findUltimas() {
        return actividadRepository.findTop20ByOrderByFechaDesc()
                .stream()
                .map(this::mapToDTO)
                .toList();
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