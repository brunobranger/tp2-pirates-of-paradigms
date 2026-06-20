package tp2.model.victory;

import org.junit.jupiter.api.Test;
import tp2.model.board.Board;
import tp2.model.game.GameState;
import tp2.model.player.Faction;
import tp2.model.player.Player;
import tp2.model.ship.type.Flagship;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlagshipSunkStrategyTest {

    @Test
    void returnsOpponentWhenFlagshipIsSunk() {
        Player p1 = new Player("Uno", Faction.CRIMSON_BROTHERHOOD);
        Player p2 = new Player("Dos", Faction.ABYSS_CROWN);

        Flagship flagship = new Flagship("Insignia");

        p1.addShip(flagship);

        flagship.receiveAttackDamage(9999);
        GameState state = new GameState(new Board(5, 5), List.of(p1, p2));

        Player winner = new FlagshipSunkStrategy().check(state);
        assertEquals(p2, winner);
    }

    @Test
    void returnsNullIfNobodyLostFlagship() {
        Board board = new Board(2, 2);

        Player p1 = new Player("Uno", Faction.CRIMSON_BROTHERHOOD);
        Player p2 = new Player("Dos", Faction.ABYSS_CROWN);

        p1.addShip(new Flagship("Flagship 1"));
        p2.addShip(new Flagship("Flagship 2"));

        GameState state = new GameState(board, List.of(p1, p2));

        FlagshipSunkStrategy strategy = new FlagshipSunkStrategy();

        assertNull(strategy.check(state));
    }
}