package com.aleroig.gamevault.actividad;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "actividad")
@Data
@NoArgsConstructor
public class Actividad {

    @Id
    private String id;

    private String tipo;
    private String entidad;
    private String entidadId;
    private String descripcion;
    private LocalDateTime fecha;
}