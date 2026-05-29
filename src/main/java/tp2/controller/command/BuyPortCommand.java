package tp2.controller.command;

import tp2.model.game.GameState;
import tp2.model.ship.Ship;
import tp2.model.resource.ResourceCosts;

public class BuyPortCommand implements ShipCommand {

    private final Ship ship;

    public BuyPortCommand(Ship ship) {
        this.ship = ship;
    }

    @Override
    public boolean canExecute(GameState gameState) {
        var cell = gameState.getBoard().getCellAt(ship.getPosition());
        boolean isUnownedPort = cell.isUnownedPort();
        boolean hasGold = gameState.getCurrentPlayer().getResources()
                .hasEnoughGold(ResourceCosts.BUY_PORT_GOLD);
        return isUnownedPort && hasGold;
    }

    @Override
    public void execute(GameState gameState) {
        var cell = gameState.getBoard().getCellAt(ship.getPosition());
        gameState.getCurrentPlayer().getResources()
                .spendGold(ResourceCosts.BUY_PORT_GOLD);
        cell.assignPortOwner(gameState.getCurrentPlayer());
    }
}