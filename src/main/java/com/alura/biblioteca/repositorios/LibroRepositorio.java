package com.alura.biblioteca.repositorios;

import com.alura.biblioteca.entidades.Autor;
import com.alura.biblioteca.entidades.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, Long> {

    List<Libro> findAll();

    @Query("SELECT DISTINCT l.autor FROM Libro l")
    List<Autor> findAllAutores();

    @Query("SELECT DISTINCT a FROM Autor a WHERE a.nacimiento <= :anio AND (a.deceso >= :anio OR a.deceso IS NULL)")
    List<Autor> findAutoresVivos(int anio);

    List<Libro> findByIdioma(String idioma);

    @Query("SELECT l FROM Libro l ORDER BY l.descargas DESC LIMIT 10")
    List<Libro> findByDescargas();

}
