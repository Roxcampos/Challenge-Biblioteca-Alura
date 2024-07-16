package com.alura.biblioteca;

import com.alura.biblioteca.principal.Principal;
import com.alura.biblioteca.repositorios.AutorRepositorio;
import com.alura.biblioteca.repositorios.LibroRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibliotecaApplication implements CommandLineRunner {

	@Autowired
	private LibroRepositorio libroRepositorio;

	@Autowired
	private AutorRepositorio autorRepositorio;

	public static void main(String[] args) {
		SpringApplication.run(BibliotecaApplication.class, args);

	}
	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(autorRepositorio, libroRepositorio);
		principal.muestraElMenu();

	}

}
