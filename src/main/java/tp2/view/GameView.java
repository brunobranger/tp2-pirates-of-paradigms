package tp2.view;

import tp2.model.game.GameState;
import tp2.model.game.VictoryCondition;
import tp2.model.board.Position;
import tp2.model.combat.AttackResult;
import tp2.model.event.EventResult;
import tp2.model.event.GameEvent;
import tp2.model.player.Player;
import tp2.model.ship.Ship;

import java.util.List;
import java.util.Set;

/**
 * Interfaz de la Vista en el patrón MVC/MVP.
 */
public interface GameView {

    // -------------------------------------------------------------------------
    // Setup
    // -------------------------------------------------------------------------

    /** Pide al usuario seleccionar el archivo de mapa. */
    String askMapFile();

    /** Pide al usuario seleccionar el archivo de configuración de flotas. */
    String askFleetFile();

    /** Pide a un jugador dónde colocar su puerto inicial. */
    Position askPortPosition(Player player, Set<Position> validPositions, GameState gameState);

    /** Pide a un jugador dónde colocar un barco al inicio de la partida. */
    Position askShipPlacementPosition(Player player, Ship ship, Set<Position> validPositions, GameState gameState);

    // -------------------------------------------------------------------------
    // Menú principal y acciones
    // -------------------------------------------------------------------------

    /**
     * Muestra el menú principal y retorna la opción elegida.
     * Opciones: 1=Ver estado, 2=Usar barco, 3=Rendirse.
     */
    int showMainMenu();

    /**
     * Muestra la lista de barcos propios y retorna el elegido.
     *
     * @param player jugador actual
     * @return barco seleccionado
     */
    Ship askSelectShip(Player player);

    /**
     * Muestra las acciones disponibles para un barco y retorna la acción elegida.
     *
     * @param ship      barco seleccionado
     * @param gameState estado actual (para saber qué acciones están disponibles)
     * @return acción elegida (enum ShipAction)
     */
    ShipAction askShipAction(Ship ship, GameState gameState);

    /**
     * Muestra las posiciones alcanzables y pide al jugador elegir una.
     *
     * @param reachable conjunto de posiciones válidas
     * @return posición elegida, o null si cancela
     */
    Position askDestination(Set<Position> reachable, GameState gameState);

    /**
     * Muestra los barcos enemigos en rango y pide elegir un objetivo.
     *
     * @param targets barcos atacables
     * @return barco objetivo elegido, o null si cancela
     */
    Ship askTarget(List<Ship> targets);

    // -------------------------------------------------------------------------
    // Presentación del estado
    // -------------------------------------------------------------------------

    /** Dibuja el tablero en consola con la leyenda. */
    void showBoard(GameState gameState);

    /** Muestra resumen del turno actual: turno, jugador, recursos. */
    void showTurnSummary(GameState gameState);

    /** Muestra el estado completo de la partida (ver Anexo del enunciado). */
    void showFullStatus(GameState gameState);

    /** Muestra las estadísticas de un barco. */
    void showShipStats(Ship ship);

    /** Muestra las posiciones alcanzables para que el jugador elija destino. */
    void showReachablePositions(Set<Position> reachable);

    /** Muestra el resultado de un ataque. */
    void showAttackResult(AttackResult result);

    // -------------------------------------------------------------------------
    // Eventos
    // -------------------------------------------------------------------------

    /** Anuncia que un evento está por ocurrir. */
    void showEventAnnouncement(GameEvent event);

    /** Muestra las consecuencias de un evento ya aplicado. */
    void showEventResult(EventResult result);

    // -------------------------------------------------------------------------
    // Victoria / derrota
    // -------------------------------------------------------------------------

    /** Muestra la pantalla de fin de partida. */
    void showVictory(Player winner, VictoryCondition condition);

    void showInitShipPlace();

    // -------------------------------------------------------------------------
    // Mensajes de error / feedback
    // -------------------------------------------------------------------------

    void showError(String message);
    void showMessage(String message);
    void showFirstPlayer(Player player);
    void showNoTargets();
    void showInvalidOption();
    void showCellInvalid();
}