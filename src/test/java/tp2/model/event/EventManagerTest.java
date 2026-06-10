package tp2.model.event;

import org.junit.jupiter.api.Test;
import tp2.model.board.Board;
import tp2.model.game.GameState;
import tp2.model.player.Faction;
import tp2.model.player.Player;
import tp2.model.ship.type.Flagship;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventManagerTest {

    @Test
    void eventOccursEveryThirdCompletedTurn() {
        EventManager manager = EventManager.getInstance();

        Player player1 = new Player("Uno", Faction.CRIMSON_BROTHERHOOD);
        Player player2 = new Player("Dos", Faction.ABYSS_CROWN);

        player1.addShip(new Flagship("F1"));
        player2.addShip(new Flagship("F2"));

        GameState gameState = new GameState(new Board(5, 5), List.of(player1, player2));

        assertNull(manager.checkAndGetEvent(gameState));
        assertNull(manager.checkAndGetEvent(gameState));

        GameEvent event = manager.checkAndGetEvent(gameState);

        assertNotNull(event);
    }

    @Test
    void generatedEventBelongsToRegisteredEvents() {
        EventManager manager = EventManager.getInstance();

        Player player1 = new Player("Uno", Faction.CRIMSON_BROTHERHOOD);
        Player player2 = new Player("Dos", Faction.ABYSS_CROWN);

        player1.addShip(new Flagship("F1"));
        player2.addShip(new Flagship("F2"));

        GameState gameState = new GameState(new Board(5, 5), List.of(player1, player2));

        manager.checkAndGetEvent(gameState);
        manager.checkAndGetEvent(gameState);

        GameEvent event = manager.checkAndGetEvent(gameState);

        assertTrue(event instanceof Squall || event instanceof Mutiny || event instanceof FloatingTreasureEvent || event instanceof FavorableWind || event instanceof DeckFire);
    }
}