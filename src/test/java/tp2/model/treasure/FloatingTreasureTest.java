package tp2.model.treasure;

import org.junit.jupiter.api.Test;
import tp2.model.board.Position;

import static org.junit.jupiter.api.Assertions.*;

class FloatingTreasureTest {

    @Test
    void treasureExpiresAfterConfiguredTurns() {
        FloatingTreasure treasure = new FloatingTreasure(new Position(1, 1), 3);

        treasure.tick();
        treasure.tick();
        treasure.tick();

        assertTrue(treasure.isGone());
    }

    @Test
    void claimMarksTreasureAsGone() {
        FloatingTreasure treasure = new FloatingTreasure(new Position(1, 1), 3);

        treasure.claim();

        assertTrue(treasure.isClaimed());
        assertTrue(treasure.isGone());
    }

    @Test
    void treasureInitiallyIsNotGone() {
        FloatingTreasure treasure = new FloatingTreasure(new Position(1, 1), 3);

        assertFalse(treasure.isGone());
    }
}