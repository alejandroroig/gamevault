package com.aleroig.gamevault.catalogo;

import com.aleroig.gamevault.catalogo.dto.EstudioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudios")
@RequiredArgsConstructor
public class EstudioController {
    private final EstudioService estudioService;

    @GetMapping
    public ResponseEntity<List<EstudioDTO>> getAll() {
        return ResponseEntity.ok(estudioService.findAll());
    }

    @PostMapping
    public ResponseEntity<EstudioDTO> create(@RequestBody EstudioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estudioService.create(dto));
    }
}
