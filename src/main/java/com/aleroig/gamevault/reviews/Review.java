package com.aleroig.gamevault.reviews;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "review")
@Data
@NoArgsConstructor
public class Review {

    @Id
    private String id; // En Mongo los IDs son alfanuméricos

    private Long videojuegoId; // Relación "lógica" con el catálogo en Postgres
    private String autor;
    private Integer puntuacion;
    private String comentario;
}
