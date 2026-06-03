package tp2.model.game;

import org.junit.jupiter.api.Test;
import tp2.model.board.Board;
import tp2.model.player.Faction;
import tp2.model.player.Player;
import tp2.model.ship.type.Flagship;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias del ciclo de turnos y de las condiciones globales
 * administradas por GameState.
 */
class GameStateTest {

    @Test
    void switchTurnAlternatesPlayersAndIncrementsRoundAfterBothPlayed() {
        Player first = playerWithFlagship("Uno", Faction.CRIMSON_BROTHERHOOD);
        Player second = playerWithFlagship("Dos", Faction.ABYSS_CROWN);
        GameState state = new GameState(new Board(2, 2), List.of(first, second));

        state.switchTurn();
        assertEquals(second, state.getCurrentPlayer());
        assertEquals(1, state.getTurnNumber());

        state.switchTurn();
        assertEquals(first, state.getCurrentPlayer());
        assertEquals(2, state.getTurnNumber());
    }

    @Test
    void surrenderAwardsVictoryToOpponent() {
        Player first = playerWithFlagship("Uno", Faction.CRIMSON_BROTHERHOOD);
        Player second = playerWithFlagship("Dos", Faction.ABYSS_CROWN);
        GameState state = new GameState(new Board(2, 2), List.of(first, second));

        first.surrender();
        state.checkVictoryConditions();

        assertTrue(state.isGameOver());
        assertEquals(second, state.getPlayerWinner());
        assertEquals(VictoryCondition.SURRENDER, state.getVictoryCondition());
        assertFalse(second.hasSurrendered());
    }

    private Player playerWithFlagship(String name, Faction faction) {
        Player player = new Player(name, faction);
        player.addShip(new Flagship("Insignia " + name));
        return player;
    }
}
