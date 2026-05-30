package tp2.model.combat;

import tp2.model.ship.Ship;
import tp2.model.board.Board;

/**
 * Sistema de combate: resuelve los ataques entre barcos.
 *
 * Patrón: se extrae la lógica de combate de Ship y Board para respetar SRP.
 * Ship sabe recibir daño; CombatSystem sabe las reglas del enfrentamiento.
 *
 * Flujo según enunciado:
 *   1. Verificar alcance.
 *   2. Calcular daño = ATK atacante - DEF defensor (mínimo 0).
 *   3. Aplicar daño al HP enemigo.
 *   4. Si HP == 0, el barco se hunde.
 */
public class CombatSystem {

    private final Board board;

    public CombatSystem(Board board) {
        this.board = board;
    }

    /**
     * Ejecuta un ataque del {@code attacker} al {@code defender}.
     * Verifica rango, calcula daño y aplica resultado.
     *
     * @param attacker barco atacante
     * @param defender barco defensor
     * @return resultado del combate con detalles del daño
     * @throws IllegalArgumentException si el defensor está fuera de rango
     */
    public AttackResult attack(Ship attacker, Ship defender) {

        boolean defenderSunk = false;
        if (!attacker.canAttack())
            throw new RuntimeException("El barco elegido no puede atacar");
        if (isInRange(attacker, defender))
            throw new RuntimeException("El barco a atacar está fuera de rango");

        int effectiveDamage = defender.receiveDamage(attacker.getEffectiveAttack());
        attacker.registerAttack();
        if(defender.isSunk()) {
            board.sinkShip(defender);
            defenderSunk = true;
        }

        return new AttackResult(attacker, defender, effectiveDamage, defenderSunk);
    }

    /**
     * Verifica si el atacante puede alcanzar al defensor con su rango actual.
     *
     * @param attacker barco atacante
     * @param defender barco defensor
     * @return true si el defensor está dentro del rango efectivo
     */
    public boolean isInRange(Ship attacker, Ship defender) {
        return attacker.getPosition().distanceTo(defender.getPosition()) <= attacker.getEffectiveRange();
    }
}