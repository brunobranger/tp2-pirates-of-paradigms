package tp2.model.board;

import tp2.model.ship.Ship;
import tp2.model.player.Player;
import tp2.model.treasure.Treasure;

/**
 * Representa una casilla del tablero.
 */
public class Cell {

    private final Position position;
    private TerrainType baseTerrain;
    private TerrainType currentTerrain;
    private int climateEffectDuration = 0;
    private TerrainType climateOverride = null;
    private Ship occupant;
    private Treasure treasure;
    private Player portOwner;
    private Direction currentDirection;

    public Cell(Position position, TerrainType terrain) {
        this.position = position;
        this.baseTerrain = terrain;
        this.currentTerrain = terrain;
    }

    // -------------------------------------------------------------------------
    // Consultas de estado
    // -------------------------------------------------------------------------

    /**
     * @return true si la casilla puede ser ocupada por un barco (no es isla, no está ya ocupada).
     */
    public boolean isNavigable() {
        return this.baseTerrain != TerrainType.ISLAND && this.occupant == null;
    }

    /**
     * Maneja lo que no puede al iniciar el juego
     * donde se requiere colocar barcos y puertos
     * de forma inicial.
     * @return true en cado de poder colocar barco/puerto
     */
    public boolean canPlaceStructure() {
        return this.getTerrain() != TerrainType.ISLAND
                && this.getTerrain() != TerrainType.PORT
                && this.treasure == null
                && this.occupant == null;
    }

    /**
     * @return true si hay un barco en esta casilla
     */
    public boolean isOccupied() {
        return occupant != null;
    }

    /**
     * @return true si es un puerto sin dueño (puede comprarse)
     */
    public boolean isUnownedPort() {
        return baseTerrain == TerrainType.PORT && portOwner == null;
    }

    /**
     * @param player jugador que consulta
     * @return true si es un puerto propio del jugador dado
     */
    public boolean isOwnedPortOf(Player player) {
        return baseTerrain == TerrainType.PORT && portOwner != null && portOwner.equals(player);
    }

    // -------------------------------------------------------------------------
    // Modificaciones de estado
    // -------------------------------------------------------------------------

    /**
     * Coloca un barco en la casilla. Lanza excepción si ya está ocupada.
     *
     * @param ship barco a colocar
     * @throws IllegalStateException si la casilla ya está ocupada
     */
    public void placeShip(Ship ship) {
        if (this.isOccupied()) throw new IllegalStateException("The cell is already occupied.");
        if (this.baseTerrain == TerrainType.ISLAND) throw new IllegalStateException("Cannot place a ship on an island terrain.");
        this.occupant = ship;
        ship.setPosition(this.position);
    }

    /**
     * Elimina el barco de la casilla (por muerte o movimiento).
     */
    public void removeShip() {
        this.occupant = null;
    }

    /**
     * Asigna el dueño del puerto a un jugador.
     *
     * @param player jugador comprador
     * @throws IllegalStateException si no es una casilla de tipo PORT
     */
    public void assignPortOwner(Player player) {
        if (baseTerrain != TerrainType.PORT) throw new IllegalStateException("The terrain is not a PORT");
        this.portOwner = player;
    }

    /**
     * Coloca un tesoro en la casilla (al hundirse un barco que lo transportaba).
     *
     * @param treasure tesoro a dejar flotando
     */
    public void placeTreasure(Treasure treasure) {
        if (treasure == null) {
            throw new IllegalArgumentException("No se puede colocar un tesoro nulo en la casilla.");
        }

        if (this.treasure != null) {
            throw new IllegalStateException("Esta casilla ya contiene un tesoro flotando.");
        }
        this.treasure = treasure;
    }

    /**
     * Remueve y retorna el tesoro flotante (cuando un barco lo recoge).
     *
     * @return tesoro flotante, o null si no había ninguno
     */
    public Treasure pickUpTreasure() {
        if (this.treasure == null) {
            throw new IllegalStateException("No hay ningún tesoro en esta casilla para ser recogido.");
        }

        Treasure targetTreasure = this.treasure;
        this.treasure = null;
        return targetTreasure;
    }

    /**
     * Cambia el tipo de terreno (usado por eventos como Vendaval que convierte
     * casillas en tormenta temporalmente).
     *
     * @param newTerrain nuevo tipo de terreno
     */
    public void setTerrain(TerrainType newTerrain) {
        this.baseTerrain = newTerrain;
        this.currentTerrain = newTerrain;
    }

    public void applyTemporalClimate(TerrainType climate, int duration) {
        if(climate != TerrainType.STORM && climate != TerrainType.FOG && climate != TerrainType.MARINE_CURRENT)
            throw new IllegalArgumentException("El tipo de terreno recibido no es un clima");
        this.climateOverride = climate;
        this.climateEffectDuration = duration;
        this.currentTerrain = climate;
    }

    /**
     * Se debe llamar al pasar el turno. Si el efecto expira, vuelve al estado base.
     */
    public void decrementClimateDuration() {
        if (this.climateOverride != null && climateEffectDuration > 0) {
            climateEffectDuration--;
            if (climateEffectDuration == 0) {
                this.climateOverride = null;
                this.currentTerrain = this.baseTerrain;
            }
        }
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public Position getPosition() { return position; }
    public TerrainType getTerrain() { return currentTerrain; }
    public Ship getOccupant() { return occupant; }
    public Treasure getTreasure() { return treasure; }
    public Player getPortOwner() { return portOwner; }
    public Direction getCurrentDirection() { return currentDirection; }

    public void setCurrentDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    }
}
