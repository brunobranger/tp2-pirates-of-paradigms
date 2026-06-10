package tp2.model.game.system;

import tp2.model.board.Board;
import tp2.model.board.Position;
import tp2.model.game.GameState;
import tp2.model.ship.Ship;

public class MovementResolver {
    private final CellInteractionResolver cellInteractionResolver;

    public MovementResolver(CellInteractionResolver cellInteractionResolver) {
        this.cellInteractionResolver = cellInteractionResolver;
    }

    public void resolveMovement(GameState gameState, Ship ship, Position destination) {
        Board board = gameState.getBoard();
        board.moveShip(ship, destination);
        cellInteractionResolver.resolveEntry(board.getCellAt(destination), ship, gameState.getCurrentPlayer());
        board.applyMarineCurrentIfPresent(ship);
        ship.registerMove();
    }
}
