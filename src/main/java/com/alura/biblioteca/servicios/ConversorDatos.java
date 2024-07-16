package com.alura.biblioteca.servicios;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConversorDatos implements IConvierteDatos {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);

            JsonNode resultsArray = rootNode.get("results");

            if (resultsArray != null && resultsArray.size() > 0) {
                JsonNode firstResult = resultsArray.get(0);

                return objectMapper.treeToValue(firstResult, clase);
            } else {
                throw new RuntimeException("No se encontraron resultados.");
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
