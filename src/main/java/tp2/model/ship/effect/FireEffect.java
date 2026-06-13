package tp2.model.ship.effect;

import tp2.model.ship.EffectType;
import tp2.model.ship.Ship;

/**
 * Incendio en cubierta: bloquea el ataque y causa danio por turno.
 */
public class FireEffect implements ShipEffect {

    public static final int DEFAULT_DAMAGE_PER_TURN = 10;

    private final int damagePerTurn;

    public FireEffect() {
        this(DEFAULT_DAMAGE_PER_TURN);
    }

    public FireEffect(int damagePerTurn) {
        if (damagePerTurn <= 0) {
            throw new IllegalArgumentException("Damage per turn must be positive");
        }
        this.damagePerTurn = damagePerTurn;
    }

    @Override
    public ShipEffect clone(){
        return new FireEffect(this.damagePerTurn);
    }

    @Override
    public EffectType getType() {
        return EffectType.FIRE;
    }

    @Override
    public void tick() {
        // Permanente hasta apagarse.
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public boolean preventsAttack() {
        return true;
    }

    @Override
    public void applyEndOfTurnDamage(Ship ship) {
        ship.receiveDamage(damagePerTurn);
    }

    @Override
    public int calculateEndOfTurnDamage(Ship ship) {
        return Math.max(0, damagePerTurn - ship.getDef());
    }

    public int getDamagePerTurn() {
        return damagePerTurn;
    }
}
