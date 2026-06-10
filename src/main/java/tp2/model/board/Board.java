package tp2.model.board;

import tp2.config.GameConfig;
import tp2.model.ship.Ship;
import tp2.model.player.Player;

import java.util.*;

/**
 * El tablero de juego: una rejilla rectangular de {@link Cell}s.
 * Patrón: el tablero actúa como Facade para las consultas espaciales.
 */
public class Board {

    private final int rows;
    private final int cols;
    private final Cell[][] grid;
    private record Node(Position position, int steps) {}

    /**
     * Inicializacion de constructor con metodos pasados correctamente.
     * @param rows debe ser positiva
     * @param cols debe ser positiva
     */
    public Board(int rows, int cols) {
        this.rows = setRows(rows);
        this.cols = setCols(cols);
        this.grid = new Cell[this.rows][this.cols];
        initializeGrid();
    }

    /**
     * Sobrecarga de constructor en caso de no enviar argumentos iniciales.
     */
    public Board() {
        this(GameConfig.DEFAULT_BOARD_ROWS, GameConfig.DEFAULT_BOARD_COLS);
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

    public Ship getShipAt(Position position) {
        return getCellAt(position).getOccupant();
    }

    public Cell getCellAtShipPosition(Ship ship) {
        return getCellAt(ship.getPosition());
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
        Position origin = ship.getPosition();

        if (!getCellAt(destination).isNavigable()) throw new IllegalArgumentException("Destination is not navigable");

        getCellAt(origin).removeShip();
        getCellAt(destination).placeShip(ship);
        ship.setPosition(destination);
    }

    public void placeShip(Ship ship, Position destination) {
        getCellAt(destination).placeShip(ship);
    }

    /**
     * Aplica el efecto de corriente marina al barco si la celda destino es MARINE_CURRENT.
     * Mueve el barco una casilla adicional en la dirección de la corriente.
     *
     * @param ship barco que acaba de moverse
     */
    public void applyMarineCurrentIfPresent(Ship ship) {
        Position current = ship.getPosition();
        Cell cell = getCellAt(current);

        if (cell.getTerrain() != TerrainType.MARINE_CURRENT) return;

        Position pushed = current.translate(cell.getCurrentDirection());
        if (isWithinBounds(pushed) && getCellAt(pushed).isNavigable()) {
            moveShip(ship, pushed);
        }
    }

    // -------------------------------------------------------------------------
    // Eventos
    // -------------------------------------------------------------------------

    /**
     * Aplica un clima temporal en un área central del tablero con un tamaño definido por el factor
     * areaPercent relativo al tamaño del mismo.
     * @param areaPercent factor entre 0 y 1 que determina el tamaño del área central afectada
     * @param climate clima a aplicar en las celdas
     * @param duration duración en turnos
     */
    public List<Position> applyCentralClimateEffect(float areaPercent, TerrainType climate, int duration){

        List<Position> affectedPositions = new ArrayList<>();

        if(areaPercent < 0 || areaPercent > 1)
            throw new IllegalArgumentException("areaPercent debe estar entre 0 y 1");

        //Obtener casillas centrales y calcular el area afectada con el porcentaje recibido
        int height = (int) (this.rows * areaPercent);
        int wide = (int) (this.cols * areaPercent);
        int initRow = Math.max(0, (rows / 2) - (height / 2));
        int endRow = (rows / 2) + (height / 2);
        int initCol = Math.max(0, (cols / 2) - (wide / 2));
        int endCol = (cols / 2) + (wide / 2);

        for (int row = initRow; row <= endRow; row++) {
            for (int col = initCol; col <= endCol; col++) {
                Position position = new Position(row, col);
                this.getCellAt(position).applyTemporalClimate(climate, duration);
                affectedPositions.add(position);
            }
        }

        return affectedPositions;
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
            if (attacker.isInRange(ship, range)) {
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
            cell.placeTreasure(ship.dropTreasure());
        }
        cell.removeShip();
    }

    private void initializeGrid() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.grid[i][j] = new Cell(new Position(i, j), TerrainType.OPEN_SEA);
            }
        }
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    private int setCols(int cols) {
        if (cols < 0) {
            throw new IllegalArgumentException("cols must be positive");
        }
        return cols;
    }

    private int setRows(int rows) {
        if (rows < 1) {
            throw new IllegalArgumentException("rows must be greater than 0");
        }
        return rows;
    }
} 
