package com.alura.forohub.domain.topico;

import com.alura.forohub.domain.curso.Curso;
import com.alura.forohub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Topico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "fechaCreacion")
    private LocalDateTime fechaCreacion;

    @Column(length = 20)
    private String status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "autor")
    private Usuario autor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "curso")
    private Curso curso;

    private Integer respuestas;

    @PrePersist
    void prePersist() {
        if (fechaCreacion == null) fechaCreacion = LocalDateTime.now();
        if (status == null) status = "activo";
        if (respuestas == null) respuestas = 0;
    }
}
