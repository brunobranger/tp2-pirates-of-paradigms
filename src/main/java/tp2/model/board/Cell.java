package tp2.model.board;

import tp2.model.ship.Ship;
import tp2.model.treasure.DynasticTreasure;
import tp2.model.player.Player;

/**
 * Representa una casilla del tablero.
 *
 * Una celda conoce:
 *   - su tipo de terreno (TerrainType)
 *   - el barco que la ocupa (si hay uno)
 *   - el tesoro flotante que contiene (si hay uno)
 *   - el jugador dueño (si es un puerto comprado)
 *   - la dirección de corriente marina (solo si es MARINE_CURRENT)
 *
 * Principio: SRP — la celda sabe sobre sí misma, no sobre reglas de juego globales.
 * Principio: PoLK (Ley de Demeter) — evitar acceder a objetos anidados desde fuera.
 */
public class Cell {

    private final Position position;
    private TerrainType terrain;
    private Ship occupant;           // null si no hay barco
    private DynasticTreasure floatingTreasure; // null si no hay tesoro flotando
    private Player portOwner;        // null si no es puerto o no tiene dueño
    private Direction currentDirection; // solo relevante si terrain == MARINE_CURRENT

    public Cell(Position position, TerrainType terrain) {
        this.position = position;
        this.terrain = terrain;
    }

    // -------------------------------------------------------------------------
    // Consultas de estado
    // -------------------------------------------------------------------------

    /**
     * @return true si la casilla puede ser ocupada por un barco
     *         (no es isla, no está ya ocupada).
     */
    public boolean isNavigable() {
        // terrain != ISLAND && occupant == null
        return false;
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
        return terrain == TerrainType.PORT && portOwner == null;
    }

    /**
     * @param player jugador que consulta
     * @return true si es un puerto propio del jugador dado
     */
    public boolean isOwnedPortOf(Player player) {
        return terrain == TerrainType.PORT && portOwner != null && portOwner.equals(player);
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
        // ...
    }

    /**
     * Elimina el barco de la casilla (por muerte o movimiento).
     */
    public void removeShip() {
        // ...
    }

    /**
     * Asigna el dueño del puerto a un jugador.
     *
     * @param player jugador comprador
     * @throws IllegalStateException si no es una casilla de tipo PORT
     */
    public void assignPortOwner(Player player) {
        // ...
    }

    /**
     * Coloca un tesoro flotante en la casilla (al hundirse un barco que lo transportaba).
     *
     * @param treasure tesoro a dejar flotando
     */
    public void placeFloatingTreasure(DynasticTreasure treasure) {
        // ...
    }

    /**
     * Remueve y retorna el tesoro flotante (cuando un barco lo recoge).
     *
     * @return tesoro flotante, o null si no había ninguno
     */
    public DynasticTreasure pickUpTreasure() {
        // ...
        return null;
    }

    /**
     * Cambia el tipo de terreno (usado por eventos como Vendaval que convierte
     * casillas en tormenta temporalmente).
     *
     * @param newTerrain nuevo tipo de terreno
     */
    public void setTerrain(TerrainType newTerrain) {
        this.terrain = newTerrain;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public Position getPosition() { return position; }
    public TerrainType getTerrain() { return terrain; }
    public Ship getOccupant() { return occupant; }
    public DynasticTreasure getFloatingTreasure() { return floatingTreasure; }
    public Player getPortOwner() { return portOwner; }
    public Direction getCurrentDirection() { return currentDirection; }

    public void setCurrentDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    }
}