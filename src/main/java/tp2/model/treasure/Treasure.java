package tp2.model.treasure;

import tp2.model.player.Player;
import tp2.model.ship.Ship;

public interface Treasure {
    /** @return tipo de tesoro. */
    TreasureType getType();

    /** Ejecuta el efecto del tesoro cuando un barco lo recoge. */
    void onPickUp(Ship ship, Player player);

    /**
     * Calcula cuanta vida recuperaria el barco al recoger este tesoro.
     * Se usa para mostrar feedback sin preguntar por la clase concreta.
     */
    default int calculateHpRepairOnPickUp(Ship ship, int hpBeforePickUp) {
        return 0;
    }

    /**
     * Reporta la recoleccion del tesoro a traves de un colaborador abstracto.
     */
    default void reportCollection(TreasureCollectionReporter reporter, Ship ship,
                                  int goldGained, int hpRecovered) {
    }
}
