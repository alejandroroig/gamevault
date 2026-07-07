package com.aleroig.gamevault.catalogo;

import com.aleroig.gamevault.catalogo.api.CatalogoConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CatalogoConsultaServiceImpl implements CatalogoConsultaService {

    private final VideojuegoRepository videojuegoRepository;

    @Override
    public boolean existeVideojuego(Long videojuegoId) {
        return videojuegoRepository.existsById(videojuegoId);
    }
}