package tp2.model.board;

import tp2.model.ship.Ship;
import tp2.model.player.Player;

import java.util.*;

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
    private record Node(Position position, int steps) {} // Para hacer BFS

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
               this.grid[i][j] = new Cell(new Position(i, j), TerrainType.OPEN_SEA);
            }
        }
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
        if (!this.isWithinBounds(position)) throw new IllegalArgumentException("The cell is not within the board bounds");
        return grid[position.getRow()][position.getCol()];
    }

    /**
     * @param position posición a verificar
     * @return true si la posición está dentro de los límites del tablero
     */
    public boolean isWithinBounds(Position position) {
        int row = position.getRow();
        int col = position.getCol();
        return row >= 0 && row < rows && col >= 0 && col < cols;
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
        Queue<Node> queue = new LinkedList<>();
        Set<Position> visited = new HashSet<>();
        Set<Position> result = new HashSet<>();

        queue.add(new Node(origin, 0));
        visited.add(origin);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            for (Direction direction : Direction.values()) {
                Position neighbor = current.position().translate(direction);

                if (!isWithinBounds(neighbor)) continue;
                if (visited.contains(neighbor)) continue;
                if (!getCellAt(neighbor).isNavigable()) continue;
                if (current.steps() + 1 > movementPoints) continue;

                visited.add(neighbor);
                result.add(neighbor);
                queue.add(new Node(neighbor, current.steps() + 1));
            }
        }

        return result;
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
        Cell origin = getCellAt(ship.getPosition());
        Cell dest = getCellAt(destination);

        if (!dest.isNavigable()) throw new IllegalArgumentException("Destination is not navigable");

        origin.removeShip();
        dest.placeShip(ship);
        ship.setPosition(destination);
    }

    /**
     * Aplica el efecto de corriente marina al barco si la celda destino es MARINE_CURRENT.
     * Mueve el barco una casilla adicional en la dirección de la corriente.
     *
     * @param ship barco que acaba de moverse
     */
    public void applyMarineCurrentIfPresent(Ship ship) {
        Cell cell = getCellAt(ship.getPosition());
        if (cell.getTerrain() != TerrainType.MARINE_CURRENT) return;

        Position pushed = ship.getPosition().translate(cell.getCurrentDirection());
        if (isWithinBounds(pushed) && getCellAt(pushed).isNavigable()) {
            moveShip(ship, pushed);
        }
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
        List<Ship> targets = new ArrayList<>();
        for (Ship ship : enemy.getActiveFleet()) {
            if (attacker.getPosition().distanceTo(ship.getPosition()) <= range) {
                targets.add(ship);
            }
        }
        return targets;
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
       List<Position> freeNavigablePositions = new ArrayList<>();
       for (int i = 0; i < rows; i++) {
           for (int j = 0; j < cols; j++) {
               if (grid[i][j].isNavigable()) {
                   freeNavigablePositions.add(new Position(i, j));
               }
           }
       }
       return freeNavigablePositions;
    }

    /**
     * Retorna una posición aleatoria libre navegable.
     * Usada para tesoros dinásticos y tesoros flotantes de eventos.
     *
     * @return posición aleatoria libre
     */
    public Position getRandomFreePosition() {
        List<Position> free = getFreeNavigablePositions();
        if (free.isEmpty()) throw new IllegalArgumentException("No navigable positions available");
        return free.get(new Random().nextInt(free.size()));
    }

    /**
     * Elimina al barco del tablero (lo hunde).
     * Si el barco transporta un tesoro dinástico, lo deja flotando en la celda.
     *
     * @param ship barco a hundir
     */
    public void sinkShip(Ship ship) {
        Cell cell = getCellAt(ship.getPosition());
        if (ship.isCarryingTreasure()) {
            cell.placeFloatingTreasure(ship.dropTreasure());
        }
        cell.removeShip();
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
} 