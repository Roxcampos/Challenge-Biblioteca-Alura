package com.alura.biblioteca.servicios;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
