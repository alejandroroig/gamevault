package com.aleroig.gamevault.seguridad;

import com.aleroig.gamevault.seguridad.dto.LoginRequestDTO;
import com.aleroig.gamevault.seguridad.dto.LoginResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
        );

        String token = jwtService.generarToken(authentication);

        return ResponseEntity.ok(new LoginResponseDTO(
                token,
                "Bearer",
                jwtService.getExpiresInSeconds()
        ));
    }
}