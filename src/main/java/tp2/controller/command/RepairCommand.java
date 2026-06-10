package tp2.controller.command;

import tp2.model.game.GameState;
import tp2.model.game.system.PortService;
import tp2.model.ship.Ship;
import tp2.config.GameConfig;
import tp2.model.resource.ResourceCosts;

public class RepairCommand implements ShipCommand {

    private final Ship ship;
    private final PortService portService;

    public RepairCommand(Ship ship, PortService portService) {
        this.ship = ship;
        this.portService = portService;
    }

    @Override
    public boolean canExecute(GameState gameState) {
        if (ship.isSunk()) return false;
        boolean inOwnPort = portService.isShipInCurrentPlayerPort(gameState, ship);
        boolean hasWood = gameState.getCurrentPlayer().getResources()
                .hasEnoughWood(ResourceCosts.REPAIR_OUT_OF_PORT_WOOD);
        return inOwnPort || hasWood;
    }

    @Override
    public void execute(GameState gameState) {
        var resources = gameState.getCurrentPlayer().getResources();

        if (portService.isShipInCurrentPlayerPort(gameState, ship)) {
            ship.repairHp(GameConfig.REPAIR_IN_PORT_HP);
        } else {
            resources.spendWood(ResourceCosts.REPAIR_OUT_OF_PORT_WOOD);
            ship.repairHp(GameConfig.REPAIR_OUT_PORT_HP);
        }
    }
}
