package tp2.model.ship;

/**
 * Efecto de inmovilización (Kraken marino).
 * Impide el movimiento durante una cantidad de turnos.
 */
public class ImmobilizedEffect implements ShipEffect {

    private int turnsRemaining;

    public ImmobilizedEffect(int turns) {
        if (turns <= 0) {
            throw new IllegalArgumentException("Turns must be positive");
        }
        this.turnsRemaining = turns;
    }

    @Override
    public EffectType getType() { return EffectType.IMMOBILIZED; }

    @Override
    public void tick() { turnsRemaining--; }

    @Override
    public boolean isExpired() { return turnsRemaining <= 0; }

    @Override
    public boolean preventsMovement() { return true; }

    public int getTurnsRemaining() { return turnsRemaining; }
}
