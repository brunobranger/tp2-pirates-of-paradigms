package tp2.model.board.terrain;

import tp2.model.ship.Ship;

public class PortTerrain implements Terrain {
    @Override
    public void applyEntryEffect(Ship ship) {
        // Port effects are resolved at end of turn by TurnProcessor/PortService.
    }
}
