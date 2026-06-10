package tp2.model.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    void seaCellCanPlaceStructure() {
        Cell cell = new Cell(new Position(0, 0), TerrainType.OPEN_SEA);
        assertTrue(cell.canPlaceStructure());
    }

    @Test
    void islandCannotPlaceStructure() {
        Cell cell = new Cell(new Position(0, 0), TerrainType.ISLAND);
        assertFalse(cell.canPlaceStructure());
    }

    @Test
    void portCannotPlaceStructure() {
        Cell cell = new Cell(new Position(0, 0), TerrainType.PORT);
        assertFalse(cell.canPlaceStructure());
    }

    @Test
    void climateExpiresAndRestoresOriginalTerrain() {
        Cell cell = new Cell(new Position(0, 0), TerrainType.OPEN_SEA);

        cell.applyTemporalClimate(TerrainType.STORM, 1);
        assertEquals(TerrainType.STORM, cell.getTerrain());

        cell.decrementClimateDuration();
        assertEquals(TerrainType.OPEN_SEA, cell.getTerrain());
    }

    @Test
    void navigablePortWithoutOccupantIsNavigable() {
        Cell cell = new Cell(new Position(0, 0), TerrainType.PORT);
        assertTrue(cell.isNavigable());
    }
}