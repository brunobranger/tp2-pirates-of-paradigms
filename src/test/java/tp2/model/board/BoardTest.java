package tp2.model.board;

import org.junit.jupiter.api.Test;
import tp2.model.ship.Sloop;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias de las reglas espaciales del tablero:
 * navegabilidad, ocupacion de celdas y corrientes marinas.
 */
class BoardTest {

    @Test
    void reachablePositionsExcludeIslandsAndOccupiedCells() {
        Board board = new Board(3, 3);
        Position origin = new Position(1, 1);
        board.getCellAt(new Position(0, 0)).setTerrain(TerrainType.ISLAND);
        board.getCellAt(new Position(0, 1)).placeShip(new Sloop("Bloqueo"));

        Set<Position> reachable = board.getReachablePositions(origin, 1);

        assertEquals(6, reachable.size());
        assertFalse(reachable.contains(new Position(0, 0)));
        assertFalse(reachable.contains(new Position(0, 1)));
        assertTrue(reachable.contains(new Position(2, 2)));
    }

    @Test
    void marineCurrentPushesShipWhenDestinationIsFree() {
        Board board = new Board(3, 3);
        Sloop ship = new Sloop("Veloz");
        Position current = new Position(1, 1);
        Position pushed = new Position(1, 2);
        ship.setPosition(current);
        board.getCellAt(current).placeShip(ship);
        board.getCellAt(current).setTerrain(TerrainType.MARINE_CURRENT);
        board.getCellAt(current).setCurrentDirection(Direction.EAST);

        board.applyMarineCurrentIfPresent(ship);

        assertEquals(pushed, ship.getPosition());
        assertFalse(board.getCellAt(current).isOccupied());
        assertEquals(ship, board.getCellAt(pushed).getOccupant());
    }
}
