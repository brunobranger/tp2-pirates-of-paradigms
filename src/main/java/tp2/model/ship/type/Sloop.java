package tp2.model.ship.type;

import tp2.model.ship.Ship;
import tp2.model.ship.ShipType;

/**
 * Balandra: barco ágil, particularmente rápido pero de bajo ataque.
 */
public class Sloop extends Ship {

    public Sloop(String name) {
        super(name, 60, 15, 5, 4, 3);
    }

    @Override
    public ShipType getType() {
        return ShipType.SLOOP;
    }

    @Override
    public boolean isAgile() {
        return true;
    }
}