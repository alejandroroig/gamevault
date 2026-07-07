package com.aleroig.gamevault.actividad;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ActividadRepository extends MongoRepository<Actividad, String> {

    List<Actividad> findTop20ByOrderByFechaDesc();
}