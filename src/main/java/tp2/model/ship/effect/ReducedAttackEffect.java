package tp2.model.ship.effect;

import tp2.model.ship.EffectType;

/**
 * Efecto temporal que reduce el ataque del barco.
 */
public class ReducedAttackEffect implements ShipEffect {

    private final double multiplier;
    private int turnsRemaining;

    public ReducedAttackEffect(int turns, double multiplier) {
        if (turns <= 0) {
            throw new IllegalArgumentException("Turns must be positive");
        }
        if (multiplier < 0 || multiplier > 1) {
            throw new IllegalArgumentException("Multiplier must be between 0 and 1");
        }
        this.turnsRemaining = turns;
        this.multiplier = multiplier;
    }

    @Override
    public ShipEffect clone(){
        return new ReducedAttackEffect(this.turnsRemaining, this.multiplier);
    }

    @Override
    public EffectType getType() {
        return EffectType.REDUCED_ATTACK;
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
    public int modifyAttack(int baseAttack) {
        return (int) Math.floor(baseAttack * multiplier);
    }

    public int getTurnsRemaining() {
        return turnsRemaining;
    }
}
