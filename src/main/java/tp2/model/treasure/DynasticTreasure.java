package tp2.model.treasure;

import tp2.model.board.Position;
import tp2.model.player.Player;
import tp2.model.ship.Ship;

/**
 * Tesoro dinástico: ítem único por jugador cuya captura y transporte al puerto
 * propio es una de las condiciones de victoria.
 * Principio: SRP — la condición de victoria no vive acá, sino en GameState.
 */
public class DynasticTreasure implements Treasure{

    private final Player owner;
    private Position positionCarrierTreasure;
    private Ship carrierTreasure;
    private boolean isFloating;

    public DynasticTreasure(Player owner, Position initialPosition) {
        this.owner = owner;
        this.positionCarrierTreasure = initialPosition;
        this.carrierTreasure = null;
        this.isFloating = false;
    }

    // -------------------------------------------------------------------------
    // Interfaz Treasure
    // -------------------------------------------------------------------------

    public String getType(){
        return "DYNASTIC";
    }

    public void onPickUp(Ship ship, Player player){
        ship.pickUpTreasure(this);
    }

    // -------------------------------------------------------------------------
    // Estado
    // -------------------------------------------------------------------------

    /** @return true si está siendo transportado por algún barco */
    public boolean isCarried() { return carrierTreasure != null; }

    /** @return true si está flotando (barco portador hundido) */
    public boolean isFloating() { return isFloating; }

    /** @return true si está en el mapa sin barco que lo lleve */
    public boolean isOnMap() { return carrierTreasure == null; }

    /** @return true si el tesoro fue capturado */
    public boolean isCaptured() {
        if (getCarrierTreasure() == null) {
            return false;
        }
        return this.owner.hasShip(getCarrierTreasure());
    }

    // -------------------------------------------------------------------------
    // Acciones / Comportamiento
    // -------------------------------------------------------------------------

    /**
     * Un barco recoge el tesoro del mapa.
     *
     * @param ship barco que lo recoge
     */
    public void pickUp(Ship ship) {
        setCarrierTreasure(ship);
        setPositionCarrierTreasure(null);
        setFloating(false);
    }

    /**
     * El barco portador fue hundido: el tesoro queda flotando en la posición del barco.
     * Puede usarse para entregas en puertos para la condicion de victoria.
     *
     * @param sinkPosition posición donde se hundió el barco
     */
    public void dropForSink(Position sinkPosition) {
        setCarrierTreasure(null);
        setPositionCarrierTreasure(sinkPosition);
        setFloating(true);
    }

    /**
     * El tesoro se deposita de formas segura en el puerto.
     * El criterio es false para distinguir el hundimiento
     * respecto de una entrega al puerto.
     *
     * @param portPosition posición de entrega del puerto
     */
    public void dropForSafePort(Position portPosition) {
        setCarrierTreasure(null);
        setPositionCarrierTreasure(portPosition);
        setFloating(false);
    }

    /**
     * El jugador entrega el tesoro en su propio puerto (victoria).
     * Llama a drop en la posición del puerto; la victoria la verifica GameState.
     *
     * @param portPosition posición del puerto
     */
    public void deliver(Position portPosition) {
        dropForSafePort(portPosition);
    }

    /**
     * Actualiza la posición del tesoro para que siga al barco portador.
     *
     * @param newPosition nueva posición del barco
     */
    public void updatePositionWithCarrier(Position newPosition) {
        setPositionCarrierTreasure(newPosition);
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public Player getOwner() { return owner; }
    public Position getPositionCarrierTreasure() { return positionCarrierTreasure; }
    public Ship getCarrierTreasure() { return carrierTreasure; }

    // -------------------------------------------------------------------------
    // Setters
    // -------------------------------------------------------------------------

    /**
     * Establecer la carga del tesoro en barco.
     * @param carrierTreasure el barco que lleva el tesoro y null si queda en el mapa
     */
    private void setCarrierTreasure(Ship carrierTreasure) {
        this.carrierTreasure = carrierTreasure;
    }

    /**
     * Establece la posicion del barco que lleva el tesoro.
     * @param positionCarrierTreasure es null si esta en el barco, sino Position.
     */
    private void setPositionCarrierTreasure(Position positionCarrierTreasure) {
        if (this.carrierTreasure != null && positionCarrierTreasure != null) {
            throw new IllegalStateException("No se puede establecer una posicion si el tesoro esta sobre un barco.");
        }

        if (this.carrierTreasure == null && positionCarrierTreasure == null) {
            throw new IllegalStateException("La posicion en el mapa no puede ser nula si ningun barco lo esta transportando.");
        }

        this.positionCarrierTreasure = positionCarrierTreasure;
    }

    /**
     * Establece el estado de si el tesoro flota o no.
     * @param isFloating true si el tesoro flota, sino false.
     */
    private void setFloating(boolean isFloating) {
        if (isFloating() && this.carrierTreasure != null) {
            throw new IllegalStateException("El barco no fue hundido, para cambiarle el estado.");
        }

        this.isFloating = isFloating;
    }
}