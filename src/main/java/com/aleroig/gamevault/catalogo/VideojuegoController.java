package com.aleroig.gamevault.catalogo;

import com.aleroig.gamevault.catalogo.dto.VideojuegoCreateDTO;
import com.aleroig.gamevault.catalogo.dto.VideojuegoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videojuegos")
@RequiredArgsConstructor
public class VideojuegoController {
    private final VideojuegoService videojuegoService;

    @GetMapping
    public ResponseEntity<Page<VideojuegoResponseDTO>> getAll(@PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(videojuegoService.findAllPaginated(pageable));
    }

    @GetMapping("/top")
    public ResponseEntity<List<VideojuegoResponseDTO>> getTopNovedades() {
        return ResponseEntity.ok(videojuegoService.getTopNovedades());
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
