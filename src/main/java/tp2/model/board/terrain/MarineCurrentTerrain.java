package tp2.model.board.terrain;

import tp2.model.ship.Ship;

public class MarineCurrentTerrain implements Terrain {
    @Override
    public void applyEntryEffect(Ship ship) {
        // Push is resolved by Board.applyMarineCurrentIfPresent.
    }
}
