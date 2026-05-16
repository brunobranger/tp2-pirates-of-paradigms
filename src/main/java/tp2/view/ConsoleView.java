package tp2.view;

import java.util.List;
import java.util.Set;

import tp2.model.GameState;
import tp2.model.VictoryCondition;
import tp2.model.board.Position;
import tp2.model.combat.AttackResult;
import tp2.model.event.EventResult;
import tp2.model.event.GameEvent;
import tp2.model.player.Player;
import tp2.model.ship.Ship;

/**
 * Implementación de GameView por consola.
 * Se recomienda usar JLine para manejo avanzado de input (ver pom.xml).
 *
 * Principio: SRP — esta clase solo maneja presentación e input.
 * Principio: DIP — GameController depende de GameView, no de ConsoleView.
 *
 * Nota: los mensajes exactos quedan a criterio del grupo (enunciado).
 */
public class ConsoleView implements GameView {

    // inyectar Terminal de JLine si se usa
    // private final Terminal terminal;
    // private final LineReader reader;

    public ConsoleView() {
        // inicializar JLine Terminal y LineReader
    }

    // -------------------------------------------------------------------------
    // Setup
    // -------------------------------------------------------------------------

    @Override
    public String askMapFile() {
        // listar archivos disponibles en resources/maps/ y pedir selección
        return null;
    }

    @Override
    public String askFleetFile() {
        // listar archivos disponibles en resources/fleets/ y pedir selección
        return null;
    }

    @Override
    public Position askPortPosition(Player player, Set<Position> validPositions) {
        // mostrar tablero, indicar posiciones válidas, leer coordenadas
        return null;
    }

    @Override
    public Position askShipPlacementPosition(Player player, Ship ship, Set<Position> validPositions) {
        // ...
        return null;
    }

    // -------------------------------------------------------------------------
    // Menú y acciones
    // -------------------------------------------------------------------------

    @Override
    public int showMainMenu() {
        // imprimir opciones y leer int
        return 0;
    }

    @Override
    public Ship askSelectShip(Player player) {
        // numerar barcos activos, leer selección
        return null;
    }

    @Override
    public ShipAction askShipAction(Ship ship, GameState gameState) {
        // mostrar acciones disponibles según estado del barco (canMove, canAttack, isOnFire, etc.)
        return ShipAction.CANCEL;
    }

    @Override
    public Position askDestination(Set<Position> reachable) {
        // mostrar celdas alcanzables, leer coordenada en formato "fila,col"
        return null;
    }

    @Override
    public Ship askTarget(List<Ship> targets) {
        // numerar barcos enemigos en rango, leer selección
        return null;
    }

    // -------------------------------------------------------------------------
    // Estado
    // -------------------------------------------------------------------------

    @Override
    public void showBoard(GameState gameState) {
        // recorrer gameState.getBoard() e imprimir la grilla con símbolos
        // Ejemplo de leyenda del enunciado: ~~ mar, B1 barco j1, T tormenta, etc.
    }

    @Override
    public void showTurnSummary(GameState gameState) {
        // imprimir turno actual, jugador, recursos
    }

    @Override
    public void showFullStatus(GameState gameState) {
        // tablero + barcos + HP + recursos + tesoros (según Anexo del enunciado)
    }

    @Override
    public void showShipStats(Ship ship) {
        // nombre, HP, ATK, DEF, MOV, RNG, efectos activos
    }

    @Override
    public void showAttackResult(AttackResult result) {
        // imprimir result.toString() o formato propio
    }

    // -------------------------------------------------------------------------
    // Eventos
    // -------------------------------------------------------------------------

    @Override
    public void showEventAnnouncement(GameEvent event) {
        // imprimir nombre y descripción del evento
    }

    @Override
    public void showEventResult(EventResult result) {
        // imprimir entidades afectadas, duración, consecuencias
    }

    // -------------------------------------------------------------------------
    // Victoria
    // -------------------------------------------------------------------------

    @Override
    public void showVictory(Player winner, VictoryCondition condition) {
        // mostrar banner de victoria con nombre del ganador y condición
    }

    // -------------------------------------------------------------------------
    // Mensajes
    // -------------------------------------------------------------------------

    @Override
    public void showError(String message) { System.err.println("[ERROR] " + message); }

    @Override
    public void showMessage(String message) { System.out.println(message); }

    @Override
    public void showFirstPlayer(Player player) {
        // ...
    }

    @Override
    public void showNoTargets() { showMessage("No hay barcos enemigos en rango."); }

    @Override
    public void showInvalidOption() { showError("Opción inválida. Intente nuevamente."); }
}