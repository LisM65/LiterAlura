package com.alura.LiterAlura.repository;

import com.alura.LiterAlura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Autor findByNombreIgnoreCase(String nombre);

    List<Autor> findByAnioNacimientoLessThanEqualAndAnioDefuncionGreaterThanEqual(int añoInicial, int añoFinal);

    @Query("SELECT a FROM Autor a WHERE a.anioNacimiento >= :yearInicio AND a.anioDefuncion <= :yearFin")
    List<Autor> autoresVivosEnDeterminadoAnio(Integer yearInicio,Integer yearFin);
}
