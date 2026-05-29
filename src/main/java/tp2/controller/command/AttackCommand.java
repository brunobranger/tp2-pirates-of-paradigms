package tp2.controller.command;

import tp2.model.game.GameState;
import tp2.model.combat.CombatSystem;
import tp2.model.ship.Ship;

public class AttackCommand implements ShipCommand {

    private final Ship attacker;
    private final Ship target;
    private final CombatSystem combatSystem;

    public AttackCommand(Ship attacker, Ship target, CombatSystem combatSystem) {
        this.attacker = attacker;
        this.target = target;
        this.combatSystem = combatSystem;
    }

    @Override
    public boolean canExecute(GameState gameState) {
        if (!attacker.canAttack()) return false;
        return combatSystem.isInRange(attacker, target);
    }

    @Override
    public void execute(GameState gameState) {
        var result = combatSystem.attack(attacker, target);

        if (result.isDefenderSunk()) {
            var opponent = gameState.getOpponentOf(gameState.getCurrentPlayer());
            opponent.removeShip(target);
            gameState.getBoard().sinkShip(target);
            gameState.getCurrentPlayer().getResources()
                    .earnGold(tp2.model.resource.ResourceCosts.SINK_SHIP_GOLD_REWARD);
        }

        gameState.checkVictoryConditions();
    }
}
