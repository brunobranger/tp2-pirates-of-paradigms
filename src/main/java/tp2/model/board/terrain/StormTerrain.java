package tp2.model.board.terrain;

import tp2.model.ship.Ship;
import tp2.model.ship.effect.ReducedMovementEffect;

public class StormTerrain implements Terrain {
    private static final int EFFECT_DURATION_TURNS = 5;

    @Override
    public void applyEntryEffect(Ship ship) {
        ship.addEffect(new ReducedMovementEffect(EFFECT_DURATION_TURNS));
    }
}
