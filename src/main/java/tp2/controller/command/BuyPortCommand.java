package tp2.controller.command;

import tp2.model.game.GameState;
import tp2.model.game.system.PortService;
import tp2.model.ship.Ship;
import tp2.model.resource.ResourceCosts;

public class BuyPortCommand implements ShipCommand {

    private final Ship ship;
    private final PortService portService;

    public BuyPortCommand(Ship ship, PortService portService) {
        this.ship = ship;
        this.portService = portService;
    }

    @Override
    public boolean canExecute(GameState gameState) {
        boolean isUnownedPort = portService.isUnownedPortAtShipPosition(gameState, ship);
        boolean hasGold = gameState.getCurrentPlayer().hasEnoughGold(ResourceCosts.BUY_PORT_GOLD);
        return isUnownedPort && hasGold;
    }

    @Override
    public void execute(GameState gameState) {
        gameState.getCurrentPlayer().spendGold(ResourceCosts.BUY_PORT_GOLD);
        portService.assignPortToCurrentPlayer(gameState, ship);
    }
}
