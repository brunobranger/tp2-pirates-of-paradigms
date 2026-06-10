package tp2.model.treasure;

import org.junit.jupiter.api.Test;
import tp2.model.board.Position;
import tp2.model.player.Faction;
import tp2.model.player.Player;
import tp2.model.ship.type.Sloop;

import static org.junit.jupiter.api.Assertions.*;

class DynasticTreasureTest {

    @Test
    void treasureStartsOnMap() {
        Player owner = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);
        DynasticTreasure treasure = new DynasticTreasure(owner, new Position(1, 1));

        assertTrue(treasure.isOnMap());
        assertFalse(treasure.isCarried());
    }

    @Test
    void pickupMakesTreasureCarried() {
        Player owner = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);
        DynasticTreasure treasure = new DynasticTreasure(owner, new Position(1, 1));
        Sloop ship = new Sloop("Nave");

        treasure.pickUp(ship);

        assertTrue(treasure.isCarried());
        assertEquals(ship, treasure.getCarrierTreasure());
    }

    @Test
    void dropForSinkLeavesTreasureFloating() {
        Player owner = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);
        DynasticTreasure treasure = new DynasticTreasure(owner, new Position(1, 1));

        treasure.dropForSink(new Position(3, 3));

        assertTrue(treasure.isFloating());
        assertEquals(new Position(3, 3), treasure.getPositionCarrierTreasure());
    }

    @Test
    void deliverLeavesTreasureOnPort() {
        Player owner = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);
        DynasticTreasure treasure = new DynasticTreasure(owner, new Position(1, 1));
        Position port = new Position(4, 4);

        treasure.deliver(port);

        assertEquals(port, treasure.getPositionCarrierTreasure());
        assertFalse(treasure.isFloating());
    }
}