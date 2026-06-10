package tp2.model.game.system;

import tp2.model.board.Cell;
import tp2.model.board.terrain.TerrainRegistry;
import tp2.model.player.Player;
import tp2.model.ship.Ship;
import tp2.model.treasure.Treasure;

public class CellInteractionResolver {
    private final TerrainRegistry terrainRegistry;

    public CellInteractionResolver(TerrainRegistry terrainRegistry) {
        this.terrainRegistry = terrainRegistry;
    }

    public void resolveEntry(Cell cell, Ship ship, Player player) {
        collectTreasureIfPresent(cell, ship, player);
        terrainRegistry.resolve(cell.getTerrain()).applyEntryEffect(ship);
    }

    private void collectTreasureIfPresent(Cell cell, Ship ship, Player player) {
        if (cell.getTreasure() == null) {
            return;
        }
        Treasure treasure = cell.pickUpTreasure();
        treasure.onPickUp(ship, player);
    }
}
