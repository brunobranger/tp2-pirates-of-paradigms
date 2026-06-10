package tp2.model.event;

import org.junit.jupiter.api.Test;
import tp2.model.board.Board;
import tp2.model.game.GameState;
import tp2.model.player.Faction;
import tp2.model.player.Player;
import tp2.model.ship.effect.FavorableWindEffect;
import tp2.model.ship.effect.ReducedMovementEffect;
import tp2.model.ship.type.Flagship;
import tp2.model.ship.type.Sloop;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameEventTest {

    @Test
    void squallHasExpectedDuration() {
        GameEvent event = new Squall();
        assertEquals(1, event.getDurationInTurns());
    }

    @Test
    void mutinyHasExpectedDuration() {
        GameEvent event = new Mutiny();
        assertEquals(2, event.getDurationInTurns());
    }

    @Test
    void favorableWindHasExpectedDuration() {
        GameEvent event = new FavorableWind();
        assertEquals(1, event.getDurationInTurns());
    }

    @Test
    void floatingTreasureEventHasExpectedDuration() {
        GameEvent event = new FloatingTreasureEvent();
        assertEquals(3, event.getDurationInTurns());
    }

    @Test
    void deckFireIsInstantaneousEvent() {
        GameEvent event = new DeckFire();
        assertEquals(0, event.getDurationInTurns());
    }

    @Test
    void favorableWindAppliesEffectToShips() {
        Player player = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);
        Sloop ship = new Sloop("Balandra");
        player.addShip(ship);

        GameState gameState = new GameState(new Board(5, 5), List.of(player));

        new FavorableWind().apply(gameState);
        assertTrue(ship.getActiveEffects().stream().anyMatch(effect -> effect instanceof FavorableWindEffect));
    }

    @Test
    void squallAppliesReducedMovementEffect() {
        Player player = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);
        Sloop ship = new Sloop("Balandra");
        player.addShip(ship);

        GameState gameState = new GameState(new Board(5, 5), List.of(player));
        new Squall().apply(gameState);

        assertTrue(ship.getActiveEffects().stream().anyMatch(effect -> effect instanceof ReducedMovementEffect));
    }

    @Test
    void eventResultStoresProvidedData() {
        EventResult result = new EventResult("Evento", List.of("Barco"), 2, List.of("Consecuencia"));

        assertEquals("Evento", result.getEventName());
        assertEquals(List.of("Barco"), result.getAffectedEntities());
        assertEquals(2, result.getDurationTurns());
        assertEquals(List.of("Consecuencia"), result.getConsequences());
    }
}