package tp2.model.treasure;

import org.junit.jupiter.api.Test;
import tp2.model.board.Position;
import tp2.model.player.Faction;
import tp2.model.player.Player;
import tp2.model.ship.type.Sloop;

import static org.junit.jupiter.api.Assertions.*;

class TreasureIntegrationTest {

    @Test
    void dynasticTreasurePickupAssignsCarrierAndMarksAsCarried() {
        Player owner = new Player("Owner", Faction.CRIMSON_BROTHERHOOD);
        Player enemy = new Player("Enemy", Faction.ABYSS_CROWN);

        DynasticTreasure treasure = new DynasticTreasure(owner, new Position(2, 2));

        Sloop ship = new Sloop("Corsario");
        enemy.addShip(ship);

        treasure.onPickUp(ship, enemy);

        assertTrue(treasure.isCarried());
        assertEquals(ship, treasure.getCarrierTreasure());
    }

    @Test
    void enemyPickupMarksTreasureAsCaptured() {
        Player owner = new Player("Owner", Faction.CRIMSON_BROTHERHOOD);
        Player enemy = new Player("Enemy", Faction.ABYSS_CROWN);

        DynasticTreasure treasure = new DynasticTreasure(owner, new Position(2, 2));

        Sloop ship = new Sloop("Corsario");
        enemy.addShip(ship);

        treasure.onPickUp(ship, enemy);

        assertTrue(treasure.isCaptured());
    }

    @Test
    void ownerPickupDoesNotMarkTreasureAsCaptured() {
        Player owner = new Player("Owner", Faction.CRIMSON_BROTHERHOOD);

        DynasticTreasure treasure = new DynasticTreasure(owner, new Position(2, 2));

        Sloop ship = new Sloop("Defensor");
        owner.addShip(ship);

        treasure.onPickUp(ship, owner);

        assertFalse(treasure.isCaptured());
    }

    @Test
    void treasureDroppedAfterSinkBecomesFloating() {
        Player owner = new Player("Owner", Faction.CRIMSON_BROTHERHOOD);
        Player enemy = new Player("Enemy", Faction.ABYSS_CROWN);

        DynasticTreasure treasure = new DynasticTreasure(owner, new Position(2, 2));

        Sloop ship = new Sloop("Corsario");
        enemy.addShip(ship);

        treasure.onPickUp(ship, enemy);

        Position sinkPosition = new Position(4, 5);

        treasure.dropForSink(sinkPosition);

        assertTrue(treasure.isFloating());
        assertNull(treasure.getCarrierTreasure());
        assertEquals(sinkPosition, treasure.getPositionCarrierTreasure());
    }

    @Test
    void deliveredTreasureStopsBeingCarried() {
        Player owner = new Player("Owner", Faction.CRIMSON_BROTHERHOOD);
        Player enemy = new Player("Enemy", Faction.ABYSS_CROWN);

        DynasticTreasure treasure = new DynasticTreasure(owner, new Position(2, 2));

        Sloop ship = new Sloop("Corsario");
        enemy.addShip(ship);

        treasure.onPickUp(ship, enemy);

        Position port = new Position(0, 0);

        treasure.deliver(port);

        assertFalse(treasure.isCarried());
        assertFalse(treasure.isFloating());
        assertEquals(port, treasure.getPositionCarrierTreasure());
    }
}