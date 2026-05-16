package tp2.model.ship;

/**
 * Interfaz para efectos de estado sobre un barco.
 *
 * Patrón: Strategy — cada efecto sabe cómo aplicarse, cuánto dura y cuándo expira.
 * El Ship delega en sus efectos activos sin conocer los detalles de cada uno (OCP).
 *
 * Ejemplos de efectos: Incendio, Inmovilización, MovimientoReducido, AtaqueBloqueado.
 *
 * UML: Ship --tiene--> List<ShipEffect> (composición: los efectos viven con el barco).
 */
public interface ShipEffect {

    /**
     * @return tipo de efecto (para identificarlo sin instanceof)
     */
    EffectType getType();

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

    /**
     * Aplica el daño por tick del efecto al barco (ej. Incendio daña al finalizar turno).
     *
     * @param ship barco afectado
     */
    default void applyEndOfTurnDamage(Ship ship) {
        // la mayoría no hace nada
    }
}