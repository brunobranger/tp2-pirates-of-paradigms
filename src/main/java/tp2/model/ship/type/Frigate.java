package tp2.model.ship.type;

import tp2.model.ship.Ship;
import tp2.model.ship.ShipType;

/**
 * Fragata: ágil, con mayor poder ofensivo y blindaje que la balandra.
 * Estadísticas sugeridas: HP=80, ATK=25, DEF=15, MOV=3, RNG=4
 */
public class Frigate extends Ship {

    public Frigate(String name) {
        super(name, 80, 25, 15, 3, 4);
    }

    @Override
    public ShipType getType() {
        return ShipType.FRIGATE;
    }

    @Override
    public boolean isAgile() {
        return true;
    }
}