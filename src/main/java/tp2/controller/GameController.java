package tp2.controller;

import tp2.config.GameConfig;
import tp2.controller.command.*;
import tp2.model.board.Board;
import tp2.model.board.Cell;
import tp2.model.game.GameState;
import tp2.model.board.Position;
import tp2.model.board.TerrainType;
import tp2.model.combat.AttackResult;
import tp2.model.combat.CombatSystem;
import tp2.model.event.EventManager;
import tp2.model.event.GameEvent;
import tp2.model.game.system.CellInteractionResolver;
import tp2.model.game.system.MovementResolver;
import tp2.model.game.system.PortService;
import tp2.model.game.system.TurnProcessor;
import tp2.model.board.terrain.TerrainRegistry;
import tp2.model.player.Player;
import tp2.model.resource.ResourceCosts;
import tp2.model.ship.EffectType;
import tp2.model.ship.Ship;
import tp2.model.ship.effect.FireEffect;
import tp2.model.ship.effect.ShipEffect;
import tp2.model.treasure.DynasticTreasure;
import tp2.model.treasure.FloatingTreasure;
import tp2.model.treasure.Treasure;
import tp2.view.GameView;
import tp2.view.ShipAction;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

        List<EndTurnShipSnapshot> endTurnSnapshots = snapshotCurrentPlayerShipsForEndTurn();
        turnProcessor.processEndOfTurn(gameState);
        showEndTurnFeedback(endTurnSnapshots);
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
            Set<Position> validPositions = new HashSet<>(board.getPositionsMatching(true));
            Position portPos = view.askPortPosition(player, validPositions, gameState);
            portService.placeInitialPort(board, player, portPos);

            for (Ship ship : player.getActiveFleet()) {
                if (!ship.isFlagship()) {
                    Set<Position> validPositionsForShip = new HashSet<>(board.getPositionsMatching(true));
                    Position shipPos = view.askShipPlacementPosition(player, ship, validPositionsForShip, gameState);
                    portService.placeInitialShip(board, ship, shipPos);
                }
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
            case 4 -> handleSurrender();
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
        if (!ship.canMove()) {
            view.showActionUnavailable(ship, ShipAction.MOVE, describeMoveBlocker(ship));
            return;
        }

        Set<Position> reachable = gameState.getBoard()
                .getReachablePositions(ship.getPosition(), ship.getEffectiveMovement());

        view.showReachablePositions(reachable);
        Position destination = view.askDestination(reachable, gameState);
        if (destination == null) return;

        ShipCommand command = new MoveCommand(ship, destination, movementResolver);
        if (!command.canExecute(gameState)) {
            view.showActionUnavailable(ship, ShipAction.MOVE, "el destino no está dentro de su alcance");
            return;
        }

        MoveSnapshot before = snapshotMove(ship, destination);
        command.execute(gameState);
        showMoveFeedback(before);
    }

    /**
     * Construye y ejecuta un AttackCommand.
     * Muestra los objetivos en rango y pide al jugador elegir uno.
     */
    private void handleAttack(Ship ship) {
        if (!ship.canAttack()) {
            view.showActionUnavailable(ship, ShipAction.ATTACK, describeAttackBlocker(ship));
            return;
        }

        var opponent = gameState.getOpponentOf(gameState.getCurrentPlayer());
        List<Ship> targets = gameState.getBoard()
                .getTargetsInRange(ship, ship.getEffectiveRange(), opponent);

        if (targets.isEmpty()) {
            view.showNoTargets();
            return;
        }

        Ship target = view.askTarget(targets);
        if (target == null) return;

        AttackCommand command = new AttackCommand(ship, target, combatSystem);
        if (!command.canExecute(gameState)) {
            view.showActionUnavailable(ship, ShipAction.ATTACK, "el objetivo está fuera de rango");
            return;
        }

        int goldBefore = gameState.getCurrentPlayer().getResources().getGold();
        command.execute(gameState);
        view.showAttackResult(command.getLastResult());
        if (command.getLastResult().isDefenderSunk()) {
            int goldAfter = gameState.getCurrentPlayer().getResources().getGold();
            view.showShipSunkReward(target, goldAfter - goldBefore, goldAfter);
        }
    }

    /**
     * Construye y ejecuta un RepairCommand.
     */
    private void handleRepair(Ship ship) {
        ShipCommand command = new RepairCommand(ship, portService);
        if (!command.canExecute(gameState)) {
            view.showActionUnavailable(ship, ShipAction.REPAIR, describeRepairBlocker(ship));
            return;
        }

        boolean inOwnPort = portService.isShipInCurrentPlayerPort(gameState, ship);
        int hpBefore = ship.getCurrentHp();
        int woodBefore = gameState.getCurrentPlayer().getResources().getWood();
        command.execute(gameState);
        int woodSpent = woodBefore - gameState.getCurrentPlayer().getResources().getWood();
        view.showRepairResult(ship, ship.getCurrentHp() - hpBefore, inOwnPort, woodSpent);
    }

    /**
     * Construye y ejecuta un BuyPortCommand.
     */
    private void handleBuyPort(Ship ship) {
        ShipCommand command = new BuyPortCommand(ship, portService);
        if (!command.canExecute(gameState)) {
            view.showActionUnavailable(ship, ShipAction.BUY_PORT, describeBuyPortBlocker(ship));
            return;
        }

        int goldBefore = gameState.getCurrentPlayer().getResources().getGold();
        command.execute(gameState);
        int goldAfter = gameState.getCurrentPlayer().getResources().getGold();
        view.showPortPurchased(ship, goldBefore - goldAfter, goldAfter);
    }

    /**
     * Construye y ejecuta un ExtinguishFireCommand.
     */
    private void handleExtinguishFire(Ship ship) {
        ShipCommand command = new ExtinguishFireCommand(ship, portService);
        if (!command.canExecute(gameState)) {
            view.showActionUnavailable(ship, ShipAction.EXTINGUISH_FIRE, describeExtinguishBlocker(ship));
            return;
        }

        boolean inOwnPort = portService.isShipInCurrentPlayerPort(gameState, ship);
        int woodBefore = gameState.getCurrentPlayer().getResources().getWood();
        command.execute(gameState);
        int woodSpent = woodBefore - gameState.getCurrentPlayer().getResources().getWood();
        view.showFireExtinguished(ship, inOwnPort, woodSpent);
    }

    // -------------------------------------------------------------------------
    // Feedback de acciones
    // -------------------------------------------------------------------------

    private MoveSnapshot snapshotMove(Ship ship, Position destination) {
        Cell destinationCell = gameState.getBoard().getCellAt(destination);
        return new MoveSnapshot(
                ship,
                ship.getPosition(),
                destination,
                destinationCell.getTerrain(),
                destinationCell.getTreasure(),
                ship.getCurrentHp(),
                gameState.getCurrentPlayer().getResources().getGold()
        );
    }

    private void showMoveFeedback(MoveSnapshot before) {
        Ship ship = before.ship();
        view.showMoveResult(ship, before.origin(), before.destination());

        if (before.destinationTreasure() instanceof DynasticTreasure treasure) {
            view.showDynasticTreasureCollected(ship, treasure.getOwner());
        } else if (before.destinationTreasure() instanceof FloatingTreasure) {
            int goldGained = gameState.getCurrentPlayer().getResources().getGold() - before.goldBefore();
            view.showFloatingTreasureCollected(ship, goldGained, calculateFloatingTreasureRepair(before));
        }

        if (before.destinationTerrain() == TerrainType.REEF) {
            view.showReefDamage(ship, calculateReefDamage(before));
        } else if (before.destinationTerrain() == TerrainType.FOG) {
            view.showFogEntered(ship);
        } else if (before.destinationTerrain() == TerrainType.STORM) {
            view.showStormEntered(ship);
        }

        if (before.destinationTerrain() == TerrainType.MARINE_CURRENT
                && !ship.getPosition().equals(before.destination())) {
            view.showMarineCurrentPush(ship, ship.getPosition());
        }
    }

    private int calculateFloatingTreasureRepair(MoveSnapshot before) {
        return Math.min(ResourceCosts.FLOATING_TREASURE_HP_REPAIR,
                before.ship().getMaxHp() - before.hpBefore());
    }

    private int calculateReefDamage(MoveSnapshot before) {
        int hpBeforeReef = before.hpBefore() + (before.destinationTreasure() instanceof FloatingTreasure
                ? calculateFloatingTreasureRepair(before)
                : 0);
        int effectiveDamage = Math.max(0, GameConfig.REEF_DAMAGE - before.ship().getDef());
        return Math.min(hpBeforeReef, effectiveDamage);
    }

    private List<EndTurnShipSnapshot> snapshotCurrentPlayerShipsForEndTurn() {
        List<EndTurnShipSnapshot> snapshots = new ArrayList<>();
        for (Ship ship : gameState.getCurrentPlayer().getActiveFleet()) {
            snapshots.add(new EndTurnShipSnapshot(
                    ship,
                    ship.getCurrentHp(),
                    portService.isShipInCurrentPlayerPort(gameState, ship),
                    calculateFireDamage(ship),
                    countEffects(ship.getActiveEffects())
            ));
        }
        return snapshots;
    }

    private void showEndTurnFeedback(List<EndTurnShipSnapshot> snapshots) {
        for (EndTurnShipSnapshot before : snapshots) {
            Ship ship = before.ship();
            int hpAfterFire = Math.max(0, before.hpBefore() - before.fireDamage());
            if (before.fireDamage() > 0) {
                view.showEndTurnFireDamage(ship, before.fireDamage(), hpAfterFire);
            }

            int hpRecovered = ship.getCurrentHp() - hpAfterFire;
            if (before.inOwnPort() && hpRecovered > 0) {
                view.showEndTurnPortRepair(ship, hpRecovered);
            }

            Map<EffectType, Integer> effectsAfter = countEffects(ship.getActiveEffects());
            for (var entry : before.effectsBefore().entrySet()) {
                int expired = entry.getValue() - effectsAfter.getOrDefault(entry.getKey(), 0);
                for (int index = 0; index < expired; index++) {
                    view.showEffectExpired(ship, entry.getKey());
                }
            }
        }
    }

    private int calculateFireDamage(Ship ship) {
        int hpRemaining = ship.getCurrentHp();
        for (ShipEffect effect : ship.getActiveEffects()) {
            if (effect instanceof FireEffect fireEffect) {
                int damage = Math.max(0, fireEffect.getDamagePerTurn() - ship.getDef());
                hpRemaining = Math.max(0, hpRemaining - damage);
            }
        }
        return ship.getCurrentHp() - hpRemaining;
    }

    private Map<EffectType, Integer> countEffects(List<ShipEffect> effects) {
        Map<EffectType, Integer> counts = new EnumMap<>(EffectType.class);
        for (ShipEffect effect : effects) {
            counts.merge(effect.getType(), 1, Integer::sum);
        }
        return counts;
    }

    private String describeMoveBlocker(Ship ship) {
        if (ship.isSunk()) return "está hundido";
        if (ship.hasMovedThisTurn()) return "ya se movió en este turno";
        if (ship.hasAttackedThisTurn() && !canMoveAfterAttack(ship)) {
            return "ya atacó en este turno";
        }
        return "tiene un efecto activo que le impide moverse";
    }

    private String describeAttackBlocker(Ship ship) {
        if (ship.isSunk()) return "está hundido";
        if (ship.hasAttackedThisTurn()) return "ya atacó en este turno";
        if (ship.isOnFire()) return "tiene un incendio activo";
        return "tiene un efecto activo que le impide atacar";
    }

    private String describeRepairBlocker(Ship ship) {
        if (ship.isSunk()) return "está hundido";
        if (!portService.isShipInCurrentPlayerPort(gameState, ship)
                && !gameState.getCurrentPlayer().getResources()
                        .hasEnoughWood(ResourceCosts.REPAIR_OUT_OF_PORT_WOOD)) {
            return "no está en un puerto propio y no tiene madera suficiente";
        }
        return "no cumple las condiciones para repararse";
    }

    private String describeBuyPortBlocker(Ship ship) {
        if (!portService.isUnownedPortAtShipPosition(gameState, ship)) {
            return "no está sobre un puerto neutral";
        }
        if (!gameState.getCurrentPlayer().getResources().hasEnoughGold(ResourceCosts.BUY_PORT_GOLD)) {
            return "no tiene oro suficiente";
        }
        return "no cumple las condiciones para comprar puerto";
    }

    private String describeExtinguishBlocker(Ship ship) {
        if (!ship.isOnFire()) return "no tiene incendio activo";
        if (!portService.isShipInCurrentPlayerPort(gameState, ship)
                && !gameState.getCurrentPlayer().getResources()
                        .hasEnoughWood(ResourceCosts.EXTINGUISH_FIRE_WOOD)) {
            return "no está en un puerto propio y no tiene madera suficiente";
        }
        return "no cumple las condiciones para apagar el incendio";
    }

    private boolean canMoveAfterAttack(Ship ship) {
        return ship.getActiveEffects().stream().anyMatch(ShipEffect::allowsMoveAfterAttack);
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

    private boolean handleSurrender() {
        Player player = gameState.getCurrentPlayer();
        if (!view.confirmSurrender(player)) {
            return false;
        }
        player.surrender();
        view.showSurrenderConfirmed(player);
        gameState.checkVictoryConditions();
        return true;
    }

    private record MoveSnapshot(
            Ship ship,
            Position origin,
            Position destination,
            TerrainType destinationTerrain,
            Treasure destinationTreasure,
            int hpBefore,
            int goldBefore
    ) {}

    private record EndTurnShipSnapshot(
            Ship ship,
            int hpBefore,
            boolean inOwnPort,
            int fireDamage,
            Map<EffectType, Integer> effectsBefore
    ) {}
}
