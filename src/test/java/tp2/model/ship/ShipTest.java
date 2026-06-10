package tp2.model.ship;

import org.junit.jupiter.api.Test;
import tp2.model.ship.effect.FavorableWindEffect;
import tp2.model.ship.effect.FireEffect;
import tp2.model.ship.effect.ReducedMovementEffect;
import tp2.model.ship.type.Sloop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias del comportamiento comun de los barcos:
 * danio, efectos temporales e incendio.
 */
class ShipTest {

    @Test
    void receiveDamageAppliesDefenseAndDoesNotDropBelowZero() {
        Sloop ship = new Sloop("Ligera");

        assertEquals(10, ship.receiveDamage(15));
        assertEquals(50, ship.getCurrentHp());

        ship.receiveDamage(1000);

        assertEquals(0, ship.getCurrentHp());
        assertTrue(ship.isSunk());
    }

    @Test
    void temporaryEffectsModifyStatsAndExpireOnReset() {
        Sloop ship = new Sloop("Ligera");
        ship.addEffect(new ReducedMovementEffect(1));
        ship.addEffect(new FavorableWindEffect(1, 2, 1));

        assertEquals(3, ship.getEffectiveMovement());
        assertEquals(4, ship.getEffectiveRange());

        ship.resetTurnState();

        assertEquals(4, ship.getEffectiveMovement());
        assertEquals(3, ship.getEffectiveRange());
        assertTrue(ship.getActiveEffects().isEmpty());
    }

    @Test
    void firePreventsAttackAndCanBeExtinguished() {
        Sloop ship = new Sloop("Ligera");
        ship.addEffect(new FireEffect());

        assertTrue(ship.isOnFire());
        assertFalse(ship.canAttack());

        ship.extinguishFire();

        assertFalse(ship.isOnFire());
        assertTrue(ship.canAttack());
    }

    @Test
    void shipCanMoveThenAttack() {
        Sloop ship = new Sloop("Nave");

        ship.registerMove();

        assertTrue(ship.canAttack());
    }

    @Test
    void shipCannotMoveAfterAttackWithoutFavorableWind() {
        Sloop ship = new Sloop("Nave");

        ship.registerAttack();

        assertFalse(ship.canMove());
    }

    @Test
    void shipCanMoveAfterAttackWithFavorableWind() {
        Sloop ship = new Sloop("Nave");

        ship.addEffect(
                new FavorableWindEffect(1, 2, 1)
        );

        ship.registerAttack();

        assertTrue(ship.canMove());
    }
}
