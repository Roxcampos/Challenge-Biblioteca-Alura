package com.alura.biblioteca.repositorios;

import com.alura.biblioteca.entidades.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepositorio extends JpaRepository<Autor,Long> {
}
