package com.svillegas.literalura.repository;

import com.svillegas.literalura.model.Libro;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTitulo(String titulo);
    List<Libro> findByIdiomas(String idioma);

    @EntityGraph(attributePaths = {"autores"})
    List<Libro> findAll();

    List<Libro> findTop5ByOrderByDescargasDesc();
}
