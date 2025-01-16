package com.svillegas.literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String idiomas;
    private Double descargas;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "libros_autores",
            joinColumns = @JoinColumn(name = "libros_id"),
            inverseJoinColumns = @JoinColumn(name = "autores_id")
    )
    private List<Autor> autores;

    public Libro(String titulo, List<Autor> autores, List<String> idiomas, Double descargas) {
        this.titulo = titulo;
        this.autores = autores;
        this.idiomas = String.join(",", idiomas);
        this.descargas = descargas;
    }

    public Libro() {
    }

    /*
    public Libro(DatosLibros datosLibros) {
        this.titulo = datosLibros.titulo();
        this.idiomas = datosLibros.idiomas();
        this.numeroDeDescargas = datosLibros.numeroDeDescargas();
        this.autores = new ArrayList<>();
    }
    */

    @Override
    public String toString() {
        StringBuilder autoresNombres = new StringBuilder();
        for (Autor autor : autores) {
            if (autoresNombres.length() > 0) {
                autoresNombres.append(", ");
            }
            autoresNombres.append(autor.getNombre());
        }

        return "Título: " + titulo + "\n" +
                "Autores: " + autoresNombres + "\n" +
                "Idiomas: " + String.join(", ", idiomas) + "\n" +
                "Descargas: " + descargas + "\n";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

    public Double getDescargas() {
        return descargas;
    }

    public void setDescargas(Double descargas) {
        this.descargas = descargas;
    }
}
