package tp2.model.victory;

import tp2.model.game.GameState;
import tp2.model.game.VictoryCondition;
import tp2.model.player.Player;

public class FlagshipSunkStrategy implements VictoryStrategy {
    @Override
    public Player check(GameState state) {
        for (Player player : state.getPlayers()) {
            if (player.isFlagshipSunk()) {
                return state.getOpponentOf(player);
            }
        }
        return null;
    }

    @Override
    public VictoryCondition getConditionType() {
        return VictoryCondition.FLAGSHIP_SUNK;
    }
}
