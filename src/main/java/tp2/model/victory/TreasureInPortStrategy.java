package tp2.model.victory;

import tp2.model.board.Cell;
import tp2.model.board.Position;
import tp2.model.game.GameState;
import tp2.model.game.VictoryCondition;
import tp2.model.player.Player;
import tp2.model.ship.Ship;
import tp2.model.treasure.DynasticTreasure;

public class TreasureInPortStrategy implements VictoryStrategy {
    @Override
    public Player check(GameState state) {
        for (Player player : state.getPlayers()) {
            Player playerOpponent = state.getOpponentOf(player);
            DynasticTreasure treasureOpponent = playerOpponent.getOwnTreasure();

            if (treasureOpponent.isCaptured()) {
                Ship ship = treasureOpponent.getCarrierTreasure();
                Position position = ship.getPosition();
                Cell currentCell = state.getBoard().getCellAt(position);

                if (currentCell.isOwnedPortOf(player)) {
                    treasureOpponent.deliver(position);
                    return player;
                }
            }
        }
        return null;
    }

    @Override
    public VictoryCondition getConditionType() {
        return VictoryCondition.TREASURE_CAPTURED;
    }
}
