package tp2.controller;

import tp2.controller.command.*;
import tp2.model.board.Board;
import tp2.model.game.GameState;
import tp2.model.board.Position;
import tp2.model.combat.CombatSystem;
import tp2.model.event.EventManager;
import tp2.model.event.GameEvent;
import tp2.model.game.system.CellInteractionResolver;
import tp2.model.game.system.MovementResolver;
import tp2.model.game.system.PortService;
import tp2.model.game.system.TurnProcessor;
import tp2.model.board.terrain.TerrainRegistry;
import tp2.model.player.Player;
import tp2.model.ship.Ship;
import tp2.view.GameView;
import tp2.view.ShipAction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static tp2.model.ship.ShipType.FLAGSHIP;
import static tp2.util.RandomUtils.coinFlip; // import estatico de Coin

/**
 * Controlador principal del juego. Patrón MVC: coordina Model (GameState)
 * y View (GameView) sin que ninguno de los dos se conozca entre sí.

 * Responsabilidades:
 *   - Ejecutar el loop principal de turnos.
 *   - Delegar en GameState las modificaciones del modelo.
 *   - Delegar en GameView la presentación y el input.
 *   - Orquestar el sistema de combate y eventos.
 * Principio: SRP — el controlador no imprime ni calcula daño; delega.
 * Principio: DIP — depende de GameView (interfaz), no de una consola específica.
 * Patrón Command: cada acción del jugador se encapsula en un ShipCommand.
 */
public class GameController {

    private final GameState gameState;
    private final GameView view;
    private final CombatSystem combatSystem;
    private final EventManager eventManager;
    private final MovementResolver movementResolver;
    private final TurnProcessor turnProcessor;
    private final PortService portService;

    public GameController(GameState gameState, GameView view,
                          CombatSystem combatSystem, EventManager eventManager) {
        PortService defaultPortService = new PortService();
        this.gameState = gameState;
        this.view = view;
        this.combatSystem = combatSystem;
        this.eventManager = eventManager;
        this.movementResolver = new MovementResolver(new CellInteractionResolver(TerrainRegistry.defaultRegistry()));
        this.turnProcessor = new TurnProcessor(defaultPortService);
        this.portService = defaultPortService;
    }

    public GameController(GameState gameState, GameView view,
                          CombatSystem combatSystem, EventManager eventManager,
                          MovementResolver movementResolver, TurnProcessor turnProcessor,
                          PortService portService) {
        this.gameState = gameState;
        this.view = view;
        this.combatSystem = combatSystem;
        this.eventManager = eventManager;
        this.movementResolver = movementResolver;
        this.turnProcessor = turnProcessor;
        this.portService = portService;
    }

    // -------------------------------------------------------------------------
    // Ciclo principal
    // -------------------------------------------------------------------------

    /**
     * Inicia el loop principal del juego.
     * Alterna turnos hasta que haya un ganador.
     */
    public void start() {
        decideFirstPlayer();
        executeInitialDeployment();

        while (!gameState.isGameOver()) {
            playTurn();
        }

        view.showVictory(gameState.getPlayerWinner(), gameState.getVictoryCondition());
    }
    /**
     * Sorteo inicial: decide al azar quién comienza.
     */
    private void decideFirstPlayer() {
        List<Player> players = gameState.getPlayers();

        Player firstPlayer = coinFlip() ? players.get(1) : players.get(0);
        gameState.setCurrentPlayer(firstPlayer);
        view.showFirstPlayer(gameState.getCurrentPlayer());
    }

    /**
     * Ejecuta el turno completo del jugador actual.
     * El jugador puede realizar acciones hasta que elija finalizar el turno.
     */
    private void playTurn() {
        view.showBoard(gameState);
        view.showTurnSummary(gameState);

        boolean turnEnded = false;
        while (!turnEnded) {
            turnEnded = handleMainMenuOption();
        }

        if (gameState.isGameOver()) {
            return;
        }

        turnProcessor.processEndOfTurn(gameState);
        checkAndApplyEvent();
        gameState.checkVictoryConditions();
        gameState.switchTurn();
    }

