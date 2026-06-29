package com.aleroig.gamevault.catalogo;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = "videojuego")
@Data
public class Videojuego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(precision = 10, scale = 2)
    private BigDecimal precio;

    private LocalDate fechaLanzamiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudio_id", nullable = false)
    private Estudio estudio;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> detallesPlataforma;
}

