package tp2.model.ship;

/**
 * Barco Insignia: pesado, mejores estadísticas que el resto.
 * Su hundimiento equivale a la derrota del jugador (condición de victoria rival).
 *
 * Estadísticas sugeridas: HP=250, ATK=60, DEF=50, MOV=2, RNG=6
 *
 * Nota de diseño: la condición de victoria no vive acá (SRP), sino en GameState.
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