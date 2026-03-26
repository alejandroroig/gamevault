package com.aleroig.gamevault.catalogo;

import com.aleroig.gamevault.catalogo.dto.EstudioDTO;
import com.aleroig.gamevault.catalogo.dto.VideojuegoCreateDTO;
import com.aleroig.gamevault.catalogo.dto.VideojuegoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideojuegoService {
    private final VideojuegoRepository videojuegoRepository;
    private final EstudioRepository estudioRepository;

    public List<VideojuegoResponseDTO> findAll() {
        return videojuegoRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public VideojuegoResponseDTO findById(Long id) {
        return videojuegoRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Videojuego no encontrado"));
    }

    public VideojuegoResponseDTO create(VideojuegoCreateDTO dto) {
        Estudio estudio = estudioRepository.findById(dto.estudioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudio no encontrado"));

        Videojuego v = new Videojuego();
        v.setTitulo(dto.titulo());
        v.setPrecio(dto.precio());
        v.setFechaLanzamiento(dto.fechaLanzamiento());
        v.setEstudio(estudio);
        v.setDetallesPlataforma(dto.detallesPlataforma()); // Guardamos el JSONB

        Videojuego saved = videojuegoRepository.save(v);
        return mapToDTO(saved);
    }

    public VideojuegoResponseDTO update(Long id, VideojuegoCreateDTO dto) {
        Videojuego v = videojuegoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Videojuego no encontrado"));

        Estudio estudio = estudioRepository.findById(dto.estudioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudio no encontrado"));

        v.setTitulo(dto.titulo());
        v.setPrecio(dto.precio());
        v.setFechaLanzamiento(dto.fechaLanzamiento());
        v.setEstudio(estudio);
        v.setDetallesPlataforma(dto.detallesPlataforma()); // Actualizamos el JSONB

        Videojuego saved = videojuegoRepository.save(v);
        return mapToDTO(saved);
    }

    // Mapeo manual
    private VideojuegoResponseDTO mapToDTO(Videojuego v) {
        EstudioDTO estudioDTO = new EstudioDTO(v.getEstudio().getId(), v.getEstudio().getNombre(), v.getEstudio().getPais());
        return new VideojuegoResponseDTO(
                v.getId(), v.getTitulo(), v.getPrecio(), v.getFechaLanzamiento(), estudioDTO, v.getDetallesPlataforma()
        );
    }
}
