package tp2.model.board;

/**
 * Las 8 direcciones de movimiento posibles en el tablero.
 * Cada dirección encapsula su delta de fila y columna.
 *
 * Principio: Tell Don't Ask — se le "dice" a la posición en qué dirección
 * moverse, en lugar de que el cliente calcule los deltas manualmente.
 */
public enum Direction {

    NORTH     (-1,  0),
    NORTH_EAST(-1,  1),
    EAST      ( 0,  1),
    SOUTH_EAST( 1,  1),
    SOUTH     ( 1,  0),
    SOUTH_WEST( 1, -1),
    WEST      ( 0, -1),
    NORTH_WEST(-1, -1);

    private final int rowDelta;
    private final int colDelta;

    Direction(int rowDelta, int colDelta) {
        this.rowDelta = rowDelta;
        this.colDelta = colDelta;
    }

    public int getRowDelta() { return rowDelta; }
    public int getColDelta() { return colDelta; }
}