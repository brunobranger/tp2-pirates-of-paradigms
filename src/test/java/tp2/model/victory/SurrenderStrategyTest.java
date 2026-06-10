package tp2.model.victory;

import org.junit.jupiter.api.Test;
import tp2.model.board.Board;
import tp2.model.game.GameState;
import tp2.model.player.Faction;
import tp2.model.player.Player;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SurrenderStrategyTest {

    @Test
    void opponentWinsWhenPlayerSurrenders() {
        Player p1 = new Player("Uno", Faction.CRIMSON_BROTHERHOOD);
        Player p2 = new Player("Dos", Faction.ABYSS_CROWN);

        p1.surrender();
        GameState state = new GameState(new Board(5, 5), List.of(p1, p2));

        assertEquals(p2, new SurrenderStrategy().check(state));
    }
}