package tp2.config;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

import tp2.model.board.Board;
import tp2.model.board.Position;

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

        if (cells != null) {
            for (JsonNode cellNode : cells) {
                board.configureCellAt(createPosition(cellNode), cellNode);
            }
        }

        return board;
    }

    private Position createPosition(JsonNode cellNode) {
        return new Position(cellNode.get("row").asInt(), cellNode.get("col").asInt());
    }
}