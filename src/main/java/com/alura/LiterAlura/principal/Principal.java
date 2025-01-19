package com.alura.LiterAlura.principal;

import ch.qos.logback.core.encoder.JsonEscapeUtil;
import com.alura.LiterAlura.model.*;
import com.alura.LiterAlura.repository.AutorRepository;
import com.alura.LiterAlura.repository.LibroRepository;
import com.alura.LiterAlura.services.ConsumoAPI;
import com.alura.LiterAlura.services.ConvierteDatos;
import com.fasterxml.jackson.core.JsonToken;

import java.net.URL;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Principal {
    //VARIABLE PARA INGRESAR DATOS
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<Libro> librosRegistrados = new ArrayList<>();
    private List<Autor> autoresRegistrados = new ArrayList<>();
    private Datos datos;

    private AutorRepository autorRepository;
    private LibroRepository libroRepository;

    public Principal(AutorRepository autorRepository, LibroRepository libroRepository) {
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

    public DatosLibro mostrar(){
        var opcion = 1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libros por titulo (Agregar a la libreria)
                    2 - Listar libros registrados (Mostrar lista de libros)
                    3 - Listar autores registrados (Mostrar lista de autores)
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    agregarLibros();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutorFechas();
                    break;
                case 5:
                    listarLibrosIdiomas();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
        return null;
    }

    private void listarLibrosIdiomas() {
        String idioma;
        System.out.println("Idiomas disponibles\n" +
                "1 - Español\n" +
                "2 - Ingles\n" +
                "3 - Frances\n" +
                "4 - Portugues");
        var opcion = teclado.nextInt();
        teclado.nextLine();
        if (opcion == 1) {
            idioma = "es";
        } else if (opcion == 2) {
            idioma = "en";
        } else if (opcion == 3) {
            idioma = "fr";
        } else if (opcion == 4) {
            idioma = "pt";
        }else {
            idioma = null;
            System.out.println("Opcion no valida");
        }

        List<Libro> librosIdiomas = libroRepository.findByIdiomaContaining(idioma);
        if (librosIdiomas.isEmpty()) {
            System.out.println("Libro no encontrado para ese idioma");
        }else {
            System.out.println("Libros encontrados");
            librosIdiomas.forEach(System.out::println);
        }
    }

    private void listarAutorFechas() {
        System.out.println("Ingrese un año de inicio de busqueda:");
        int yearInicio = teclado.nextInt();
        teclado.nextLine();
        System.out.println("Ingrese un año de fin de busqueda:");
        int yearFin = teclado.nextInt();
        teclado.nextLine();
        try {
            teclado.nextLine();
            List<Autor> autoresVivos = autorRepository.autoresVivosEnDeterminadoAnio(yearInicio,yearFin);
            if (autoresVivos == null && autoresVivos.isEmpty()){
                System.out.println("No hay autores registrados");
            } else {
                autoresVivos.forEach(autor -> System.out.println(autor.toString()));
            }
        } catch (InputMismatchException e) {
            teclado.nextLine();
            System.out.println("Ingrese el año en numero");
        }
    }

    //GET DATOS DE LIBROS
    private Datos extraerDatosLibros(){
        var tituloLibros = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibros.replace(" ","+"));
        Datos datos1 = conversor.obtenerDatos(json, Datos.class);
        return datos1;
    }

    private Libro crearLibro(DatosLibro datosLibro, Autor autor){
        if (autor!=null){
            return new Libro(datosLibro, autor);
        }else{
            System.out.println("el autor es null");
            return null;
        }
    }

    private void agregarLibros(){
        System.out.println("Escriba el libro que deseas buscar");
        Datos datos2 = extraerDatosLibros();
        if (datos2.resultados() != null && !datos2.resultados().isEmpty()){
            DatosLibro datosLibro = datos2.resultados().get(0);
            DatosAutor datosAutor = datosLibro.autor().get(0);

            Libro libro = null;
            Libro librosRepositorio = libroRepository.findByTitulo(datosLibro.titulo());
            if (librosRepositorio != null){
                System.out.println("Este libro ya esta registrado");
                System.out.println(librosRepositorio.toString());
            }else {
                Autor autorRepositorio = autorRepository.findByNombreIgnoreCase(datosLibro.autor().get(0).nombre());
                if (autorRepositorio != null){
                    libro = crearLibro(datosLibro, autorRepositorio);
                    libroRepository.save(libro);
                    System.out.println("Libro agregado");
                    System.out.println(libro.toString());
                } else {
                    Autor autor = new Autor(datosAutor);
                    autor = autorRepository.save(autor);
                    libro = crearLibro(datosLibro, autor);
                    libroRepository.save(libro);
                    System.out.println("Libro agregado");
                    System.out.println(libro.toString());
                }
            }

        } else {
            System.out.println("libro no eb¿ncontrado");
        }
    }

    private void listarLibros(){
        List<Libro> librosRegistrados = libroRepository.findAll();
        if (librosRegistrados == null && librosRegistrados.isEmpty()){
            System.out.println("No hay libros registrados");
        }
        librosRegistrados.forEach(libro -> System.out.println(libro.toString()));
    }

    private void listarAutores(){
        List<Autor> autoresRegistrados = autorRepository.findAll();
        if (autoresRegistrados == null && autoresRegistrados.isEmpty()){
            System.out.println("No hay autores registrados");
        }
        autoresRegistrados.forEach(autor -> System.out.println(autor.toString()));
    }

}
