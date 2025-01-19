package com.alura.LiterAlura.model;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String idioma; //enum
    private int numeroDescargas;
    //private String genero;
    //@ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autores_id")
    private Autor autores;

    public Libro(DatosLibro datosLibro, Autor autor) {
        this.titulo = datosLibro.titulo();
        setIdioma(datosLibro.idioma());
        //this.idioma = datosLibro.idioma();
        this.numeroDescargas = datosLibro.descargas();
        this.autores = autor;
    }

    public Libro() {
    }

    @Override
    public String toString() {
        return "***************************LIBRO****************************\n" +
                "Titulo=" + titulo + "\n" +
                "Idioma=" + idioma + "\n" +
                "NumeroDescargas=" + numeroDescargas + "\n" +
                "Autor=" + autores.getNombre() + "\n" +
                "************************************************************\n" ;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getIdioma() {
        return Arrays.asList(idioma.split(","));
    }

    public void setIdioma(List<String> idioma) {
        this.idioma = String.join(",", idioma);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(int numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }


    public Autor getAutores() {
        return autores;
    }

    public void setAutores(Autor autores) {
        this.autores = autores;
    }
}
