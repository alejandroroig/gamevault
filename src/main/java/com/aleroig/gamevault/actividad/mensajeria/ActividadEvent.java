package com.aleroig.gamevault.actividad.mensajeria;

public record ActividadEvent(
        String tipo,
        String entidad,
        String entidadId,
        String descripcion
) {
}