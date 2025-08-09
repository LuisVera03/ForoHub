package com.alura.forohub.repository;

import com.alura.forohub.domain.topico.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    boolean existsByTituloAndMensaje(String titulo, String mensaje);

    @Query("select t from Topico t where (:curso is null or lower(t.curso.nombre) like lower(concat('%', :curso, '%'))) and (:anio is null or function('YEAR', t.fechaCreacion) = :anio)")
    Page<Topico> search(@Param("curso") String curso,
                        @Param("anio") Integer anio,
                        Pageable pageable);
}
