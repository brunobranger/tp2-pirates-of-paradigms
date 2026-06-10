package tp2.model.board.terrain;

import tp2.model.ship.Ship;
import tp2.model.ship.effect.ReducedAttackEffect;
import tp2.model.ship.effect.ReducedMovementEffect;

public class FogTerrain implements Terrain {
    private static final int EFFECT_DURATION_TURNS = 5;
    private static final double ATTACK_MULTIPLIER = 0.4;

    @Override
    public void applyEntryEffect(Ship ship) {
        ship.addEffect(new ReducedMovementEffect(EFFECT_DURATION_TURNS));
        ship.addEffect(new ReducedAttackEffect(EFFECT_DURATION_TURNS, ATTACK_MULTIPLIER));
    }
}
