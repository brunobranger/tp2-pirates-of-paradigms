package tp2.controller.command;

import tp2.model.game.GameState;
import tp2.model.ship.Ship;
import tp2.config.GameConfig;
import tp2.model.resource.ResourceCosts;

public class RepairCommand implements ShipCommand {

    private final Ship ship;

    public RepairCommand(Ship ship) {
        this.ship = ship;
    }

    @Override
    public boolean canExecute(GameState gameState) {
        if (ship.isSunk()) return false;
        var cell = gameState.getBoard().getCellAt(ship.getPosition());
        boolean inOwnPort = cell.isOwnedPortOf(gameState.getCurrentPlayer());
        boolean hasWood = gameState.getCurrentPlayer().getResources()
                .hasEnoughWood(ResourceCosts.REPAIR_OUT_OF_PORT_WOOD);
        return inOwnPort || hasWood;
    }

    @Override
    public void execute(GameState gameState) {
        var cell = gameState.getBoard().getCellAt(ship.getPosition());
        var resources = gameState.getCurrentPlayer().getResources();

        if (cell.isOwnedPortOf(gameState.getCurrentPlayer())) {
            ship.repairHp(GameConfig.REPAIR_IN_PORT_HP);
        } else {
            resources.spendWood(ResourceCosts.REPAIR_OUT_OF_PORT_WOOD);
            ship.repairHp(GameConfig.REPAIR_OUT_PORT_HP);
        }
    }
}