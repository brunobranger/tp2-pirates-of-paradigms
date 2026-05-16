package tp2.view;

/**
 * Acciones disponibles para un barco en el menú de la vista.
 * La vista retorna un ShipAction; el controlador decide qué hacer.
 *
 * Principio: SRP — la vista no ejecuta la acción, solo la comunica.
 */
public enum ShipAction {
    MOVE,
    ATTACK,
    REPAIR,
    BUY_PORT,
    EXTINGUISH_FIRE,
    CANCEL
}