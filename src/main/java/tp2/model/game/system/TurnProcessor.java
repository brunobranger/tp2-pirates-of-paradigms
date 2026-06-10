package tp2.model.game.system;

import tp2.model.game.GameState;
import tp2.model.player.Player;
import tp2.model.ship.Ship;

public class TurnProcessor {
    private final PortService portService;

    public TurnProcessor(PortService portService) {
        this.portService = portService;
    }

    public void processEndOfTurn(GameState gameState) {
        Player endingPlayer = gameState.getCurrentPlayer();
        applyEndOfTurnEffects(endingPlayer);
        applyPortRepair(gameState, endingPlayer);
        checkVictory(gameState);
        cleanup(gameState);
    }

    private void applyEndOfTurnEffects(Player endingPlayer) {
        for (Ship ship : endingPlayer.getActiveFleet()) {
            ship.getActiveEffects().forEach(effect -> effect.applyEndOfTurnDamage(ship));
            ship.resetTurnState();
        }
    }

    private void applyPortRepair(GameState gameState, Player endingPlayer) {
        for (Ship ship : endingPlayer.getActiveFleet()) {
            portService.applyEndTurnRepairIfDocked(gameState.getBoard(), ship, endingPlayer);
        }
    }

    private void checkVictory(GameState gameState) {
        gameState.checkVictoryConditions();
    }

    private void cleanup(GameState gameState) {
        gameState.cleanExpiredFloatingTreasures();
    }
}
