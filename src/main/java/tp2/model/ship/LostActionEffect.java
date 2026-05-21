package tp2.model.ship;

/**
 * Motin: el barco pierde sus acciones durante una cantidad de turnos.
 */
public class LostActionEffect implements ShipEffect {

    private int turnsRemaining;

    public LostActionEffect(int turns) {
        if (turns <= 0) {
            throw new IllegalArgumentException("Turns must be positive");
        }
        this.turnsRemaining = turns;
    }

    @Override
    public EffectType getType() {
        return EffectType.LOST_ACTION;
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
    public boolean preventsAttack() {
        return true;
    }

    @Override
    public boolean preventsMovement() {
        return true;
    }

    public int getTurnsRemaining() {
        return turnsRemaining;
    }
}
