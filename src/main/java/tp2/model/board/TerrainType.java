package tp2.model.board;

/**
 * Tipos de terreno marítimo definidos en el enunciado.
 * Cada tipo tiene efectos distintos sobre el movimiento y combate.
 */
public enum TerrainType {

    /** Sin efectos sobre movimiento ni combate. */
    OPEN_SEA,

    /** Reduce el movimiento a 1 casilla adyacente. */
    STORM,

    /**
     * Permite recuperar HP al finalizar el turno en puerto propio.
     * Si además se transporta el tesoro enemigo, el jugador gana la partida.
     */
    PORT,

    /** Reduce visión y rango de ataque. */
    FOG,

    /** Daña a los barcos que ingresan en esta casilla. */
    REEF,

    /**
     * Empuja al barco una casilla adicional en una dirección predefinida.
     * La dirección se define en la celda (ver Cell).
     */
    MARINE_CURRENT,

    /** Intransitable. Ningún barco puede ingresar. */
    ISLAND,

    /** Celda ocupada por Kraken, intransitable. */
    KRAKEN,

    /** Celdas bloqueada por efecto del Kraken, intransitable. */
    KRAKEN_BLOCKED_CELL
}
