package com.aleroig.gamevault.reviews.dto;

public record ReviewResumenDTO(
        Long videojuegoId,
        long totalReviews,
        double puntuacionMedia
) {
}