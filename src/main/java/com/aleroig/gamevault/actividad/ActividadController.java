package com.aleroig.gamevault.actividad;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actividad")
@RequiredArgsConstructor
public class ActividadController {

    private final ActividadService actividadService;

    @GetMapping
    public ResponseEntity<List<ActividadResponseDTO>> getUltimas() {
        return ResponseEntity.ok(actividadService.findUltimas());
    }
}