    /**
     * Implementa puerto y barcos iniciales.
     */
    private void executeInitialDeployment() {
        view.showInitShipPlace();
        Board board = gameState.getBoard();

        for (Player player : gameState.getPlayers()) {
            Set<Position> validPositions = new HashSet<>(board.getFreeNavigablePositions());
            Position portPos = view.askPortPosition(player, validPositions, gameState);
            portService.placeInitialPort(board, player, portPos);

            for (Ship ship : player.getActiveFleet()) {
                if (ship.getType().equals(FLAGSHIP)) {
                    continue;
                }

                Set<Position> validPositionsForShip = new HashSet<>(board.getFreeNavigablePositions());
                Position shipPos = view.askShipPlacementPosition(player, ship, validPositionsForShip, gameState);
                portService.placeInitialShip(board, ship, shipPos);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Menú principal
    // -------------------------------------------------------------------------

    /**
     * Muestra el menú principal y maneja la opción elegida.
     * Retorna true si el jugador eligió finalizar el turno.
     */
    private boolean handleMainMenuOption() {
        int option = view.showMainMenu();
        return switch (option) {
            case 1 -> { showGameStatus(); yield false; }
            case 2 -> { handleShipAction(); yield false; }
            case 3 -> true;
            case 4 -> { handleSurrender(); yield true; }
            default -> { view.showInvalidOption(); yield false; }
        };
    }

    // -------------------------------------------------------------------------
    // Acciones de barco
    // -------------------------------------------------------------------------

    /**
     * Submenú: el jugador elige un barco y luego una acción sobre él.
     * Si cancela en cualquier punto, vuelve al menú principal.
     */
    private void handleShipAction() {
        Ship ship = view.askSelectShip(gameState.getCurrentPlayer());
        if (ship == null) return;

        view.showShipStats(ship);
        ShipAction action = view.askShipAction(ship, gameState);

        switch (action) {
            case MOVE            -> handleMove(ship);
            case ATTACK          -> handleAttack(ship);
            case REPAIR          -> handleRepair(ship);
            case BUY_PORT        -> handleBuyPort(ship);
            case EXTINGUISH_FIRE -> handleExtinguishFire(ship);
            case CANCEL          -> {}
        }
    }

    /**
     * Construye y ejecuta un MoveCommand.
     * Muestra las posiciones alcanzables y pide destino al jugador.
     */
    private void handleMove(Ship ship) {
        Set<Position> reachable = gameState.getBoard()
                .getReachablePositions(ship.getPosition(), ship.getEffectiveMovement());

        view.showReachablePositions(reachable);
        Position destination = view.askDestination(reachable, gameState);
        if (destination == null) return;

        ShipCommand command = new MoveCommand(ship, destination, movementResolver);
        executeCommand(command);
    }

    /**
     * Construye y ejecuta un AttackCommand.
     * Muestra los objetivos en rango y pide al jugador elegir uno.
     */
    private void handleAttack(Ship ship) {
        var opponent = gameState.getOpponentOf(gameState.getCurrentPlayer());
        List<Ship> targets = gameState.getBoard()
                .getTargetsInRange(ship, ship.getEffectiveRange(), opponent);

        if (targets.isEmpty()) {
            view.showNoTargets();
            return;
        }

        Ship target = view.askTarget(targets);
        if (target == null) return;

        ShipCommand command = new AttackCommand(ship, target, combatSystem);
        executeCommand(command);
    }

    /**
     * Construye y ejecuta un RepairCommand.
     */
    private void handleRepair(Ship ship) {
        ShipCommand command = new RepairCommand(ship, portService);
        executeCommand(command);
    }

    /**
     * Construye y ejecuta un BuyPortCommand.
     */
    private void handleBuyPort(Ship ship) {
        ShipCommand command = new BuyPortCommand(ship, portService);
        executeCommand(command);
    }

    /**
     * Construye y ejecuta un ExtinguishFireCommand.
     */
    private void handleExtinguishFire(Ship ship) {
        ShipCommand command = new ExtinguishFireCommand(ship, portService);
        executeCommand(command);
    }

    // -------------------------------------------------------------------------
    // Ejecución de comandos
    // -------------------------------------------------------------------------

    /**
     * Verifica canExecute y ejecuta el comando.
     * Si no puede ejecutarse, muestra error y vuelve al menú (no termina el turno).
     * Principio Tell Don't Ask: el comando sabe si puede ejecutarse,
     * el controller no inspecciona el estado para decidirlo.
     */
    private void executeCommand(ShipCommand command) {
        if (!command.canExecute(gameState)) {
            view.showError("Acción no válida en este momento.");
            return;
        }
        command.execute(gameState);
    }

    /**
     * Verifica si corresponde un evento aleatorio y lo aplica.
     */
    private void checkAndApplyEvent() {
        GameEvent event = eventManager.checkAndGetEvent(gameState);
        if (event == null) return;

        view.showEventAnnouncement(event);
        var result = event.apply(gameState);
        view.showEventResult(result);
    }

    // -------------------------------------------------------------------------
    // Otras acciones del menú
    // -------------------------------------------------------------------------

    private void showGameStatus() {
        view.showFullStatus(gameState);
    }

    private void handleSurrender() {
        gameState.getCurrentPlayer().surrender();
        gameState.checkVictoryConditions();
    }
}
