package com.aleroig.gamevault.catalogo;

import com.aleroig.gamevault.catalogo.dto.VideojuegoCreateDTO;
import com.aleroig.gamevault.catalogo.dto.VideojuegoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videojuegos")
@RequiredArgsConstructor
public class VideeojuegoController {
    private final VideojuegoService videojuegoService;

    @GetMapping
    public ResponseEntity<List<VideojuegoResponseDTO>> getAll() {
        return ResponseEntity.ok(videojuegoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideojuegoResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(videojuegoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<VideojuegoResponseDTO> create(@Valid @RequestBody VideojuegoCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(videojuegoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideojuegoResponseDTO> update(@PathVariable Long id, @Valid @RequestBody VideojuegoCreateDTO dto) {
        return ResponseEntity.ok(videojuegoService.update(id, dto));
    }
}
