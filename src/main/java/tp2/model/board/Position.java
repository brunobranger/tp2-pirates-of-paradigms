package tp2.model.board;

/**
 * Value Object que representa una posición (fila, columna) en el tablero.
 *
 * Principio: inmutabilidad — una posición no cambia; se crean posiciones nuevas.
 * Esto evita efectos secundarios al compartir referencias entre objetos.
 */
public final class Position {

    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    /**
     * Calcula la distancia de Chebyshev (máximo entre diferencia de filas y columnas),
     * usada para determinar alcance de disparo y movimiento en 8 direcciones.
     *
     * @param other posición destino
     * @return distancia de Chebyshev entre this y other
     */
    public int distanceTo(Position other) {
        // ...
        return 0;
    }

    /**
     * Devuelve true si esta posición es adyacente (8 direcciones) a la dada.
     *
     * @param other posición a comparar
     * @return true si son adyacentes
     */
    public boolean isAdjacentTo(Position other) {
        // ...
        return false;
    }

    /**
     * Devuelve una nueva posición desplazada según la dirección indicada.
     *
     * @param direction dirección de desplazamiento
     * @return nueva posición resultante
     */
    public Position translate(Direction direction) {
        // ...
        return null;
    }

    @Override
    public boolean equals(Object o) {
        // ...
        return false;
    }

    @Override
    public int hashCode() {
        // ...
        return 0;
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}