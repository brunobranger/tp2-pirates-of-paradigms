package tp2.model.combat;

import tp2.model.ship.Ship;

/**
 * Resultado de un ataque. Value Object inmutable.
 * Usado por la vista para mostrar el log del combate.
 */
public class AttackResult {

    private final Ship attacker;
    private final Ship defender;
    private final int damageDealt;
    private final boolean defenderSunk;

    public AttackResult(Ship attacker, Ship defender, int damageDealt, boolean defenderSunk) {
        this.attacker = attacker;
        this.defender = defender;
        this.damageDealt = damageDealt;
        this.defenderSunk = defenderSunk;
    }

    public Ship getAttacker() { return attacker; }
    public Ship getDefender() { return defender; }
    public int getDamageDealt() { return damageDealt; }
    public boolean isDefenderSunk() { return defenderSunk; }

    @Override
    public String toString() {
        String result = attacker.getName() + " atacó a " + defender.getName()
                + " causando " + damageDealt + " de daño.";
        if (defenderSunk) result += " ¡" + defender.getName() + " fue hundido!";
        return result;
    }
}