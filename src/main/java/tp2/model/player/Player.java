package tp2.model.player;

import tp2.model.board.Position;
import tp2.model.resource.ResourcePool;
import tp2.model.resource.ResourceCosts;
import tp2.model.ship.Ship;
import tp2.model.ship.Flagship;
import tp2.model.treasure.DynasticTreasure;

import java.util.List;
import java.util.ArrayList;

/**
 * Representa a un capitán pirata (jugador).
 *
 * Responsabilidades:
 *   - Mantener su flota activa.
 *   - Gestionar su pool de recursos (oro y madera).
 *   - Conocer la posición de su puerto inicial.
 *   - Conocer su tesoro dinástico propio.
 *
 * UML: Player --agrega--> List<Ship> (agregación: los barcos existen como entidades propias).
 *      Player --compone--> ResourcePool (el pool no existe sin el jugador).
 *      Player --asocia--> DynasticTreasure (lo conoce pero no lo posee físicamente).
 */
public class Player {

    private final String name;
    private final Faction faction;
    private final List<Ship> fleet;
    private final ResourcePool resources;
    private Position homePortPosition;
    private DynasticTreasure ownTreasure;
    private boolean surrendered;

    public Player(String name, Faction faction) {
        this.name = name;
        this.faction = faction;
        this.fleet = new ArrayList<>();
        this.resources = new ResourcePool(ResourceCosts.INITIAL_GOLD, ResourceCosts.INITIAL_WOOD);
        this.surrendered = false;
    }

    // -------------------------------------------------------------------------
    // Flota
    // -------------------------------------------------------------------------

    /**
     * Agrega un barco a la flota del jugador.
     *
     * @param ship barco a agregar
     */
    public void addShip(Ship ship) {
        // ...
    }

    /**
     * Elimina un barco hundido de la flota activa.
     *
     * @param ship barco a eliminar
     */
    public void removeShip(Ship ship) {
        // ...
    }

    /**
     * @return lista inmutable de barcos activos (no hundidos)
     */
    public List<Ship> getActiveFleet() {
        // filtrar fleet por !ship.isSunk()
        return List.copyOf(fleet);
    }

    /**
     * @return el barco insignia del jugador
     * @throws IllegalStateException si no se encuentra (nunca debería ocurrir al inicio)
     */
    public Flagship getFlagship() {
        // buscar en fleet por instanceof Flagship
        return null;
    }

    /**
     * @return true si el barco insignia fue hundido
     */
    public boolean isFlagshipSunk() {
        // ...
        return false;
    }

    /**
     * Reinicia el estado de turno de ...s los barcos activos.
     * Llamado al inicio de cada turno de este jugador.
     */
    public void resetFleetTurnState() {
        // fleet.forEach(Ship::resetTurnState)
    }

    // -------------------------------------------------------------------------
    // Recursos
    // -------------------------------------------------------------------------

    public ResourcePool getResources() { return resources; }

    // -------------------------------------------------------------------------
    // Estado de partida
    // -------------------------------------------------------------------------

    /**
     * El jugador se rinde. Condición de derrota voluntaria.
     */
    public void surrender() {
        this.surrendered = true;
    }

    public boolean hasSurrendered() { return surrendered; }

    // -------------------------------------------------------------------------
    // Getters / setters
    // -------------------------------------------------------------------------

    public String getName() { return name; }
    public Faction getFaction() { return faction; }
    public Position getHomePortPosition() { return homePortPosition; }
    public void setHomePortPosition(Position pos) { this.homePortPosition = pos; }
    public DynasticTreasure getOwnTreasure() { return ownTreasure; }
    public void setOwnTreasure(DynasticTreasure treasure) { this.ownTreasure = treasure; }

    @Override
    public String toString() { return name + " (" + faction + ")"; }
}