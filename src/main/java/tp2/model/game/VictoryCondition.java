package tp2.model.game;

/**
 * Las tres condiciones de victoria definidas en el enunciado.
 */
public enum VictoryCondition {
    /** El barco insignia rival fue hundido. */
    FLAGSHIP_SUNK,
    /** El tesoro dinástico enemigo fue capturado y llevado al propio puerto. */
    TREASURE_CAPTURED,
    /** El rival decidió rendirse. */
    SURRENDER
}