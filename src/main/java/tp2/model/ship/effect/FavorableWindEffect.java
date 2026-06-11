package tp2.model.ship.effect;

import tp2.model.ship.EffectType;

/**
 * Viento favorable: mejora movimiento/rango y habilita mover y atacar en el mismo turno.
 */
public class FavorableWindEffect implements ShipEffect {

    private final int movementBonus;
    private final int rangeBonus;
    private int turnsRemaining;

    public FavorableWindEffect(int turns, int movementBonus, int rangeBonus) {
        if (turns <= 0) {
            throw new IllegalArgumentException("Turns must be positive");
        }
        this.turnsRemaining = turns;
        this.movementBonus = movementBonus;
        this.rangeBonus = rangeBonus;
    }

    @Override
    public ShipEffect clone(){
        return new FavorableWindEffect(this.turnsRemaining, this.rangeBonus, this.movementBonus);
    }

    @Override
    public EffectType getType() {
        return EffectType.FAVORABLE_WIND;
    }

    @Override
    public void tick() {
        turnsRemaining--;
    }

    @Override
    public boolean isExpired() {
        return turnsRemaining <= 0;
    }

    @Override
    public int modifyMovement(int baseMovement) {
        return baseMovement + movementBonus;
    }

    @Override
    public int modifyRange(int baseRange) {
        return baseRange + rangeBonus;
    }

    @Override
    public boolean allowsMoveAfterAttack() {
        return true;
    }

    public int getTurnsRemaining() {
        return turnsRemaining;
    }
}
