package com.aleroig.gamevault.actividad;

import java.time.LocalDateTime;

public record ActividadResponseDTO(
        String id,
        String tipo,
        String entidad,
        String entidadId,
        String descripcion,
        LocalDateTime fecha
) {
}