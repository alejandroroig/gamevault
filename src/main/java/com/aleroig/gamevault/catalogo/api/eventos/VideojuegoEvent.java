package com.aleroig.gamevault.catalogo.api.eventos;

public record VideojuegoEvent(
        String tipo,
        Long videojuegoId,
        String titulo
) {
    public static final String VIDEOJUEGO_CREADO = "VIDEOJUEGO_CREADO";
    public static final String VIDEOJUEGO_ACTUALIZADO = "VIDEOJUEGO_ACTUALIZADO";
    public static final String VIDEOJUEGO_ELIMINADO = "VIDEOJUEGO_ELIMINADO";
}