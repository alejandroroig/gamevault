package com.aleroig.gamevault.catalogo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record VideojuegoCreateDTO(
        @NotBlank(message = "El título no puede estar vacío")
        String titulo,

        @NotNull(message = "El precio es obligatorio")
        @PositiveOrZero(message = "El precio no puede ser negativo")
        BigDecimal precio,

        @PastOrPresent(message = "La fecha de lanzamiento no puede ser futura")
        LocalDate fechaLanzamiento,

        @NotNull(message = "El ID del estudio es obligatorio")
        Long estudioId,

        // El JSONB puede ser nulo o vacío, así que no le ponemos validación estricta
        Map<String, Object> detallesPlataforma
) {}
