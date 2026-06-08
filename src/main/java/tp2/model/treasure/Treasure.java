package tp2.model.treasure;

import tp2.model.player.Player;
import tp2.model.ship.Ship;

public interface Treasure {
    TreasureType getType();
    void onPickUp(Ship ship, Player player);
}
