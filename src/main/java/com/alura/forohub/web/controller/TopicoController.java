package com.alura.forohub.web.controller;

import com.alura.forohub.domain.curso.Curso;
import com.alura.forohub.domain.topico.Topico;
import com.alura.forohub.domain.usuario.Usuario;
import com.alura.forohub.repository.CursoRepository;
import com.alura.forohub.repository.TopicoRepository;
import com.alura.forohub.repository.UsuarioRepository;
import com.alura.forohub.web.dto.TopicoDtos;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private final TopicoRepository topicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;

    public TopicoController(TopicoRepository topicoRepository, UsuarioRepository usuarioRepository, CursoRepository cursoRepository) {
        this.topicoRepository = topicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
    }

    @GetMapping
    public Page<TopicoDtos.TopicoResponse> list(
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) Integer anio,
            @PageableDefault(size = 10, sort = "fechaCreacion") Pageable pageable) {
        if (curso != null || anio != null) {
            return topicoRepository.search(curso, anio, pageable).map(this::toResponse);
        }
        return topicoRepository.findAll(pageable).map(this::toResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        Optional<Topico> op = topicoRepository.findById(id);
        if (op.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toResponse(op.get()));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody @Valid TopicoDtos.TopicoCreateRequest body) {
        if (topicoRepository.existsByTituloAndMensaje(body.titulo(), body.mensaje())) {
            return ResponseEntity.badRequest().body("Tópico duplicado: mismo título y mensaje");
        }
        Usuario autor = usuarioRepository.findById(body.autorId()).orElse(null);
        if (autor == null) return ResponseEntity.badRequest().body("Autor no encontrado");
        Curso curso = cursoRepository.findById(body.cursoId()).orElse(null);
        if (curso == null) return ResponseEntity.badRequest().body("Curso no encontrado");
        Topico creado = topicoRepository.save(Topico.builder()
                .titulo(body.titulo())
                .mensaje(body.mensaje())
                .autor(autor)
                .curso(curso)
                .build());
        return ResponseEntity.created(URI.create("/topicos/" + creado.getId())).body(toResponse(creado));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid TopicoDtos.TopicoUpdateRequest body) {
        Optional<Topico> op = topicoRepository.findById(id);
        if (op.isEmpty()) return ResponseEntity.notFound().build();
        Topico t = op.get();
        Curso curso = cursoRepository.findById(body.cursoId()).orElse(null);
        if (curso == null) return ResponseEntity.badRequest().body("Curso no encontrado");
        t.setTitulo(body.titulo());
        t.setMensaje(body.mensaje());
        t.setCurso(curso);
        t.setStatus(body.status());
        return ResponseEntity.ok(toResponse(t));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!topicoRepository.existsById(id)) return ResponseEntity.notFound().build();
        topicoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private TopicoDtos.TopicoResponse toResponse(Topico t) {
        return new TopicoDtos.TopicoResponse(
                t.getId(),
                t.getTitulo(),
                t.getMensaje(),
                t.getFechaCreacion(),
                t.getStatus(),
                t.getAutor() != null ? t.getAutor().getNombre() : null,
                t.getCurso() != null ? t.getCurso().getNombre() : null,
                t.getRespuestas()
        );
    }
}
