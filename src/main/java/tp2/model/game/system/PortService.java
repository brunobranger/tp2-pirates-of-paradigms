package tp2.model.game.system;

import tp2.config.GameConfig;
import tp2.model.board.Board;
import tp2.model.board.Cell;
import tp2.model.board.Position;
import tp2.model.board.TerrainType;
import tp2.model.game.GameState;
import tp2.model.player.Player;
import tp2.model.ship.Ship;

public class PortService {
    public void placeInitialPort(Board board, Player player, Position portPosition) {
        Cell portCell = board.getCellAt(portPosition);
        portCell.setTerrain(TerrainType.PORT);
        portCell.assignPortOwner(player);
        player.setHomePortPosition(portPosition);
        board.placeShip(player.getFlagship(), portPosition);
    }

    public void placeInitialShip(Board board, Ship ship, Position shipPosition) {
        board.placeShip(ship, shipPosition);
    }

    public boolean isShipInCurrentPlayerPort(GameState gameState, Ship ship) {
        return boardCellOfShip(gameState.getBoard(), ship).isOwnedPortOf(gameState.getCurrentPlayer());
    }

    public boolean isUnownedPortAtShipPosition(GameState gameState, Ship ship) {
        return boardCellOfShip(gameState.getBoard(), ship).isUnownedPort();
    }

    public void assignPortToCurrentPlayer(GameState gameState, Ship ship) {
        boardCellOfShip(gameState.getBoard(), ship).assignPortOwner(gameState.getCurrentPlayer());
    }

    public void applyEndTurnRepairIfDocked(Board board, Ship ship, Player player) {
        if (boardCellOfShip(board, ship).isOwnedPortOf(player)) {
            ship.repairHp(GameConfig.PORT_REPAIR_AMOUNT);
        }
    }

    private Cell boardCellOfShip(Board board, Ship ship) {
        return board.getCellAtShipPosition(ship);
    }
}
