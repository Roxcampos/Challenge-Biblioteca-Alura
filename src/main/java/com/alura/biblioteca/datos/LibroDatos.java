package com.alura.biblioteca.datos;

import com.alura.biblioteca.entidades.Autor;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LibroDatos(
        @JsonAlias("title") String titulo,
        @JsonAlias("name") Autor autores,
        @JsonAlias("languages") List<String> idiomas,
        @JsonAlias("download_count") Double descargas){
}


