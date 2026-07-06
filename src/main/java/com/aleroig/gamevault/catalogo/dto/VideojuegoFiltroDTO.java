package com.aleroig.gamevault.catalogo.dto;

import java.math.BigDecimal;

public record VideojuegoFiltroDTO(
        String titulo,
        BigDecimal precioMin,
        BigDecimal precioMax,
        Long estudioId
) {
}