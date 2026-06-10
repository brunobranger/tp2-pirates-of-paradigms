package tp2.model.treasure;

import tp2.model.player.Player;
import tp2.model.ship.Ship;

public interface Treasure {
    /** @return tipo de tesoro. */
    TreasureType getType();

    /** Ejecuta el efecto del tesoro cuando un barco lo recoge. */
    void onPickUp(Ship ship, Player player);
}
