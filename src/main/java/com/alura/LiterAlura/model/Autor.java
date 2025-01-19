package com.alura.LiterAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="autors")
//@JsonIgnoreProperties(ignoreUnknown=true)
public class Autor {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("name")
    private String nombre;
    @JsonProperty("birth_year")
    private Integer anioNacimiento;
    @JsonProperty("death_year")
    private Integer anioDefuncion;
    //@OneToMany(mappedBy = "autores", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OneToMany(mappedBy = "autores", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Libro> libro =  new ArrayList<>();

    public Autor() {}

    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.anioNacimiento = datosAutor.anioNac();
        this.anioDefuncion = datosAutor.anioDef();
    }

    public Autor(Autor datosAutor) {
    }

    @Override
    public String toString() {
        return "***************************AUTOR****************************\n" +
                "Nombre='" + nombre + "\n" +
                "AñoNacimiento=" + anioNacimiento + "\n" +
                "AñoDefuncion=" + anioDefuncion + "\n" +
                "************************************************************\n";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getAnioNacimiento() {
        return anioNacimiento;
    }

    public void setAnioNacimiento(Integer anioNacimiento) {
        this.anioNacimiento = anioNacimiento;
    }

    public Integer getAnioDefuncion() {
        return anioDefuncion;
    }

    public void setAnioDefuncion(Integer anioDefuncion) {
        this.anioDefuncion = anioDefuncion;
    }

    public List<Libro> getLibro() {
        return libro;
    }

    public void setLibro(List<Libro> libro) {
        this.libro = libro;
    }
}
