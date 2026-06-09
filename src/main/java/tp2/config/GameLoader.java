package tp2.config;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import tp2.model.board.Board;
import tp2.model.game.GameState;
import tp2.model.player.Faction;
import tp2.model.player.Player;

/**
 * Orquestador de carga del juego.
 * Coordina los distintos loaders especializados sin construir el dominio directamente.
 */
public class GameLoader {
    private final BoardLoader boardLoader;
    private final FleetLoader fleetLoader;
    private final GameInitializer gameInitializer;

    public GameLoader() {
        JsonLoader jsonLoader = new JsonLoader();

        this.boardLoader = new BoardLoader(jsonLoader);
        this.fleetLoader = new FleetLoader(jsonLoader);
        this.gameInitializer = new GameInitializer();
    }

    public GameState loadGame(String mapFileName, String fleetFileName) throws IOException {
        Board board = boardLoader.load(mapFileName);
        Player player1 = fleetLoader.load(fleetFileName, Faction.CRIMSON_BROTHERHOOD);
        Player player2 = fleetLoader.load(fleetFileName, Faction.ABYSS_CROWN);

        return gameInitializer.initializeGame(board, player1, player2);
    }

    public List<String> getAvailableMaps() {
        return listFiles("maps");
    }

    public List<String> getAvailableFleets() {
        return listFiles("fleets");
    }

    private List<String> listFiles(String folder) {
        Path path = getFolderPath(folder);

        try (var files = Files.list(path)) {
            return files.map(file -> file.getFileName().toString()).toList();
        } catch (IOException error) {
            throw new IllegalStateException("Error listing files of directory: " + folder, error);
        }
    }

    private Path getFolderPath(String folder) {
        URL resource = getResource(folder);

        try {
            return Paths.get(resource.toURI());
        } catch (URISyntaxException error) {
            throw new IllegalStateException("Route invalid for: " + folder, error);
        }
    }

    private URL getResource(String folder) {
        String fullPath = "/" + folder;
        URL resource = getClass().getClassLoader().getResource(fullPath);
        if (resource == null) throw new IllegalStateException("No existe el recurso: " + fullPath);

        return resource;
    }
}