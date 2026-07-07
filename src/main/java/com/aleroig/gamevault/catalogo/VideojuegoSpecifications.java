package com.aleroig.gamevault.catalogo;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public final class VideojuegoSpecifications {

    private VideojuegoSpecifications() { }

    public static Specification<Videojuego> tituloContiene(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            return Specification.unrestricted();
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("titulo")),
                        "%" + titulo.toLowerCase() + "%"
                );
    }

    public static Specification<Videojuego> precioMayorOIgualA(BigDecimal precioMin) {
        if (precioMin == null) {
            return Specification.unrestricted();
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("precio"), precioMin);
    }

    public static Specification<Videojuego> precioMenorOIgualA(BigDecimal precioMax) {
        if (precioMax == null) {
            return Specification.unrestricted();
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("precio"), precioMax);
    }

    public static Specification<Videojuego> perteneceAlEstudio(Long estudioId) {
        if (estudioId == null) {
            return Specification.unrestricted();
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("estudio").get("id"), estudioId);
    }

    public static Specification<Videojuego> disponibleEnPlataforma(String plataforma) {
        if (plataforma == null || plataforma.isBlank()) {
            return Specification.unrestricted();
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(
                        criteriaBuilder.function(
                                "jsonb_exists",
                                Boolean.class,
                                root.get("detallesPlataforma"),
                                criteriaBuilder.literal(plataforma.toLowerCase())
                        )
                );
    }
}