package com.alura.forohub.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class TopicoDtos {
    public record TopicoCreateRequest(
            @NotBlank String titulo,
            @NotBlank String mensaje,
            @NotNull Long autorId,
            @NotNull Long cursoId
    ) {}

    public record TopicoUpdateRequest(
            @NotBlank String titulo,
            @NotBlank String mensaje,
            @NotNull Long cursoId,
            @NotBlank String status
    ) {}

    public record TopicoResponse(
            Long id,
            String titulo,
            String mensaje,
            LocalDateTime fechaCreacion,
            String status,
            String autor,
            String curso,
            Integer respuestas
    ) {}
}
