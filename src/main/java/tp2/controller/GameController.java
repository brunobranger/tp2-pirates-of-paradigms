package tp2.controller;

import tp2.model.GameState;
import tp2.model.board.Board;
import tp2.model.board.Position;
import tp2.model.combat.AttackResult;
import tp2.model.combat.CombatSystem;
import tp2.model.event.EventManager;
import tp2.model.event.GameEvent;
import tp2.model.player.Player;
import tp2.model.ship.Ship;
import tp2.config.GameLoader;
import tp2.view.GameView;

/**
 * Controlador principal del juego. Patrón MVC: coordina Model (GameState)
 * y View (GameView) sin que ninguno de los dos se conozca entre sí.
 *
 * Responsabilidades:
 *   - Ejecutar el loop principal de turnos.
 *   - Delegar en GameState las modificaciones del modelo.
 *   - Delegar en GameView la presentación y el input.
 *   - Orquestar el sistema de combate y eventos.
 *
 * Principio: SRP — el controlador no imprime ni calcula daño; delega.
 * Principio: DIP — depende de GameView (interfaz), no de una consola específica.
 */
public class GameController {

    private GameState gameState;
    private final GameView view;
    private final CombatSystem combatSystem;
    private final EventManager eventManager;
    private final GameLoader loader;

    public GameController(GameView view, GameLoader loader) {
        this.view = view;
        this.loader = loader;
        this.eventManager = new EventManager();
        // combatSystem se inicializa tras cargar el board
        this.combatSystem = null; // inicializar con el board cargado
    }

    // -------------------------------------------------------------------------
    // Ciclo principal
    // -------------------------------------------------------------------------

    /**
     * Punto de entrada del juego. Orquesta: carga → setup → loop de turnos → fin.
     */
    public void start() {
        //
        // 1. gameState = loader.loadGame(view.askMapFile(), view.askFleetFile())
        // 2. setupPhase()
        // 3. decideFirstPlayer()
        // 4. while (!gameState.isGameOver()) { playTurn(); }
        // 5. view.showVictory(gameState.getWinner(), gameState.getVictoryCondition())
    }

    /**
     * Fase de setup: jugadores colocan puertos y barcos.
     */
    private void setupPhase() {
        //
        // Para cada jugador:
        //   1. view.askPortPosition() -> validar -> board.getCellAt(pos).assignPortOwner(player)
        //   2. Para cada barco en la flota: view.askShipPosition(ship) -> board.placeShip
        //   3. Colocar tesoros dinásticos en posición aleatoria válida
    }

    /**
     * Sorteo inicial: elige al azar quién comienza.
     */
    private void decideFirstPlayer() {
        // Random().nextBoolean() -> gameState.setCurrentPlayer(player1 o player2)
        // view.showFirstPlayer(gameState.getCurrentPlayer())
    }

    /**
     * Ejecuta el turno completo del jugador actual.
     */
    private void playTurn() {
        //
        // 1. view.showBoard(gameState)
        // 2. view.showTurnSummary(gameState)
        // 3. boolean turnEnded = false
        // 4. while (!turnEnded) { turnEnded = handlePlayerAction(); }
        // 5. applyEndOfTurnEffects()
        // 6. checkAndApplyEvent()
        // 7. gameState.checkVictoryConditions()
        // 8. gameState.switchTurn()
    }

    /**
     * Lee y ejecuta una acción del jugador actual.
     * Retorna true si el jugador eligió "Finalizar turno".
     *
     * @return true si el turno terminó
     */
    private boolean handlePlayerAction() {
        //
        // int option = view.showMainMenu()
        // return switch (option) {
        //     case 1 -> { showGameStatus(); yield false; }
        //     case 2 -> { handleShipAction(); yield false; }
        //     case 3 -> { handleSurrender(); yield true; }
        //     case 4 -> true; // Finalizar turno
        //     default -> { view.showInvalidOption(); yield false; }
        // }
        return false;
    }

    // -------------------------------------------------------------------------
    // Acciones de barco
    // -------------------------------------------------------------------------

