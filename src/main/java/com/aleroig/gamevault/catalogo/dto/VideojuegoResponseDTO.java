package com.aleroig.gamevault.catalogo.dto;

import java.time.LocalDate;
import java.util.Map;

public record VideojuegoResponseDTO(
        Long id,
        String titulo,
        Double precio,
        LocalDate fechaLanzamiento,
        EstudioDTO estudio,
        Map<String, Object> detallesPlataforma
) {}
