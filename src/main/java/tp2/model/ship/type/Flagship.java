package tp2.model.ship.type;

import tp2.model.ship.Ship;
import tp2.model.ship.ShipType;

/**
 * Barco Insignia: pesado, mejores estadísticas que el resto.
 */
public class Flagship extends Ship {

    public Flagship(String name) {
        super(name, 250, 60, 50, 2, 6);
    }

    @Override
    public ShipType getType() {
        return ShipType.FLAGSHIP;
    }
}