    /**
     * Submenú: el jugador elige un barco propio y luego una acción sobre él.
     */
    private void handleShipAction() {
        //
        // 1. view.showFleet(currentPlayer) -> jugador elige barco
        // 2. view.showShipStats(ship)
        // 3. view.showShipActions(ship, gameState) -> jugador elige acción
        // 4. switch acción: MOVE, ATTACK, REPAIR, BUY_PORT, EXTINGUISH_FIRE, CANCEL
    }

    /**
     * Ejecuta el movimiento de un barco a una posición elegida por el jugador.
     *
     * @param ship barco a mover
     */
    private void handleMove(Ship ship) {
        //
        // 1. reachable = gameState.getBoard().getReachablePositions(ship.getPosition(), ship.getEffectiveMovement())
        // 2. view.showReachablePositions(reachable)
        // 3. Position dest = view.askDestination()
        // 4. if (!reachable.contains(dest)) -> view.showError, return
        // 5. gameState.getBoard().moveShip(ship, dest)
        // 6. gameState.getBoard().applyMarineCurrentIfPresent(ship)
        // 7. ship.registerMove()
        // 8. checkTreasurePickup(ship)
        // 9. checkPortEffects(ship)
    }

    /**
     * Ejecuta el ataque de un barco a un enemigo elegido por el jugador.
     *
     * @param attacker barco atacante
     */
    private void handleAttack(Ship attacker) {
        //
        // 1. opponent = gameState.getOpponentOf(currentPlayer)
        // 2. targets = gameState.getBoard().getTargetsInRange(attacker, attacker.getEffectiveRange(), opponent)
        // 3. if (targets.isEmpty()) -> view.showNoTargets(); return
        // 4. Ship target = view.askTarget(targets)
        // 5. AttackResult result = combatSystem.attack(attacker, target)
        // 6. view.showAttackResult(result)
        // 7. if (result.isDefenderSunk()) -> opponent.removeShip(target); rewardForSink()
        // 8. gameState.checkVictoryConditions()
    }

    /**
     * Repara un barco (en puerto propio = gratis; fuera = cuesta madera).
     *
     * @param ship barco a reparar
     */
    private void handleRepair(Ship ship) {
        // ...
    }

    /**
     * Compra un puerto sin dueño en la posición del barco.
     *
     * @param ship barco que compra el puerto
     */
    private void handleBuyPort(Ship ship) {
        // ...
    }

    /**
     * Apaga el incendio de un barco (cuesta madera o está en puerto propio).
     *
     * @param ship barco incendiado
     */
    private void handleExtinguishFire(Ship ship) {
        // ...
    }

    // -------------------------------------------------------------------------
    // Fin de turno y eventos
    // -------------------------------------------------------------------------

    /**
     * Aplica efectos de fin de turno: daño por incendio, tick de efectos, tesoros flotantes.
     */
    private void applyEndOfTurnEffects() {
        //
        // Para cada barco activo de ambos jugadores:
        //   ship.getActiveEffects().forEach(e -> e.applyEndOfTurnDamage(ship))
        //   ship.resetTurnState() [solo al inicio del propio turno, ver GameState.switchTurn]
        // gameState.cleanExpiredFloatingTreasures()
    }

    /**
     * Verifica si corresponde disparar un evento aleatorio y lo aplica.
     */
    private void checkAndApplyEvent() {
        //
        // GameEvent event = eventManager.checkAndGetEvent(gameState)
        // if (event != null) {
        //     view.showEventAnnouncement(event)
        //     EventResult result = event.apply(gameState)
        //     view.showEventResult(result)
        // }
    }

    /**
     * Chequea si el barco está sobre un tesoro (dinástico o flotante) y lo recoge.
     *
     * @param ship barco que se movió
     */
    private void checkTreasurePickup(Ship ship) {
        // ...
    }

    /**
     * Otorga recompensa en recursos al hundir un barco enemigo.
     */
    private void rewardForSink() {
        // currentPlayer.getResources().earnGold(ResourceCosts.SINK_SHIP_GOLD_REWARD)
    }

    private void showGameStatus() {
        // view.showFullStatus(gameState)
    }

    private void handleSurrender() {
        // gameState.getCurrentPlayer().surrender(); gameState.checkVictoryConditions()
    }
}