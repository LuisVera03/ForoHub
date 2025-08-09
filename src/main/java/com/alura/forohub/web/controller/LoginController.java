package com.alura.forohub.web.controller;

import com.alura.forohub.domain.usuario.Usuario;
import com.alura.forohub.security.TokenService;
import com.alura.forohub.web.dto.AuthDtos;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final AuthenticationManager authManager;
    private final TokenService tokenService;

    public LoginController(AuthenticationManager authManager, TokenService tokenService) {
        this.authManager = authManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthDtos.LoginRequest request) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.correoElectronico(), request.contrasena()));
        Usuario usuario = (Usuario) auth.getPrincipal();
        String token = tokenService.generarToken(usuario.getUsername());
        return ResponseEntity.ok(new AuthDtos.AuthResponse(token, "Bearer"));
    }
}
