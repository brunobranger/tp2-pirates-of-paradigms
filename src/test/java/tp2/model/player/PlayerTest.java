package tp2.model.player;

import org.junit.jupiter.api.Test;
import tp2.model.ship.type.Flagship;
import tp2.model.ship.type.Sloop;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void playerWithoutFlagshipIsConsideredSunk() {
        Player player = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);
        assertTrue(player.isFlagshipSunk());
    }

    @Test
    void healthyFlagshipIsNotSunk() {
        Player player = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);

        Flagship flagship = new Flagship("Insignia");

        player.addShip(flagship);
        assertFalse(player.isFlagshipSunk());
    }

    @Test
    void sunkFlagshipIsDetected() {
        Player player = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);
        Flagship flagship = new Flagship("Insignia");

        player.addShip(flagship);
        flagship.receiveDamage(9999);

        assertTrue(player.isFlagshipSunk());
    }

    @Test
    void hasShipReturnsTrueForOwnedShip() {
        Player player = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);
        Sloop ship = new Sloop("Balandra");

        player.addShip(ship);
        assertTrue(player.hasShip(ship));
    }

    @Test
    void hasShipReturnsFalseForForeignShip() {
        Player player = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);

        Sloop ship = new Sloop("Balandra");
        assertFalse(player.hasShip(ship));
    }

    @Test
    void removeShipActuallyRemovesShipFromFleet() {
        Player player = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);

        Sloop ship = new Sloop("Balandra");
        player.addShip(ship);

        player.removeShip(ship);
        assertFalse(player.hasShip(ship));
    }

    @Test
    void removeShipThrowsIfShipIsNotPresent() {
        Player player = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);
        Sloop ship = new Sloop("Balandra");

        assertThrows(IllegalStateException.class, () -> player.removeShip(ship));
    }

    @Test
    void surrenderedPlayerIsMarkedAsSurrendered() {
        Player player = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);

        player.surrender();
        assertTrue(player.hasSurrendered());
    }

    @Test
    void addingNegativeGoldThrowsException() {
        Player player = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);
        assertThrows(IllegalArgumentException.class, () -> player.addGold(-1));
    }

    @Test
    void spendingNegativeGoldThrowsException() {
        Player player = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);

        assertThrows(IllegalArgumentException.class, () -> player.spendGold(-1));
    }
}