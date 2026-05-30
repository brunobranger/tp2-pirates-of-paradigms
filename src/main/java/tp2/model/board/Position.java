package tp2.model.board;

import java.util.Objects;

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
        int rowDiff = Math.abs(this.row - other.row);
        int colDiff = Math.abs(this.col - other.col);
        return Math.max(rowDiff, colDiff);
    }

    /**
     * Devuelve true si esta posición es adyacente (8 direcciones) a la dada.
     *
     * @param other posición a comparar
     * @return true si son adyacentes
     */
    public boolean isAdjacentTo(Position other) {
        return this.distanceTo(other) == 1;
    }

    /**
     * Devuelve una nueva posición desplazada según la dirección indicada.
     *
     * @param direction dirección de desplazamiento
     * @return nueva posición resultante
     */
    public Position translate(Direction direction) {
        return new Position(this.row + direction.getRowDelta(), this.col + direction.getColDelta());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Position other = (Position) o;
        return this.row == other.row && this.col == other.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}