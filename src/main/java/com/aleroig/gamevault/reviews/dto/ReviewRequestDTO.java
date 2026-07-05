package com.aleroig.gamevault.reviews.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewRequestDTO(
        @NotBlank(message = "El autor no puede estar vacío")
        String autor,

        @NotNull(message = "La puntuación es obligatoria")
        @Min(value = 1, message = "La puntuación mínima es 1")
        @Max(value = 10, message = "La puntuación máxima es 10")
        Integer puntuacion,

        @NotBlank(message = "El comentario es obligatorio")
        String comentario
) {}