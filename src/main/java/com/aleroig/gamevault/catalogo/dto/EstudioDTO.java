package com.aleroig.gamevault.catalogo.dto;

import java.io.Serializable;

public record EstudioDTO(
        Long id,
        String nombre,
        String pais
) implements Serializable {}
