package tp2.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Centraliza la lectura de archivos JSON desde resources.
 */
public class JsonLoader {
    private final ObjectMapper mapper;

    public JsonLoader() {
        this.mapper = new ObjectMapper();
    }

    /**
     * Lee un archivo JSON desde resources.
     *
     * @param relativePath ruta relativa dentro de resources
     * @return nodo raíz del JSON
     * @throws IOException si el archivo no existe o no puede leerse
     */
    public JsonNode readJson(String relativePath) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(relativePath);

        if (is == null) {
            throw new IOException("File not found: " + relativePath);
        }

        return mapper.readTree(is);
    }
}