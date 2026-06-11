package tp2.model.ship.effect;

import tp2.model.ship.EffectType;
import tp2.model.ship.Ship;

/**
 * Interfaz para efectos de estado sobre un barco.
 */
public interface ShipEffect {

    /**
     * @return tipo de efecto (para identificarlo sin instanceof)
     */
    EffectType getType();

    /**
     * retorna una nueva instancia del efecto con los mismos parámetros
     * @return
     */
    ShipEffect clone();

    /**
     * Avanza un turno en la duración del efecto.
     * Los efectos permanentes ignoran este llamado.
     */
    void tick();

    /**
     * @return true si el efecto venció y debe eliminarse
     */
    boolean isExpired();

    /**
     * Modifica el movimiento efectivo del barco.
     * Por defecto no modifica (retorna el valor recibido).
     *
     * @param baseMovement movimiento base
     * @return movimiento modificado por este efecto
     */
    default int modifyMovement(int baseMovement) {
        return baseMovement;
    }

    /**
     * Modifica el rango de disparo efectivo del barco.
     *
     * @param baseRange rango base
     * @return rango modificado
     */
    default int modifyRange(int baseRange) {
        return baseRange;
    }

    /**
     * Modifica el ataque base para los disparos del barco.
     * @param baseAttack ataque base
     * @return ataque modificado
     */
    default int modifyAttack(int baseAttack) {
        return baseAttack;
    }

    /**
     * @return true si este efecto impide atacar (ej. Incendio)
     */
    default boolean preventsAttack() {
        return false;
    }

    /**
     * @return true si este efecto impide moverse (ej. Kraken)
     */
    default boolean preventsMovement() {
        return false;
    }

    default boolean allowsMoveAfterAttack() {
        return false;
    }

    /**
     * Aplica el daño por tick del efecto al barco (ej. Incendio daña al finalizar turno).
     *
     * @param ship barco afectado
     */
    default void applyEndOfTurnDamage(Ship ship) {}
}
