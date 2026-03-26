package com.aleroig.gamevault.catalogo;

import com.aleroig.gamevault.catalogo.dto.EstudioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstudioService {

    private final EstudioRepository estudioRepository;

    public List<EstudioDTO> findAll() {
        return estudioRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public EstudioDTO create(EstudioDTO dto) {
        Estudio estudio = new Estudio();
        estudio.setNombre(dto.nombre());
        estudio.setPais(dto.pais());

        Estudio saved = estudioRepository.save(estudio);
        return mapToDTO(saved);
    }

    // Mapeo manual
    private EstudioDTO mapToDTO(Estudio estudio) {
        return new EstudioDTO(estudio.getId(), estudio.getNombre(), estudio.getPais());
    }
}
