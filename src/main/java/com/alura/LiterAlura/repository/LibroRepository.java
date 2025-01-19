package com.alura.LiterAlura.repository;

import com.alura.LiterAlura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Libro findByTitulo(String titulo);

    List<Libro> findByIdiomaContaining(String idioma);
}
