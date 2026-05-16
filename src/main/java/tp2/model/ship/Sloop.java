package tp2.model.ship;

/**
 * Balandra: barco ágil, particularmente rápido pero de bajo ataque.
 *
 * Estadísticas sugeridas (hipótesis del grupo — documentar en informe):
 *   HP=60, ATK=15, DEF=5, MOV=4, RNG=3
 *
 * Polimorfismo: sobreescribe getType() e isAgile().
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
        return true; // afectado por Viento Favorable (+2 MOV)
    }
}