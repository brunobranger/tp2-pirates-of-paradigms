package tp2.controller.command;

import tp2.config.GameConfig;
import tp2.model.board.TerrainType;
import tp2.model.game.GameState;
import tp2.model.board.Position;
import tp2.model.ship.EffectType;
import tp2.model.ship.Ship;
import tp2.model.ship.effect.ReducedAttackEffect;
import tp2.model.ship.effect.ReducedMovementEffect;

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
        if (cell.getTerrain() == TerrainType.REEF) {
            ship.receiveDamage(GameConfig.REEF_DAMAGE);
        }

        if (cell.getTerrain() == TerrainType.STORM) {
            ship.addEffect(new ReducedMovementEffect(5)); //  cantidad de turnos?
        }

        if (cell.getTerrain() == TerrainType.FOG) {
            ship.addEffect(new ReducedMovementEffect(5));
            ship.addEffect(new ReducedAttackEffect(1, 0.4)); // por un turno y reduce el ataque un 40%
        }
    }
}
