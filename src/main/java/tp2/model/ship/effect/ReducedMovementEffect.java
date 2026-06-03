package tp2.model.ship.effect;

import tp2.model.ship.EffectType;

/**
 * Efecto temporal que reduce el movimiento a 1 (Vendaval, Tormenta).
 * Dura una cantidad configurable de turnos.
 */
public class ReducedMovementEffect implements ShipEffect {

    private int turnsRemaining;

    public ReducedMovementEffect(int turns) {
        if (turns <= 0) {
            throw new IllegalArgumentException("Turns must be positive");
        }
        this.turnsRemaining = turns;
    }

    @Override
    public EffectType getType() { return EffectType.REDUCED_MOVEMENT; }

    @Override
    public void tick() { turnsRemaining--; }

    @Override
    public boolean isExpired() { return turnsRemaining <= 0; }

    @Override
    public int modifyMovement(int baseMovement) { return 1; }

    public int getTurnsRemaining() { return turnsRemaining; }
}
