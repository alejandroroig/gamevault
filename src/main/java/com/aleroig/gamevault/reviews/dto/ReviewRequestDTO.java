package com.aleroig.gamevault.reviews.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewRequestDTO(
        @NotNull(message = "La puntuación es obligatoria")
        @Min(value = 0, message = "La puntuación mínima es 0")
        @Max(value = 10, message = "La puntuación máxima es 10")
        Integer puntuacion,

        @NotBlank(message = "El comentario es obligatorio")
        String comentario
) {}