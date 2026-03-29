package com.aleroig.gamevault.reviews.dto;

public record ReviewResponseDTO(
        String id, // El ID de Mongo
        Long videojuegoId, // El ID de Postgres
        String autor,
        Integer puntuacion,
        String comentario
) {}
