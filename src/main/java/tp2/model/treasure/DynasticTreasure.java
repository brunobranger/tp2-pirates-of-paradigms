package tp2.model.treasure;

import tp2.model.board.Position;
import tp2.model.player.Player;
import tp2.model.ship.Ship;

/**
 * Tesoro dinástico: ítem único por jugador cuya captura y transporte al puerto
 * propio es una de las condiciones de victoria.
 *
 * Estado posible:
 *   - En el mapa (posición conocida, sin barco transportándolo).
 *   - Transportado por un barco (se mueve con él).
 *   - Flotando (el barco que lo llevaba fue hundido).
 *
 * UML: DynasticTreasure --pertenece a--> Player (asociación).
 *      Ship --transporta--> DynasticTreasure (asociación opcional).
 *
 * Principio: SRP — la condición de victoria no vive acá, sino en GameState.
 */
public class DynasticTreasure {

    private final Player owner;
    private Position position;       // posición actual en el mapa (null si está en barco)
    private Ship carrier;            // barco que lo transporta (null si está en el mapa)
    private boolean isFloating;      // true si el barco que lo cargaba fue hundido

    public DynasticTreasure(Player owner, Position initialPosition) {
        this.owner = owner;
        this.position = initialPosition;
        this.carrier = null;
        this.isFloating = false;
    }

    // -------------------------------------------------------------------------
    // Estado
    // -------------------------------------------------------------------------

    /** @return true si está siendo transportado por algún barco */
    public boolean isCarried() { return carrier != null; }

    /** @return true si está flotando (barco portador hundido) */
    public boolean isFloating() { return isFloating; }

    /** @return true si está en el mapa sin barco que lo lleve */
    public boolean isOnMap() { return carrier == null; }

    // -------------------------------------------------------------------------
    // Acciones
    // -------------------------------------------------------------------------

    /**
     * Un barco recoge el tesoro del mapa.
     *
     * @param ship barco que lo recoge
     */
    public void pickUp(Ship ship) {
        // this.carrier = ship; this.position = null; this.isFloating = false;
    }

    /**
     * El barco portador fue hundido: el tesoro queda flotando en la posición del barco.
     *
     * @param sinkPosition posición donde se hundió el barco
     */
    public void drop(Position sinkPosition) {
        // this.carrier = null; this.position = sinkPosition; this.isFloating = true;
    }

    /**
     * El jugador entrega el tesoro en su propio puerto (victoria).
     * Llama a drop en la posición del puerto; la victoria la verifica GameState.
     *
     * @param portPosition posición del puerto
     */
    public void deliver(Position portPosition) {
        // ...
    }

    /**
     * Actualiza la posición del tesoro para que siga al barco portador.
     * Llamado por Board.moveShip() si el barco lleva el tesoro.
     *
     * @param newPosition nueva posición del barco
     */
    public void updatePositionWithCarrier(Position newPosition) {
        // ...
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public Player getOwner() { return owner; }
    public Position getPosition() { return position; }
    public Ship getCarrier() { return carrier; }
}