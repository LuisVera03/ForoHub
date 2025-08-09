package com.alura.forohub.repository;

import com.alura.forohub.domain.usuario.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
}
