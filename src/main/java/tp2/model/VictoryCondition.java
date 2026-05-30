package tp2.model;

/**
 * Condiciones de victoria pausibles.
 */
public enum VictoryCondition {
    /** El barco insignia rival fue hundido. */
    FLAGSHIP_SUNK,
    /** El tesoro dinástico enemigo fue capturado y llevado al propio puerto. */
    TREASURE_CAPTURED,
    /** El rival decidió rendirse. */
    SURRENDER
}