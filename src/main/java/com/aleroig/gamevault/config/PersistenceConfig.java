package com.aleroig.gamevault.config;

import com.aleroig.gamevault.catalogo.Estudio;
import com.aleroig.gamevault.catalogo.EstudioRepository;
import com.aleroig.gamevault.catalogo.Videojuego;
import com.aleroig.gamevault.catalogo.VideojuegoRepository;
import com.aleroig.gamevault.reviews.ReviewRepository;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EntityScan(basePackageClasses = {
        Estudio.class,
        Videojuego.class
})
@EnableJpaRepositories(basePackageClasses = {
        EstudioRepository.class,
        VideojuegoRepository.class
})
@EnableMongoRepositories(basePackageClasses = {
        ReviewRepository.class
})
public class PersistenceConfig {
}