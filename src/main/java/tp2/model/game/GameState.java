package tp2.model.game;

import tp2.model.board.Board;
import tp2.model.board.Position;
import tp2.model.board.TerrainType;
import tp2.model.player.Player;
import tp2.model.ship.Ship;
import tp2.model.ship.effect.ShipEffect;
import tp2.model.treasure.FloatingTreasure;
import tp2.model.victory.FlagshipSunkStrategy;
import tp2.model.victory.SurrenderStrategy;
import tp2.model.victory.TreasureInPortStrategy;
import tp2.model.victory.VictoryStrategy;

import java.util.List;
import java.util.ArrayList;

import static tp2.util.RandomUtils.nextInt;

/**
 * Estado central del juego. Modelo en el patrón MVC/MVP.
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
     * Crea un tesoro flotante en el juego en una posición aleatoria (generado por evento).
     *
     * @param duration duración en turnos
     */
    public Position spawnFloatingTreasure(int duration) {
        Position pos = this.board.getRandomFreePosition();
        FloatingTreasure treasure = new FloatingTreasure(pos, duration);
        this.floatingTreasures.add(treasure);
        this.board.getCellAt(pos).placeTreasure(treasure);
        return pos;
    }

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
    // Aplicación de eventos
    // -------------------------------------------------------------------------

    /**
     * Aplica un efecto a todos los barcos presentes.
     * @param effect efecto a aplicar
     */
    public void applyEffectToAllShips(ShipEffect effect){
        for(Player player : this.players) {
            for (Ship ship : player.getActiveFleet()) {
                ship.addEffect(effect);
            }
        }
    }

    /**
     * Aplica daño a todos los barcos presentes.
     * @param damage daño a aplicar
     */
    public void applyDamageToAllShips(int damage){
        for(Player player : this.getPlayers()) {
            for (Ship ship : player.getActiveFleet()) {
                ship.receiveDamage(damage);
            }
        }
    }

    /**
     * Aplica daño a los barcos en un área del tablero.
     * @param area lista de posiciones donde se aplicará el daño
     * @param damage daño a aplicar
     */
    public List<String> applyDamageToShipsInArea(List<Position> area, int damage){
        List<String> affectedShips = new ArrayList<>();
        for(Position pos : area) {
            if (!board.isWithinBounds(pos)) {
                continue;
            }

            Ship ship = board.getShipAt(pos);
            if (ship != null) {
                ship.receiveDamage(damage);
                affectedShips.add(ship.getName());
            }
        }
        return affectedShips;
    }

    /**
     * Aplica el efecto tormenta en un área central del tablero determinada por un porcentaje
     * @param areaPercent porcentaje del tablero donde se aplica la tormenta
     * @param duration duración en turnos
     */
    public List<Position> applyCentralStorm(float areaPercent, int duration){
        return this.board.applyCentralClimateEffect(areaPercent, TerrainType.STORM, duration);
    }

    public void applyEffectToCurrentPlayerFleet(ShipEffect effect){
        for(Ship ship : this.currentPlayer.getActiveFleet())
            ship.addEffect(effect.clone());
    }

    public String applyEffectToCurrentPlayerRandomShip(ShipEffect effect){
        List<Ship> fleet = this.currentPlayer.getActiveFleet();
        Ship ship = fleet.get(nextInt(fleet.size()));
        ship.addEffect(effect);
        return ship.getName();
    }

    public void applyCostIncreaseToCurrentPlayer(double costMultiplier, int duration){
        this.getCurrentPlayer().applyCostIncrease(costMultiplier, duration);
    }

    public void applyEffectToAllAgileShips(ShipEffect effect){
        for (var player : players) {
            for (var ship : player.getActiveFleet()) {
                if(ship.isAgile())
                    ship.addEffect(effect.clone());
            }
        }
    }

    public void applyEffectToAllNonAgileShips(ShipEffect effect){
        for (var player : players) {
            for (var ship : player.getActiveFleet()) {
                if(!ship.isAgile())
                    ship.addEffect(effect.clone());
            }
        }
    }

    public void updateClimateEffect(){
        this.board.updateClimateEffect();
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
    public void setCurrentPlayer(Player p) { this.currentPlayer = p; }
}
