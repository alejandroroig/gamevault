package com.aleroig.gamevault.catalogo.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record VideojuegoCreateDTO(
        @NotBlank(message = "El título no puede estar vacío")
        @Size(max = 150, message = "El título no puede superar los 150 caracteres")
        String titulo,

        @NotNull(message = "El precio es obligatorio")
        @PositiveOrZero(message = "El precio no puede ser negativo")
        @Digits(integer = 8, fraction = 2, message = "El precio debe tener como máximo 8 cifras enteras y 2 decimales")
        BigDecimal precio,

        @NotNull(message = "La fecha de lanzamiento es obligatoria")
        @PastOrPresent(message = "La fecha de lanzamiento no puede ser futura")
        LocalDate fechaLanzamiento,

        @NotNull(message = "El ID del estudio es obligatorio")
        @Positive(message = "El ID del estudio debe ser positivo")
        Long estudioId,

        // El JSONB puede ser nulo o vacío, así que no le ponemos validación estricta
        Map<String, Object> detallesPlataforma
) {}
