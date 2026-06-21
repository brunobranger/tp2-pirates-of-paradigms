// BoardRenderer.java
package tp2.view;

import tp2.model.board.Board;
import tp2.model.board.Cell;
import tp2.model.board.Position;
import tp2.model.board.TerrainType;
import tp2.model.game.GameState;
import tp2.model.player.Player;
import tp2.model.ship.ShipType;


import java.util.Set;

/**
 * Renderizador ASCII del tablero.
 * Responsabilidad: transformar el estado actual del tablero en una
 * representacion textual para consola, sin leer input ni modificar el modelo.
 *
 * El coloreado se aplica por categoria (flota, peligro, tesoro, resaltado)
 * para mantener la lectura clara sin saturar la salida.
 */
public class BoardRenderer {

    public String render(GameState gameState) {
        return this.render(gameState, null);
    }

    public String render(GameState gameState, Set<Position> validPositions) {
        return render(gameState, validPositions, null);
    }

    /**
     * Genera la grilla completa con coordenadas y leyenda.
     *
     * @param gameState       estado actual de la partida
     * @param validPositions  posiciones a marcar como válidas (setup/movimiento), o null
     * @param highlightedPosition posición a resaltar como última acción (ej. destino de un movimiento), o null
     * @return representacion multilínea lista para imprimir
     */
    public String render(GameState gameState, Set<Position> validPositions, Position highlightedPosition) {
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

                if (currentPos.equals(highlightedPosition)) {
                    symbol = wrapHighlight(symbol);
                }

                result.append(padColored(symbol));
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
     * Obtiene el simbolo visible de una celda, ya coloreado segun su categoria.
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
                String symbol = "P" + (playerIndex + 1);
                return AnsiColors.colorize(symbol, getPlayerColor(playerIndex));
            }
        }
        if (hasDynasticTreasure(cell.getPosition(), gameState)) {
            return AnsiColors.colorize("TR", AnsiColors.TREASURE);
        }

        if (hasFloatingTreasure(cell.getPosition(), gameState)) {
            return AnsiColors.colorize("FT", AnsiColors.TREASURE);
        }

        return getTerrainSymbol(cell.getTerrain());
    }

    /** @return color asociado a la flota segun su indice (0 -> jugador 1, 1 -> jugador 2) */
    private String getPlayerColor(int playerIndex) {
        return playerIndex == 0 ? AnsiColors.PLAYER_ONE : AnsiColors.PLAYER_TWO;
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
     * Los terrenos peligrosos o bloqueantes se resaltan en color de alerta.
     */
    private String getTerrainSymbol(TerrainType terrain) {
        return switch (terrain) {
            case OPEN_SEA       -> "~~";
            case STORM          -> AnsiColors.colorize("T", AnsiColors.DANGER);
            case PORT           -> "P";
            case FOG            -> "N";
            case REEF           -> AnsiColors.colorize("A", AnsiColors.DANGER);
            case MARINE_CURRENT -> "MC";
            case ISLAND         -> AnsiColors.colorize("I", AnsiColors.DANGER);
            case KRAKEN         -> AnsiColors.colorize("K", AnsiColors.DANGER);
            case KRAKEN_BLOCKED_CELL -> AnsiColors.colorize("X", AnsiColors.DANGER);
        };
    }

    /**
     * Identifica a que jugador pertenece el barco para representarlo como B1,
     * B2, etc., coloreado segun la flota. B? se utiliza como respaldo ante
     * un estado inconsistente y queda sin color.
     */
    private String getShipSymbol(Cell cell, GameState gameState) {
        for (int index = 0; index < gameState.getPlayers().size(); index++) {
            Player player = gameState.getPlayers().get(index);
            if (player.hasShip(cell.getOccupant())) {
                String symbol = getShipTypeAbbreviation(cell.getOccupant().getType());
                return AnsiColors.colorize(symbol, getPlayerColor(index));
            }
        }
        return "B?";
    }


    /**
     * Traduce el tipo de barco a una abreviatura de 2 caracteres para el
     * tablero. Centralizado aca para que agregar un tipo nuevo sea una sola
     * linea (OCP).
     */
    private String getShipTypeAbbreviation(ShipType type) {
        return switch (type) {
            case FLAGSHIP -> "FL";
            case FRIGATE  -> "FR";
            case GALLEON  -> "GA";
            case SLOOP    -> "SL";
        };
    }

    /**
     * Envuelve un simbolo ya coloreado (o sin colorear) con el resaltado de
     * ultima accion, preservando legibilidad si ya tenia color propio.
     */
    private String wrapHighlight(String symbol) {
        String visibleText = stripAnsi(symbol);
        return AnsiColors.colorize(visibleText, AnsiColors.HIGHLIGHT);
    }

    /**
     * Aplica padding contando solo el texto visible, sin importar si el
     * simbolo tiene codigos ANSI o no. Reemplaza el uso de
     * String.format("%-3s", ...), que cuenta mal los caracteres invisibles.
     */
    private String padColored(String symbol) {
        String visibleText = stripAnsi(symbol);
        int padding = Math.max(0, 3 - visibleText.length());
        return symbol + " ".repeat(padding);
    }

    private String stripAnsi(String text) {
        return text.replaceAll("\u001B\\[\\d+m", "");
    }
}