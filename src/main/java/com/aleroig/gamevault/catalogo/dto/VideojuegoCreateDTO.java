package com.aleroig.gamevault.catalogo.dto;

import java.time.LocalDate;
import java.util.Map;

public record VideojuegoCreateDTO(
        String titulo,
        Double precio,
        LocalDate fechaLanzamiento,
        Long estudioId,
        Map<String, Object> detallesPlataforma
) {}
