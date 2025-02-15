package com.svillegas.literalura.repository;

import com.svillegas.literalura.model.Autor;
import com.svillegas.literalura.model.Libro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LibroService {
    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    public void guardarLibro(Libro libro) {
        List<Autor> autoresProcesados = new ArrayList<>();

        for (Autor autor : libro.getAutores()) {
            Optional<Autor> autorExistente = autorRepository.findByNombre(autor.getNombre());
            if (autorExistente.isPresent()) {
                autoresProcesados.add(autorExistente.get());
            } else {
                Autor nuevoAutor = autorRepository.save(autor);
                autoresProcesados.add(nuevoAutor);
            }
        }

        libro.setAutores(autoresProcesados);
        libroRepository.save(libro);
    }
}
