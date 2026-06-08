package tp2.model.resource;

/**
 * Constantes de costo de recursos para todas las acciones del juego.
 * Centralizar aquí facilita el balanceo sin buscar números mágicos en el código.
 *
 * Hipótesis: valores iniciales sugeridos — documentar en informe.
 */
public final class ResourceCosts {

    private ResourceCosts() {}

    // Oro
    public static final int BUY_PORT_GOLD        = 100;
    public static final int RESOLVE_MUTINY_GOLD  = 50;

    // Madera
    public static final int REPAIR_OUT_OF_PORT_WOOD = 30;
    public static final int EXTINGUISH_FIRE_WOOD     = 20;

    // Recompensas
    public static final int SINK_SHIP_GOLD_REWARD    = 40;
    public static final int FLOATING_TREASURE_GOLD   = 10;
    public static final int FLOATING_TREASURE_HP_REPAIR = 25;

    // Iniciales por jugador
    public static final int INITIAL_GOLD = 200;
    public static final int INITIAL_WOOD = 150;
}