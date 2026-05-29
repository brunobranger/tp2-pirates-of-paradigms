package tp2.controller.command;

import tp2.model.GameState;
import tp2.model.ship.Ship;
import tp2.model.resource.ResourceCosts;

public class ExtinguishFireCommand implements ShipCommand {

    private final Ship ship;

    public ExtinguishFireCommand(Ship ship) {
        this.ship = ship;
    }

    @Override
    public boolean canExecute(GameState gameState) {
        if (!ship.isOnFire()) return false;
        var cell = gameState.getBoard().getCellAt(ship.getPosition());
        boolean inOwnPort = cell.isOwnedPortOf(gameState.getCurrentPlayer());
        boolean hasWood = gameState.getCurrentPlayer().getResources()
                .hasEnoughWood(ResourceCosts.EXTINGUISH_FIRE_WOOD);
        return inOwnPort || hasWood;
    }

    @Override
    public void execute(GameState gameState) {
        var cell = gameState.getBoard().getCellAt(ship.getPosition());

        if (cell.isOwnedPortOf(gameState.getCurrentPlayer())) {
            ship.extinguishFire(); // gratis en puerto propio
        } else {
            gameState.getCurrentPlayer().getResources()
                    .spendWood(ResourceCosts.EXTINGUISH_FIRE_WOOD);
            ship.extinguishFire();
        }
    }
}