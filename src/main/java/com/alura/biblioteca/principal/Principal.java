package com.alura.biblioteca.principal;

import com.alura.biblioteca.datos.AutorDatos;
import com.alura.biblioteca.datos.LibroDatos;
import com.alura.biblioteca.entidades.Autor;
import com.alura.biblioteca.entidades.Libro;
import com.alura.biblioteca.repositorios.AutorRepositorio;
import com.alura.biblioteca.repositorios.LibroRepositorio;
import com.alura.biblioteca.servicios.ConsumoAPI;
import com.alura.biblioteca.servicios.ConversorDatos;
import com.alura.biblioteca.servicios.ConversorDatosAutor;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConversorDatos conversor = new ConversorDatos();
    private ConversorDatosAutor conversorAutor = new ConversorDatosAutor();
    private final String URL_BASE = "https://gutendex.com/books/";
    private Scanner teclado = new Scanner(System.in);
    private LibroRepositorio repositorioLibro;
    private AutorRepositorio repositorioAutor;

    public Principal(LibroRepositorio repositorioLibro, AutorRepositorio repositorioAutor) {
        this.repositorioLibro = repositorioLibro;
        this.repositorioAutor = repositorioAutor;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    Elija la tarea a través de su número:
                    1- buscar libro por título
                    2- listar libros 
                    3- listar autores 
                    4- listar autores vivos en un determinado año
                    5- listar libros por idioma
                    6- listar libros por cantidad de descargas
                    0 - Salir.
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion) {
                case 1:
                    busquedaPorTitulo();
                    break;
                case 2:
                    mostrarLibros();
                    break;
                case 3:
                    mostrarAutores();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    busquedaPorIdioma();
                    break;
                case 6:
                    mostrarPorDescargas();
                    break;
                case 0:
                    System.out.println("Saliendo del sistema...");
                    opcion = 0;
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }
    private void busquedaPorTitulo() {
        String tituloLibro = busqueda();
        Optional<Libro> libroOptional = repositorioLibro.findAll().stream()
                .filter(libro -> libro.getTitulo().toLowerCase().contains(tituloLibro.toLowerCase()))
                .findFirst();

        if (libroOptional.isPresent()) {
            Libro libroEncontrado = libroOptional.get();
            System.out.println(libroEncontrado);
            System.out.println("Libro registrado");
        } else {
            try {
                LibroDatos libroDatos = getLibroDatos(tituloLibro);
                System.out.println(libroDatos);

                if (libroDatos != null) {
                    AutorDatos autorDatos = getAutorDatos(tituloLibro);
                    Autor autor = obtenerAutor(autorDatos);

                    Libro libro = new Libro(
                            libroDatos.titulo(),
                            autor,
                            libroDatos.idiomas(),
                            libroDatos.descargas()
                    );

                    repositorioLibro.save(libro);

                    System.out.println("Libro guardado exitosamente");
                    System.out.println(libro);
                } else {
                    System.out.println("No se encontró el libro.");
                }
            } catch (Exception e) {
                System.out.println("Excepción: " + e.getMessage());
            }
        }
    }

    private LibroDatos getLibroDatos(String tituloLibro) {
        String json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        return conversor.obtenerDatos(json, LibroDatos.class);
    }

    private AutorDatos getAutorDatos(String tituloLibro) {
        String json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        return conversorAutor.obtenerDatos(json, AutorDatos.class);
    }

    private Autor obtenerAutor(AutorDatos autorDatos) {
        List<Autor> autores = repositorioAutor.findAll();
        Optional<Autor> autorOptional = autores.stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(autorDatos.nombre()))
                .findFirst();

        Autor autor;
        if (autorOptional.isPresent()) {
            autor = autorOptional.get();
        } else {
            autor = new Autor(
                    autorDatos.nombre(),
                    autorDatos.nacimiento(),
                    autorDatos.fallecimiento()
            );
            repositorioAutor.save(autor);
        }
        return autor;
    }

    private String busqueda() {
        System.out.println("Escribir el titulo del libro buscado:");
        return teclado.nextLine();
    }

    private void mostrarLibros() {
        List<Libro> libros = repositorioLibro.findAll();
        libros.forEach(libro -> {
            System.out.println("Título: " + libro.getTitulo());
            System.out.println("Autor: " + libro.getAutor().getNombre());
            System.out.println("Idiomas: " + libro.getIdioma());
            System.out.println("Número de descargas: " + libro.getDescargas());
            System.out.println("-------------------");
        });
    }

    private void mostrarAutores() {
        List<Autor> autores = repositorioAutor.findAll();
        autores.forEach(autor -> {
            System.out.println("Autor: " + autor.getNombre());
            System.out.println("Nacimiento: " + autor.getNacimiento());
            System.out.println("Fallecimiento: " + autor.getFallecimiento());
            System.out.println("Libros:");
            autor.getLibros().forEach(libro -> {
                System.out.println(" - " + libro.getTitulo());
            });
            System.out.println("-------------------");
        });
    }

    private void listarAutoresVivos() {
        System.out.println("Ingrese un año:");
        int anio = teclado.nextInt();
        teclado.nextLine();
        List<Autor> autores = repositorioLibro.findAutoresVivos(anio);

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + anio + ".");
        } else {
            autores.forEach(autor -> {
                System.out.println("Autor: " + autor.getNombre());
                System.out.println("Nacimiento: " + autor.getNacimiento());
                System.out.println("Fallecimiento: " + autor.getFallecimiento());
                System.out.println("Libros:");
                autor.getLibros().forEach(libro -> {
                    System.out.println(" - " + libro.getTitulo());
                });
                System.out.println("-------------------");
            });
        }
    }

    private void busquedaPorIdioma() {
        System.out.println("Ingrese el idioma de los libros buscados:");
        String idioma = teclado.nextLine();

        List<Libro> libros = repositorioLibro.findByIdioma(idioma);
        libros.forEach(libro -> {
            System.out.println("Título: " + libro.getTitulo());
            System.out.println("Autor: " + libro.getAutor().getNombre());
            System.out.println("Número de descargas: " + libro.getDescargas());
            System.out.println("-------------------");
        });
    }

    private void mostrarPorDescargas(){

        List<Libro> libros = repositorioLibro.findByDescargas();
        libros.forEach(libro -> {
            System.out.println("Título: " + libro.getTitulo());
            System.out.println("Autor: " + libro.getAutor().getNombre());
            System.out.println("Número de descargas: " + libro.getDescargas());
            System.out.println("-------------------");
        });
    }
}
