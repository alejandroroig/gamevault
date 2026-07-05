package com.aleroig.gamevault.reviews;

import com.aleroig.gamevault.reviews.dto.ReviewResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videojuegos/{videojuegoId}/reviews")
@RequiredArgsConstructor
public class VideojuegoReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewResponseDTO>> getByVideojuegoId(@PathVariable Long videojuegoId) {
        return ResponseEntity.ok(reviewService.findByVideojuegoId(videojuegoId));
    }
}