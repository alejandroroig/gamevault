package com.aleroig.gamevault.seguridad.dto;

public record LoginResponseDTO(
        String accessToken,
        String tokenType,
        long expiresInSeconds
) {
}