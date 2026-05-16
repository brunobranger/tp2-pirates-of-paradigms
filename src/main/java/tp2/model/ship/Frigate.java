package tp2.model.ship;

/**
 * Fragata: ágil, con mayor poder ofensivo y blindaje que la balandra.
 *
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
        return true; // afectado por Viento Favorable (+2 MOV)
    }
}