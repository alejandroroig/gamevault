package com.aleroig.gamevault.seguridad;

import com.aleroig.gamevault.openapi.api.AuthApi;
import com.aleroig.gamevault.openapi.model.AuthMeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthMeController implements AuthApi {

    @Override
    public ResponseEntity<AuthMeResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> authority.replace("ROLE_", ""))
                .toList();

        AuthMeResponse response = new AuthMeResponse()
                .username(authentication.getName())
                .roles(roles);

        return ResponseEntity.ok(response);
    }
}