package com.alura.forohub.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {
    public record LoginRequest(
            @NotBlank @Email String correoElectronico,
            @NotBlank String contrasena
    ) {}

    public record RegisterRequest(
            @NotBlank String nombre,
            @NotBlank @Email String correoElectronico,
            @NotBlank @Size(min = 6, max = 100) String contrasena
    ) {}

    public record AuthResponse(String token, String type) {}
}
