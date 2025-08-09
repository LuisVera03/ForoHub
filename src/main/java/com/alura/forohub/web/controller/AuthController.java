package com.alura.forohub.web.controller;

import com.alura.forohub.domain.usuario.Usuario;
import com.alura.forohub.repository.UsuarioRepository;
import com.alura.forohub.security.TokenService;
import com.alura.forohub.web.dto.AuthDtos;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authManager, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.authManager = authManager;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthDtos.LoginRequest request) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.correoElectronico(), request.contrasena()));
        Usuario usuario = (Usuario) auth.getPrincipal();
        String token = tokenService.generarToken(usuario.getUsername());
        return ResponseEntity.ok(new AuthDtos.AuthResponse(token, "Bearer"));
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> register(@RequestBody @Valid AuthDtos.RegisterRequest request) {
        if (usuarioRepository.existsByCorreoElectronico(request.correoElectronico())) {
            return ResponseEntity.badRequest().body("El correo ya está registrado");
        }
        Usuario usuario = Usuario.builder()
                .nombre(request.nombre())
                .correoElectronico(request.correoElectronico())
                .contrasena(passwordEncoder.encode(request.contrasena()))
                .build();
        usuarioRepository.save(usuario);
        String token = tokenService.generarToken(usuario.getUsername());
        return ResponseEntity.ok(new AuthDtos.AuthResponse(token, "Bearer"));
    }
}
