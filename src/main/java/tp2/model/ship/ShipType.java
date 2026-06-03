package tp2.model.ship;

/**
 * Tipos de barco definidos en el enunciado.
 * Usado para la vista (representación en tablero) y para lógica de eventos
 */
public enum ShipType {
    /** Balandra: ágil, rápido, bajo ataque. */
    SLOOP,
    /** Fragata: ágil, mayor ATK y DEF que balandra. */
    FRIGATE,
    /** Galeón: pesado, lento, resistente, gran armamento. */
    GALLEON,
    /** Barco Insignia: pesado, mejores estadísticas. Su hundimiento da la victoria al rival. */
    FLAGSHIP
}