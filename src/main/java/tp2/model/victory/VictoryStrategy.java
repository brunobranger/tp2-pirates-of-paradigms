package tp2.model.victory;

import tp2.model.game.GameState;
import tp2.model.game.VictoryCondition;
import tp2.model.player.Player;

public interface VictoryStrategy {
    /**
     * Permite obtener la instancia del juego actual
     * @param state estado del juego actual
     * @return puede ser null (si nadie ha ganado aun) o Player
     */
    Player check(GameState state);

    /**
     * Permite obtener el tipo de condicion para la Victoria
     * @return tipo de victoria
     */
    VictoryCondition getConditionType();
}
