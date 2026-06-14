package tp2.view;

import tp2.model.board.Board;
import tp2.model.board.Cell;
import tp2.model.board.Position;
import tp2.model.board.TerrainType;
import tp2.model.game.GameState;
import tp2.model.player.Player;

import java.util.Set;

/**
 * Renderizador ASCII del tablero.
 * Responsabilidad: transformar el estado actual del tablero en una
 * representacion textual para consola, sin leer input ni modificar el modelo.
 */
public class BoardRenderer {

    public String render(GameState gameState) {
        return this.render(gameState, null);
    }

    /**
     * Genera la grilla completa con coordenadas y leyenda.
     *
     * @param gameState estado actual de la partida
     * @return representacion multilínea lista para imprimir
     */
    public String render(GameState gameState, Set<Position> validPositions) {
        Board board = gameState.getBoard();
        StringBuilder result = new StringBuilder();

        result.append("    ");
        for (int col = 0; col < board.getCols(); col++) {
            result.append(String.format("%-3d", col));
        }
        result.append(System.lineSeparator());

        for (int row = 0; row < board.getRows(); row++) {
            result.append(String.format("%-3d ", row));
            for (int col = 0; col < board.getCols(); col++) {
                Position currentPos = new Position(row, col);
                Cell cell = board.getCellAt(currentPos);
                String symbol = getCellSymbol(cell, gameState);

                if (validPositions != null && validPositions.contains(currentPos)) {
                    if (symbol.equals("~~")) {
                        symbol = " • ";
                    }
                }
                result.append(String.format("%-3s", symbol));
            }
            result.append(System.lineSeparator());
        }

        result.append(System.lineSeparator())
                .append("Leyenda: ~~ mar | B1/B2 barco | T tormenta | P puerto | ")
                .append("N niebla | A arrecife | MC corriente | I isla | ")
                .append("TR tesoro dinastico | FT tesoro flotante")
                .append(System.lineSeparator());
        return result.toString();
    }

    /**
     * Obtiene el simbolo visible de una celda.
     * La prioridad visual es: barco, puerto, tesoro y terreno.
     */
    private String getCellSymbol(Cell cell, GameState gameState) {
        if (cell.isOccupied()) {
            return getShipSymbol(cell, gameState);
        }

        if (cell.getTerrain() == TerrainType.PORT) {
            Player ownerPort = cell.getPortOwner();

            if (ownerPort == null) {
                return "P";
            }

            int playerIndex = gameState.getPlayers().indexOf(ownerPort);
            if (playerIndex != -1) {
                return "P" + (playerIndex + 1);
            }
        }
        if (hasDynasticTreasure(cell.getPosition(), gameState)) {
            return "TR";
        }

        if (hasFloatingTreasure(cell.getPosition(), gameState)) {
            return "FT";
        }

        return getTerrainSymbol(cell.getTerrain());
    }

    /**
     * Identifica a que jugador pertenece el barco para representarlo como B1,
     * B2, etc. B? se utiliza como respaldo ante un estado inconsistente.
     */
    private String getShipSymbol(Cell cell, GameState gameState) {
        for (int index = 0; index < gameState.getPlayers().size(); index++) {
            Player player = gameState.getPlayers().get(index);
            if (player.hasShip(cell.getOccupant())) {
                return "B" + (index + 1);
            }
        }
        return "B?";
    }

    /** @return true si existe un tesoro dinastico visible en la posicion */
    private boolean hasDynasticTreasure(Position position, GameState gameState) {
        return gameState.getPlayers().stream()
                .map(Player::getOwnTreasure)
                .filter(treasure -> treasure != null && treasure.isOnMap())
                .anyMatch(treasure -> position.equals(treasure.getPositionCarrierTreasure()));
    }

    /** @return true si existe un tesoro flotante activo en la posicion */
    private boolean hasFloatingTreasure(Position position, GameState gameState) {
        return gameState.getFloatingTreasures().stream()
                .filter(treasure -> !treasure.isGone())
                .anyMatch(treasure -> position.equals(treasure.getPosition()));
    }

    /**
     * Traduce los tipos de terreno del modelo a simbolos compactos de consola.
     */
    private String getTerrainSymbol(TerrainType terrain) {
        return switch (terrain) {
            case OPEN_SEA       -> "~~";
            case STORM          -> "T";
            case PORT           -> "P";
            case FOG            -> "N";
            case REEF           -> "A";
            case MARINE_CURRENT -> "MC";
            case ISLAND         -> "I";
            case KRAKEN         -> "K";
            case KRAKEN_BLOCKED_CELL -> "X";
        };
    }
}
