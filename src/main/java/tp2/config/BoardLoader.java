package tp2.config;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

import tp2.model.board.*;

/**
 * Responsable únicamente de construir un Board desde un JSON.
 */
public class BoardLoader {
    private final JsonLoader jsonLoader;

    public BoardLoader(JsonLoader jsonLoader) {
        this.jsonLoader = jsonLoader;
    }

    public Board load(String mapFileName) throws IOException {
        JsonNode root = jsonLoader.readJson("maps/" + mapFileName);

        int rows = root.get("rows").asInt();
        int cols = root.get("cols").asInt();

        Board board = new Board(rows, cols);
        JsonNode cells = root.get("cells");


        // Revisar OCP aca con el if, hay que pensar una manera de abstraernos del tipo de terreno
        // Si bien justo MARINE_CURRENT es el unico con ese detalle, esta abierto a modificaciones
        for (JsonNode cellNode : cells) {
            Position pos = createPosition(cellNode);
            TerrainType terrain = TerrainType.valueOf(cellNode.get("terrain").asText());
            Cell cell = board.getCellAt(pos);
            cell.setTerrain(terrain);

            if (terrain == TerrainType.MARINE_CURRENT) {
                Direction dir = Direction.valueOf(cellNode.get("direction").asText());
                cell.setCurrentDirection(dir);
            }
        }

        return board;
    }

    private Position createPosition(JsonNode cellNode) {
        return new Position(cellNode.get("row").asInt(), cellNode.get("col").asInt());
    }
}