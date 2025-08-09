package com.alura.forohub.domain.usuario;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Perfil")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;
}
