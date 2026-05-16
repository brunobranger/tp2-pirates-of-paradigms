package tp2.config;

/**
 * Constantes de configuración del juego.
 * Centralizar aquí facilita ajustar balance sin buscar números mágicos.
 *
 * Hipótesis: valores iniciales — documentar en informe.
 */
public final class GameConfig {

    private GameConfig() {}

    // Tablero
    public static final int DEFAULT_BOARD_ROWS = 10;
    public static final int DEFAULT_BOARD_COLS = 10;

    // Puerto
    public static final int PORT_REPAIR_AMOUNT = 20; // HP recuperado al finalizar turno en puerto propio

    // Arrecife
    public static final int REEF_DAMAGE = 15; // daño al entrar en casilla de arrecife

    // Niebla
    public static final int FOG_RANGE_REDUCTION = 2; // casillas que se restan al rango en niebla

    // Reparación
    public static final int REPAIR_IN_PORT_HP   = 30;
    public static final int REPAIR_OUT_PORT_HP  = 15; // cuesta madera

    // Kraken
    public static final int KRAKEN_DURATION_TURNS = 3;

    // Recursos iniciales (también en ResourceCosts, se unifican aquí para config)
    public static final int INITIAL_GOLD = 200;
    public static final int INITIAL_WOOD = 150;
}