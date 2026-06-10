package tp2.model.board.terrain;

import tp2.config.GameConfig;
import tp2.model.ship.Ship;

public class ReefTerrain implements Terrain {
    @Override
    public void applyEntryEffect(Ship ship) {
        ship.receiveDamage(GameConfig.REEF_DAMAGE);
    }
}
