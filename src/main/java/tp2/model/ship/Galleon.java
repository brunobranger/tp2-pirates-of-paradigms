package tp2.model.ship;

/**
 * Galeón: barco pesado, lento pero resistente y de gran armamento.
 *
 * Estadísticas sugeridas: HP=150, ATK=40, DEF=30, MOV=2, RNG=5
 */
public class Galleon extends Ship {

    public Galleon(String name) {
        super(name, 150, 40, 30, 2, 5);
    }

    @Override
    public ShipType getType() {
        return ShipType.GALLEON;
    }
}