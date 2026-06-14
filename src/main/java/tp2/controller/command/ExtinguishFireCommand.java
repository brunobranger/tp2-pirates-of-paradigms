package tp2.controller.command;

import tp2.model.game.GameState;
import tp2.model.game.system.PortService;
import tp2.model.ship.Ship;
import tp2.model.resource.ResourceCosts;

public class ExtinguishFireCommand implements ShipCommand {

    private final Ship ship;
    private final PortService portService;

    public ExtinguishFireCommand(Ship ship, PortService portService) {
        this.ship = ship;
        this.portService = portService;
    }

    @Override
    public boolean canExecute(GameState gameState) {
        if (!ship.isOnFire()) return false;
        boolean inOwnPort = portService.isShipInCurrentPlayerPort(gameState, ship);
        boolean hasWood = gameState.getCurrentPlayer().hasEnoughWood(ResourceCosts.EXTINGUISH_FIRE_WOOD);
        return inOwnPort || hasWood;
    }

    @Override
    public void execute(GameState gameState) {
        if (portService.isShipInCurrentPlayerPort(gameState, ship)) {
            ship.extinguishFire();
        } else {
            gameState.getCurrentPlayer().spendWood(ResourceCosts.EXTINGUISH_FIRE_WOOD);
            ship.extinguishFire();
        }
    }
}
