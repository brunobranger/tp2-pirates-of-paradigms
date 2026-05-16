package tp2.model.ship;

/**
 * Tipos de efecto de estado para identificación sin instanceof.
 * Principio: Tell Don't Ask — el Ship pregunta el tipo para decidir
 * comportamientos específicos (ej. canAttack chequea FIRE).
 */
public enum EffectType {
    FIRE,             // Incendio: bloquea ataque, daña por turno
    IMMOBILIZED,      // Inmovilización (Kraken): bloquea movimiento
    REDUCED_MOVEMENT, // Vendaval/Tormenta: reduce MOV a 1
    REDUCED_ATTACK,   // Motín: reduce ATK temporalmente
    LOST_ACTION,      // Motín: pierde próxima acción
    FAVORABLE_WIND    // Viento Favorable: bonus MOV y RNG
}