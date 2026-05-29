package tp2.view;

import tp2.model.game.GameState;
import tp2.model.board.Cell;
import tp2.model.board.TerrainType;

/**
 * Renderizador ASCII del tablero.
 * Separado de ConsoleView para respetar SRP: ConsoleView orquesta el flujo,
 * BoardRenderer sabe cómo dibujar la grilla.
 *
 * Leyenda del enunciado:
 *   ~~  → mar abierto
 *   T   → tormenta
 *   P   → puerto
 *   N   → niebla
 *   A   → arrecife
 *   I   → isla
 *   MC  → corriente marina
 *   B1  → barco jugador 1
 *   B2  → barco jugador 2
 *   TR  → tesoro dinástico
 *   FT  → tesoro flotante
 *   KR  → Kraken
 */
public class BoardRenderer {

    /**
     * Genera el string completo del tablero con leyenda para imprimir por consola.
     *
     * @param gameState estado del juego
     * @return string multilínea con el tablero y la leyenda
     */
    public String render(GameState gameState) {
        //
        // StringBuilder sb = new StringBuilder()
        // Recorrer board.getRows() y board.getCols()
        // Para cada celda: getCellSymbol(cell, gameState)
        // Agregar leyenda al costado
        return "";
    }

    /**
     * Retorna el símbolo de 2 caracteres para una celda dada.
     *
     * @param cell      celda a representar
     * @param gameState estado del juego (para saber qué jugador ocupa qué)
     * @return símbolo de la celda
     */
    private String getCellSymbol(Cell cell, GameState gameState) {
        // prioridad: barco > tesoro > terreno especial > mar abierto
        return "~~";
    }

    /**
     * Retorna el símbolo del tipo de terreno.
     *
     * @param terrain tipo de terreno
     * @return símbolo correspondiente
     */
    private String getTerrainSymbol(TerrainType terrain) {
        return switch (terrain) {
            case OPEN_SEA       -> "~~";
            case STORM          -> "T ";
            case PORT           -> "P ";
            case FOG            -> "N ";
            case REEF           -> "A ";
            case MARINE_CURRENT -> "MC";
            case ISLAND         -> "I ";
        };
    }
}