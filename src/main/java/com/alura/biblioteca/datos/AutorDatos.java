package com.alura.biblioteca.datos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AutorDatos(
        @JsonAlias("name") String nombre,
        @JsonAlias("birth_year") int nacimiento,
        @JsonAlias("death_year") int fallecimiento) {
}
