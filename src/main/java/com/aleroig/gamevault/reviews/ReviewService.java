package com.aleroig.gamevault.reviews;

import com.aleroig.gamevault.catalogo.VideojuegoRepository;
import com.aleroig.gamevault.reviews.dto.ReviewCreateDTO;
import com.aleroig.gamevault.reviews.dto.ReviewResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final VideojuegoRepository videojuegoRepository; // Importamos el de Postgres

    public List<ReviewResponseDTO> findByVideojuegoId(Long videojuegoId) {
        // 1. Verificamos que el juego exista en PostgreSQL antes de buscar sus reseñas
        if (!videojuegoRepository.existsById(videojuegoId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Videojuego no encontrado en el catálogo");
        }

        // 2. Buscamos en MongoDB y mapeamos
        return reviewRepository.findByVideojuegoId(videojuegoId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public ReviewResponseDTO create(ReviewCreateDTO dto) {
        // 1. ¡Integridad Referencial manual! Comprobamos Postgres antes de escribir en Mongo
        if (!videojuegoRepository.existsById(dto.videojuegoId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No puedes reseñar un juego que no existe en el catálogo");
        }

        // 2. Mapeamos y guardamos en Mongo
        Review review = new Review();
        review.setVideojuegoId(dto.videojuegoId());
        review.setAutor(dto.autor());
        review.setPuntuacion(dto.puntuacion());
        review.setComentario(dto.comentario());

        Review saved = reviewRepository.save(review);
        return mapToDTO(saved);
    }

    // Metodo auxiliar de mapeo manual
    private ReviewResponseDTO mapToDTO(Review r) {
        return new ReviewResponseDTO(r.getId(), r.getVideojuegoId(), r.getAutor(), r.getPuntuacion(), r.getComentario());
    }
}
