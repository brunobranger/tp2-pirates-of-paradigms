package tp2.controller.command;

import tp2.model.game.GameState;
import tp2.model.board.Position;
import tp2.model.ship.Ship;

public class MoveCommand implements ShipCommand {

    private final Ship ship;
    private final Position destination;

    public MoveCommand(Ship ship, Position destination) {
        this.ship = ship;
        this.destination = destination;
    }

    @Override
    public boolean canExecute(GameState gameState) {
        if (!ship.canMove()) return false;
        var reachable = gameState.getBoard().getReachablePositions(
                ship.getPosition(), ship.getEffectiveMovement()
        );
        return reachable.contains(destination);
    }

    @Override
    public void execute(GameState gameState) {
        gameState.getBoard().moveShip(ship, destination);
        gameState.getBoard().applyMarineCurrentIfPresent(ship);
        ship.registerMove();

        // Si el barco llegó a una celda con tesoro flotante, lo recoge
        var cell = gameState.getBoard().getCellAt(destination);
        if (cell.getFloatingTreasure() != null) {
            var treasure = cell.pickUpTreasure();
            ship.pickUpTreasure(treasure);
        }

        // Si el barco llegó a un arrecife, recibe daño
        if (cell.getTerrain() == tp2.model.board.TerrainType.REEF) {
            ship.receiveDamage(tp2.config.GameConfig.REEF_DAMAGE);
        }
    }
}
