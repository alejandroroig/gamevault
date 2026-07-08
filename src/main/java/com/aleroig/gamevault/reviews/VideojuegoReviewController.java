package com.aleroig.gamevault.reviews;

import com.aleroig.gamevault.reviews.dto.ReviewCreateDTO;
import com.aleroig.gamevault.reviews.dto.ReviewRequestDTO;
import com.aleroig.gamevault.reviews.dto.ReviewResponseDTO;
import com.aleroig.gamevault.reviews.dto.ReviewResumenDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/videojuegos/{videojuegoId}/reviews")
@RequiredArgsConstructor
public class VideojuegoReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewResponseDTO>> getByVideojuegoId(@PathVariable Long videojuegoId) {
        return ResponseEntity.ok(reviewService.findByVideojuegoId(videojuegoId));
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> create(
            @PathVariable Long videojuegoId,
            @Valid @RequestBody ReviewRequestDTO dto,
            Principal principal
    ) {
        ReviewCreateDTO createDTO = new ReviewCreateDTO(
                videojuegoId,
                principal.getName(),
                dto.puntuacion(),
                dto.comentario()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.create(createDTO));
    }

    @GetMapping("/resumen")
    public ResponseEntity<ReviewResumenDTO> getResumenByVideojuegoId(@PathVariable Long videojuegoId) {
        return ResponseEntity.ok(reviewService.getResumenByVideojuegoId(videojuegoId));
    }
}