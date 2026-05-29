package tp2.model;

import tp2.model.board.Board;
import tp2.model.player.Player;
import tp2.model.treasure.DynasticTreasure;
import tp2.model.treasure.FloatingTreasure;

import java.util.List;
import java.util.ArrayList;

/**
 * Estado central del juego. Modelo en el patrón MVC/MVP.
 * Contiene:
 *   - El tablero (Board)
 *   - Los dos jugadores (Player)
 *   - El turno actual y quién juega
 *   - Los tesoros dinásticos
 *   - Tesoros flotantes activos (evento)
 *   - Condición de victoria detectada
 * Principio: SRP — GameState es el modelo puro. No imprime nada ni lee input.
 * El GameController lo modifica; la View lo lee para mostrarlo.
 * UML: GameState --compone--> Board, Player x2, DynasticTreasure x2.
 *      GameController --usa--> GameState (asociación).
 */
public class GameState {

    private final Board board;
    private final Player player1;
    private final Player player2;

    private Player currentPlayer;
    private final int turnNumber;

    private final DynasticTreasure treasure1;
    private final DynasticTreasure treasure2;

    private final List<FloatingTreasure> floatingTreasures;

    private VictoryCondition victoryCondition; // null si no terminó
    private Player playerWinner;                      // null si no terminó

    // Flag especial de Viento Favorable: este turno se puede mover y atacar
    private boolean canMoveAndAttackSameTurn;

    public GameState(Board board, Player player1, Player player2,
                     DynasticTreasure treasure1, DynasticTreasure treasure2) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.treasure1 = treasure1;
        this.treasure2 = treasure2;
        this.floatingTreasures = new ArrayList<>();
        this.turnNumber = 1;
        this.currentPlayer = player1; // se sobreescribirá tras sorteo inicial
    }

    // -------------------------------------------------------------------------
    // Condiciones de victoria
    // -------------------------------------------------------------------------

    /**
     * Evalúa si alguna condición de victoria se cumplió y actualiza winner.
     * Debe llamarse al finalizar cada acción que pueda cambiar el estado.
     * Condiciones (enunciado):
     *   1. El barco insignia rival fue hundido.
     *   2. Se capturó el tesoro dinástico enemigo y se llegó al propio puerto.
     *   3. El rival se rindió.
     */
    public void checkVictoryConditions() {

        // condicion de buque insignia hundido
        // -------------------------------------------------------------------------
        // usa metodos repetidos para ambos - generar Player Template (necesario)
        if (player1.isFlagshipSunk()) {
            setPlayerWinner(player1);
            setVictoryCondition(VictoryCondition.FLAGSHIP_SUNK);
        }

        if (player2.isFlagshipSunk()) {
            setPlayerWinner(player2);
            setVictoryCondition(VictoryCondition.FLAGSHIP_SUNK);
        }
        // -------------------------------------------------------------------------

        // condicion de rendicion
        // -------------------------------------------------------------------------
        // usa metodos repetidos para ambos - generar Player Template (necesario)
        if (player1.hasSurrendered()) {
            setPlayerWinner(player1);
            setVictoryCondition(VictoryCondition.SURRENDER);
        }
        if (player2.hasSurrendered()) {
            setPlayerWinner(player2);
            setVictoryCondition(VictoryCondition.SURRENDER);
        }
        // -------------------------------------------------------------------------

        // condicion de tesoro entregado en puerto propio
        // -------------------------------------------------------------------------
        if () {}
        if () {}
        // -------------------------------------------------------------------------

        // Condición de tesoro: verificar si treasure entregado en puerto propio
    }

    /** @return true si el juego terminó */
    public boolean isGameOver() {
        return victoryCondition != null;
    }

    // -------------------------------------------------------------------------
    // Gestión de turno
    // -------------------------------------------------------------------------

    /**
     * Alterna el turno al otro jugador y avanza el contador de turno
     * cuando ambos jugadores completaron el suyo.
     */
    public void switchTurn() {
        // 
        // currentPlayer = (currentPlayer == player1) ? player2 : player1;
        // if (currentPlayer == player1) turnNumber++;
        // currentPlayer.resetFleetTurnState();
        // canMoveAndAttackSameTurn = false;
    }

    /**
     * Retorna el jugador oponente al dado.
     *
     * @param player jugador de referencia
     * @return el otro jugador
     */
    public Player getOpponentOf(Player player) {
        return player == player1 ? player2 : player1;
    }

    // -------------------------------------------------------------------------
    // Tesoros flotantes
    // -------------------------------------------------------------------------

    /**
     * Agrega un tesoro flotante al estado (generado por evento).
     *
     * @param treasure tesoro a agregar
     */
    public void addFloatingTreasure(FloatingTreasure treasure) {
        floatingTreasures.add(treasure);
    }

    /**
     * Elimina tesoros flotantes vencidos (expirados o reclamados).
     * Llamado al finalizar cada turno.
     */
    public void cleanExpiredFloatingTreasures() {
        floatingTreasures.removeIf(FloatingTreasure::isGone);
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    public Player getPlayer1() { return this.player1; }
    public Player getPlayer2() { return this.player2; }
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    public DynasticTreasure getTreasure1() { return this.treasure1; }
    public DynasticTreasure getTreasure2() { return this.treasure2; }
    // -------------------------------------------------------------------------

    public Board getBoard() { return this.board; }
    public int getTurnNumber() { return this.turnNumber; }
    public Player getCurrentPlayer() { return this.currentPlayer; }
    public List<FloatingTreasure> getFloatingTreasures() { return List.copyOf(floatingTreasures); }
    public VictoryCondition getVictoryCondition() { return this.victoryCondition; }
    public Player getPlayerWinner() { return this.playerWinner; }

    // -------------------------------------------------------------------------
    // Setters
    // -------------------------------------------------------------------------

    public void setVictoryCondition(VictoryCondition victoryCondition) {
        this.victoryCondition = victoryCondition;
    }
    public void setPlayerWinner(Player playerWinner) {
        this.playerWinner = playerWinner;
    }
    public boolean canMoveAndAttackSameTurn() { return canMoveAndAttackSameTurn; }
    public void setCanMoveAndAttackSameTurn(boolean v) { this.canMoveAndAttackSameTurn = v; }
    public void setCurrentPlayer(Player p) { this.currentPlayer = p; }
}