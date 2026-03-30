package com.aleroig.gamevault.catalogo.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

public record VideojuegoResponseDTO(
        Long id,
        String titulo,
        Double precio,
        LocalDate fechaLanzamiento,
        EstudioDTO estudio,
        Map<String, Object> detallesPlataforma
) implements Serializable {}
