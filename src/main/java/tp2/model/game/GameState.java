package tp2.model.game;

import tp2.model.board.Board;
import tp2.model.player.Player;
import tp2.model.treasure.FloatingTreasure;
import tp2.model.victory.FlagshipSunkStrategy;
import tp2.model.victory.SurrenderStrategy;
import tp2.model.victory.TreasureInPortStrategy;
import tp2.model.victory.VictoryStrategy;

import java.util.List;
import java.util.ArrayList;

/**
 * Estado central del juego. Modelo en el patrón MVC/MVP.
 * Contiene:
 *   - El tablero (Board)
 *   - El turno actual y quién juega
 *   - Tesoros flotantes activos (evento)
 *   - Condición de victoria detectada
 * Principio: SRP — GameState es el modelo puro. No imprime nada ni lee input.
 * El GameController lo modifica; la View lo lee para mostrarlo.
 */
public class GameState {
    private final List<Player> players;
    private final List<FloatingTreasure> floatingTreasures;
    private final List<VictoryStrategy> victoryStrategies;
    private final Board board;
    private Player currentPlayer;
    private int turnNumber;
    private VictoryCondition victoryCondition;
    private Player playerWinner;
    private boolean canMoveAndAttackSameTurn;


    public GameState(Board board, List<Player> players) {
        this.board = board;
        this.players = new ArrayList<>(players);
        this.currentPlayer = players.isEmpty() ? null : players.getFirst();
        this.floatingTreasures = new ArrayList<>();
        this.turnNumber = 1;
        this.victoryCondition = null;
        this.playerWinner = null;
        this.victoryStrategies = List.of(
          new FlagshipSunkStrategy(),
          new SurrenderStrategy(),
          new TreasureInPortStrategy()
        );
    }

    // -------------------------------------------------------------------------
    // Condiciones de victoria
    // -------------------------------------------------------------------------

    /**
     * Evalúa si alguna condición de victoria se cumplió y actualiza winner.
     * Debe llamarse al finalizar cada acción que pueda cambiar el estado.
     */
    public void checkVictoryConditions() {
        if (isGameOver()) return;

        for (VictoryStrategy strategy : victoryStrategies) {
            Player winner = strategy.check(this);
            if (winner != null) {
                setPlayerWinner(winner);
                setVictoryCondition(strategy.getConditionType());
                break;
            }
        }
    }

    /** @return true si ya hay un jugado asentado */
    public boolean isGameOver() {
        return playerWinner != null;
    }

    // -------------------------------------------------------------------------
    // Gestión de turno
    // -------------------------------------------------------------------------

    /**
     * Alterna el turno al otro jugador y avanza el contador de turno
     * cuando ambos jugadores completaron el suyo.
     */
    public void switchTurn() {
        if(isGameOver()) return;

        List<Player> playerList = getPlayers();

        int nextIndex = (playerList.indexOf(this.currentPlayer) + 1) % playerList.size();
        setCurrentPlayer(playerList.get(nextIndex));

        if (nextIndex == 0) this.turnNumber++;

        this.currentPlayer.resetFleetTurnState();
        setCanMoveAndAttackSameTurn(false);
    }

    /**
     * Retorna el jugador oponente al dado.
     *
     * @param player jugador de referencia
     * @return el otro jugador
     */
    public Player getOpponentOf(Player player) {
        return players.stream()
                .filter(p -> !p.equals(player))
                .findFirst()
                .orElse(null);
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
        for (FloatingTreasure treasure : floatingTreasures) {
            treasure.tick();
        }
        floatingTreasures.removeIf(FloatingTreasure::isGone);
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public List<Player> getPlayers() { return List.copyOf(players); }
    public Board getBoard() { return this.board; }
    public int getTurnNumber() { return this.turnNumber; }
    public Player getCurrentPlayer() { return this.currentPlayer; }
    public List<FloatingTreasure> getFloatingTreasures() { return List.copyOf(floatingTreasures); }
    public VictoryCondition getVictoryCondition() { return this.victoryCondition; }
    public Player getPlayerWinner() { return this.playerWinner; }

    // -------------------------------------------------------------------------
    // Setters
    // -------------------------------------------------------------------------

    public void setVictoryCondition(VictoryCondition condition) { this.victoryCondition = condition; }
    public void setPlayerWinner(Player player) { this.playerWinner = player; }
    public boolean canMoveAndAttackSameTurn() { return canMoveAndAttackSameTurn; }
    public void setCanMoveAndAttackSameTurn(boolean v) { this.canMoveAndAttackSameTurn = v; }
    public void setCurrentPlayer(Player p) { this.currentPlayer = p; }
}