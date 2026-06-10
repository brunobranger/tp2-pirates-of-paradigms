package tp2.controller.command;

import tp2.model.game.GameState;
import tp2.model.game.system.MovementResolver;
import tp2.model.board.Position;
import tp2.model.ship.Ship;

public class MoveCommand implements ShipCommand {

    private final Ship ship;
    private final Position destination;
    private final MovementResolver movementResolver;

    public MoveCommand(Ship ship, Position destination, MovementResolver movementResolver) {
        this.ship = ship;
        this.destination = destination;
        this.movementResolver = movementResolver;
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
        movementResolver.resolveMovement(gameState, ship, destination);
    }
}
