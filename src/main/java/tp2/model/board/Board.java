package tp2.model.board;

import tp2.model.ship.Ship;
import tp2.model.player.Player;

import java.util.List;
import java.util.Set;

/**
 * El tablero de juego: una rejilla rectangular de {@link Cell}s.
 *
 * Responsabilidades del tablero:
 *   - Mantener la grilla y brindar acceso a celdas por posición.
 *   - Resolver si una posición es válida dentro de los límites.
 *   - Calcular celdas alcanzables dado un rango de movimiento o disparo.
 *   - NO contiene reglas de turno ni de victoria (SRP).
 *
 * Patrón: el tablero actúa como Facade para las consultas espaciales.
 * UML: Board tiene composición con Cell (si el Board se destruye, las celdas también).
 */
public class Board {

    private final int rows;
    private final int cols;
    private final Cell[][] grid;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];
        // inicializar todas las celdas con TerrainType.OPEN_SEA
    }

    // -------------------------------------------------------------------------
    // Acceso a celdas
    // -------------------------------------------------------------------------

    /**
     * Retorna la celda en la posición dada.
     *
     * @param position posición a consultar
     * @return celda correspondiente
     * @throws IllegalArgumentException si la posición está fuera del tablero
     */
    public Cell getCellAt(Position position) {
        // validar límites y retornar grid[position.getRow()][position.getCol()]
        return null;
    }

    /**
     * @param position posición a verificar
     * @return true si la posición está dentro de los límites del tablero
     */
    public boolean isWithinBounds(Position position) {
        // TODO
        return false;
    }

    // -------------------------------------------------------------------------
    // Movimiento
    // -------------------------------------------------------------------------

    /**
     * Calcula todas las posiciones navegables alcanzables desde {@code origin}
     * con hasta {@code movementPoints} pasos (BFS en 8 direcciones).
     * Excluye casillas con islas, barcos u otros bloqueos.
     *
     * @param origin         posición de origen
     * @param movementPoints cantidad máxima de casillas a moverse
     * @return conjunto de posiciones alcanzables
     */
    public Set<Position> getReachablePositions(Position origin, int movementPoints) {
        // ...
        return Set.of();
    }

    /**
     * Mueve un barco de su posición actual a la posición destino.
     * Actualiza las celdas de origen y destino.
     *
     * @param ship        barco a mover
     * @param destination posición destino (debe ser alcanzable)
     * @throws IllegalArgumentException si el destino no es navegable
     */
    public void moveShip(Ship ship, Position destination) {
        // ...
    }

    /**
     * Aplica el efecto de corriente marina al barco si la celda destino es MARINE_CURRENT.
     * Mueve el barco una casilla adicional en la dirección de la corriente.
     *
     * @param ship barco que acaba de moverse
     */
    public void applyMarineCurrentIfPresent(Ship ship) {
        // ...
    }

    // -------------------------------------------------------------------------
    // Combate / rango
    // -------------------------------------------------------------------------

    /**
     * Retorna los barcos enemigos dentro del rango de disparo del atacante.
     *
     * @param attacker barco atacante
     * @param range    rango de disparo (distancia de Chebyshev)
     * @param enemy    jugador enemigo
     * @return lista de barcos enemigos atacables
     */
    public List<Ship> getTargetsInRange(Ship attacker, int range, Player enemy) {
        // ...
        return List.of();
    }

    // -------------------------------------------------------------------------
    // Consultas generales
    // -------------------------------------------------------------------------

    /**
     * Retorna todas las posiciones navegables y libres (sin barco, sin isla).
     * Usada para colocar tesoros dinásticos al inicio o tras evento.
     *
     * @return lista de posiciones libres navegables
     */
    public List<Position> getFreeNavigablePositions() {
        // ...
        return List.of();
    }

    /**
     * Retorna una posición aleatoria libre navegable.
     * Usada para tesoros dinásticos y tesoros flotantes de eventos.
     *
     * @return posición aleatoria libre
     */
    public Position getRandomFreePosition() {
        // usar Random sobre getFreeNavigablePositions()
        return null;
    }

    /**
     * Elimina al barco del tablero (lo hunde).
     * Si el barco transporta un tesoro dinástico, lo deja flotando en la celda.
     *
     * @param ship barco a hundir
     */
    public void sinkShip(Ship ship) {
        // ...
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
} 