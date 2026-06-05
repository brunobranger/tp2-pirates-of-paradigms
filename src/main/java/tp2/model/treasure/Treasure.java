package tp2.model.treasure;

import tp2.model.player.Player;
import tp2.model.ship.Ship;

public interface Treasure {
    String getType(); // "DYNASTIC" o "FLOATING"
    void onPickUp(Ship ship, Player player);
}
