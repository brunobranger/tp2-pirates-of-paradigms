package tp2.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Centraliza la lectura de archivos JSON desde resources.
 */
public class JsonLoader {

    private final String resourcesBasePath;
    private final ObjectMapper mapper;

    public JsonLoader(String resourcesBasePath) {
        this.resourcesBasePath = resourcesBasePath;
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
        String fullPath = resourcesBasePath + "/" + relativePath;

        InputStream is = getClass().getClassLoader().getResourceAsStream(fullPath);

        if (is == null) {
            throw new IOException("File not found: " + fullPath);
        }

        return mapper.readTree(is);
    }
}