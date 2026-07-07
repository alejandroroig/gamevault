package com.aleroig.gamevault.reviews;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByVideojuegoId(Long videojuegoId); // Autoimplementado
    long deleteByVideojuegoId(Long videojuegoId);
}
