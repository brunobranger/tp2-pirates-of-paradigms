package tp2.model.treasure;

import tp2.model.player.Player;
import tp2.model.ship.Ship;

public interface Treasure {
    /*
    Obtener el tipo de tesoro
     */
    TreasureType getType();

    /*
    Obtener la propiedad de ser agarrado.
     */
    void onPickUp(Ship ship, Player player);
}
