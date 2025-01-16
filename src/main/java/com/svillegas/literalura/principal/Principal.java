package com.svillegas.literalura.principal;

import com.svillegas.literalura.model.*;
import com.svillegas.literalura.repository.AutorRepository;
import com.svillegas.literalura.repository.LibroRepository;
import com.svillegas.literalura.service.ConsumoAPI;
import com.svillegas.literalura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {

    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);
    private static final String URL_BASE = "https://gutendex.com/books/";
    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

//TODO ESTA ES LA VERSIONA MAS FUNCIONAL
    @Autowired
    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void MenuPrincipal(){
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ********************************************
                    Seleccione la opción a través de su número:
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados 
                    3 - Listar autores registrados
                    4 - Listar autores vivos en determinado año
                    5 - Buscar libros por idioma   
                    6 - Libros mas descargados            
                                  
                    0 - Salir
                    ********************************************
                    """;
            System.out.println(menu);
            try {
                // Intentar leer una opción válida
                opcion = teclado.nextInt();
                teclado.nextLine(); // Limpiar el buffer
            } catch (java.util.InputMismatchException e) {
                // Si ocurre una excepción (por ejemplo, texto en lugar de un número)
                System.out.println("Opción inválida. Por favor, ingresa un número.");
                teclado.nextLine(); // Limpiar el buffer del scanner
                continue; // Volver al inicio del ciclo
            }


            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;

                case 2:
                    listarLibros();
                    break;

                case 3:
                    listarAutores();
                    break;

                case 4:
                    autoresVivos();
                    break;

                case 5:
                    listarIdioma();
                    break;

                case 6:
                    top5Libros();
                    break;

                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }




    private void buscarLibro() {
        System.out.println("Escribe el libro:");
        var tituloLibro = teclado.nextLine();

        // Consumir la API
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        var datos = conversor.obtenerDatos(json, Datos.class);

        // Procesar solo la primera coincidencia
        datos.resultados().stream().findFirst().ifPresent(libroAPI -> {
            // Verificar si el libro ya existe en la base de datos
            Optional<Libro> libroExistente = libroRepository.findByTitulo(libroAPI.titulo());
            if (libroExistente.isEmpty()) {
                // Convertir los autores
                List<Autor> autores = libroAPI.autores().stream()
                        .map(autorAPI -> new Autor(
                                autorAPI.nombre(),
                                autorAPI.fechaDeNacimiento(),
                                autorAPI.fechaDeFallecimiento()))
                        .toList();

                // Crear y guardar el libro
                Libro nuevoLibro = new Libro(
                        libroAPI.titulo(),
                        autores,
                        libroAPI.idiomas(),
                        libroAPI.numeroDeDescargas()
                );

                // Verificar longitud del título antes de guardar
                if (nuevoLibro.getTitulo().length() > 255) {
                    nuevoLibro.setTitulo(nuevoLibro.getTitulo().substring(0, 255)); // Truncar si es necesario
                }

                libroRepository.save(nuevoLibro);
                System.out.println("Libro agregado!!!");
                System.out.println(nuevoLibro);
            } else {
                System.out.println("El libro ya está registrado: " + libroExistente.get());
            }
        });
    }


    public void listarLibros() {
        List<Libro> libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros disponibles.");
            return;
        }
        libros.forEach(libro -> {
            System.out.println("================= LIBRO =================");
            System.out.println(libro);
        });
    }

    private void listarAutores() {
        // Obtener la lista de autores con todos los libros relacionados
        List<Autor> autores = autorRepository.findAllWithLibros();

        // Usar un conjunto para evitar duplicados basados en el nombre del autor
        Set<String> nombresProcesados = new HashSet<>();

        // Procesar cada autor y mostrar la información
        autores.stream()
                .filter(autor -> nombresProcesados.add(autor.getNombre())) // Filtrar duplicados por nombre
                .forEach(autor -> {
                    System.out.println("================= AUTOR =================");
                    System.out.println("Autor: " + autor.getNombre());
                    System.out.println("Fecha de Nacimiento: " + autor.getFechaDeNacimiento());
                    System.out.println("Fecha de Fallecimiento: " + autor.getFechaDeFallecimiento());

                    // Mostrar los libros escritos por el autor
                    System.out.println("Libros:");
                    autor.getLibros().forEach(libro ->
                            System.out.println(" - " + libro.getTitulo())
                    );
                });
    }

    private void autoresVivos() {
        // Solicitar el año al usuario
        System.out.println("Ingresa un año:");
        int anio = teclado.nextInt();
        teclado.nextLine(); // Consumir el salto de línea

        // Obtener todos los autores
        List<Autor> autores = autorRepository.findAll();

        // Filtrar autores vivos en el año especificado
        List<Autor> autoresVivosEnElAno = autores.stream()
                .filter(autor -> autor.getFechaDeNacimiento() != null && autor.getFechaDeNacimiento() <= anio &&
                        (autor.getFechaDeFallecimiento() == null || autor.getFechaDeFallecimiento() >= anio))
                .collect(Collectors.toList());

        // Verificar si se encontraron autores
        if (autoresVivosEnElAno.isEmpty()) {
            System.out.println("No hay autores registrados para el año ingresado.");
        } else {
            // Mostrar los autores vivos en el año especificado
            autoresVivosEnElAno.forEach(autor -> {
                System.out.println("================= AUTOR =================");
                System.out.println("Autor: " + autor.getNombre());
                System.out.println("Fecha de Nacimiento: " + autor.getFechaDeNacimiento());
                System.out.println("Fecha de Fallecimiento: " +
                        (autor.getFechaDeFallecimiento() != null ? autor.getFechaDeFallecimiento() : "Sigue Vivo"));

                // Mostrar los libros del autor
                System.out.println("Libros:");
                autor.getLibros().forEach(libro ->
                        System.out.println(" - " + libro.getTitulo())
                );
            });
        }
    }

    private void listarIdioma() {
        // Mostrar opciones de idiomas
        System.out.println("Elige un idioma:");
        System.out.println("1. es - Español");
        System.out.println("2. en - Inglés");
        System.out.println("3. fr - Francés");
        System.out.println("4. pt - Portugués");

        // Leer la opción del usuario
        String idiomaSeleccionado = teclado.nextLine();

        // Validar que el idioma seleccionado sea uno de los posibles
        if (!idiomaSeleccionado.equals("es") && !idiomaSeleccionado.equals("en") &&
                !idiomaSeleccionado.equals("fr") && !idiomaSeleccionado.equals("pt")) {
            System.out.println("Opción no válida. Por favor ingresa uno de los siguientes idiomas: es, en, fr, pt.");
            return; // Salir si la opción no es válida
        }

        // Buscar libros por idioma
        List<Libro> librosPorIdioma = libroRepository.findByIdiomas(idiomaSeleccionado);

        // Mostrar la información de los libros encontrados
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma " + idiomaSeleccionado + ".");
        } else {
            librosPorIdioma.forEach(libro -> {
                System.out.println("================= LIBRO =================");
                System.out.println("Título: " + libro.getTitulo());
                libro.getAutores().forEach(autor ->
                        System.out.println("Autor: " + autor.getNombre())
                );
                System.out.println("Idioma: " + libro.getIdiomas());
                System.out.println("Número de Descargas: " + libro.getDescargas());
            });
        }
    }

    private void top5Libros() {
        List<Libro> topLibros = libroRepository.findTop5ByOrderByDescargasDesc();
        topLibros.forEach(l-> {
            System.out.println("================= LIBRO =================");
            System.out.println("Título: " + l.getTitulo());
            l.getAutores().forEach(autor ->
                    System.out.println("Autor: " + autor.getNombre())
            );
            System.out.println("Número de Descargas: " + l.getDescargas());
        });
    }




}





