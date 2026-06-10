package tp2.view;

import tp2.model.board.Position;
import tp2.model.combat.AttackResult;
import tp2.model.event.EventResult;
import tp2.model.event.GameEvent;
import tp2.model.game.GameState;
import tp2.model.game.VictoryCondition;
import tp2.model.player.Player;
import tp2.model.ship.Ship;
import tp2.model.treasure.DynasticTreasure;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.function.Function;

import static java.lang.Integer.parseInt;

/**
 * Implementacion por consola de GameView.
 * Responsabilidad: mostrar informacion de la partida y transformar el input
 * del usuario en elecciones simples para el controlador.
 */
public class ConsoleView implements GameView {

    private static final String CANCEL_HINT = "0 para cancelar";

    private final Scanner scanner;
    private final PrintStream out;
    private final PrintStream err;
    private final BoardRenderer boardRenderer;
    private final List<String> availableMaps;
    private final List<String> availableFleets;

    /** Construye la vista interactiva con la entrada y salida estandar. */
    public ConsoleView() {
        this(System.in, System.out, System.err);
    }

    /**
     * Construye una vista con streams inyectados.
     */
    public ConsoleView(InputStream input, PrintStream output, PrintStream error) {
        this(input, output, error,
                List.of("predeterminada.json", "archipielago.json", "pasillos_actuales.json", "paso_de_arrecife.json"),
                List.of("balanced.json"));
    }

    /**
     * Construye la vista interactiva con las configuraciones descubiertas por
     * el componente responsable de leer archivos.
     */
    public ConsoleView(List<String> availableMaps, List<String> availableFleets) {
        this(System.in, System.out, System.err, availableMaps, availableFleets);
    }

    /**
     * Variante completamente inyectable para composicion y tests.
     */
    public ConsoleView(InputStream input, PrintStream output, PrintStream error,
                       List<String> availableMaps, List<String> availableFleets) {
        this(input, output, error, new BoardRenderer(), availableMaps, availableFleets);
    }

    // -------------------------------------------------------------------------
    // Setup inicial
    // -------------------------------------------------------------------------

    ConsoleView(InputStream input, PrintStream output, PrintStream error,
                BoardRenderer boardRenderer, List<String> availableMaps,
                List<String> availableFleets) {
        this.scanner = new Scanner(input);
        this.out = output;
        this.err = error;
        this.boardRenderer = boardRenderer;
        this.availableMaps = List.copyOf(availableMaps);
        this.availableFleets = List.copyOf(availableFleets);
    }

    @Override
    public String askMapFile() {
        return askFile("mapa", availableMaps);
    }

    @Override
    public String askFleetFile() {
        return askFile("flota", availableFleets);
    }

    @Override
    public Position askPortPosition(Player player, Set<Position> validPositions, GameState gameState) {
        return askPosition("Ubique el puerto inicial de " + player + ".", validPositions, false, gameState);
    }

    @Override
    public Position askShipPlacementPosition(Player player, Ship ship, Set<Position> validPositions, GameState gameState) {
        return askPosition("Ubique " + ship.getName() + " de " + player + ".", validPositions, false, gameState);
    }

    // -------------------------------------------------------------------------
    // Menu principal y acciones
    // -------------------------------------------------------------------------

    @Override
    public int showMainMenu() {
        out.println();
        out.println("1. Ver estado de la partida");
        out.println("2. Utilizar barco");
        out.println("3. Finalizar turno");
        out.println("4. Rendirse");
        out.print("Ingrese opcion: ");
        String input = readLine();
        try {
            return parseInt(input);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    @Override
    public Ship askSelectShip(Player player) {
        return askListItem("Seleccione un barco propio:", player.getActiveFleet(),
                this::describeShip, true);
    }

    @Override
    public ShipAction askShipAction(Ship ship, GameState gameState) {
        List<ShipAction> actions = new ArrayList<>();
        actions.add(ShipAction.MOVE);
        actions.add(ShipAction.ATTACK);
        actions.add(ShipAction.REPAIR);
        actions.add(ShipAction.BUY_PORT);
        if (ship.isOnFire()) {
            actions.add(ShipAction.EXTINGUISH_FIRE);
        }

        ShipAction selected = askListItem("Seleccione una accion para " + ship.getName() + ":",
                actions, this::describeAction, true);
        return selected == null ? ShipAction.CANCEL : selected;
    }

    @Override
    public Position askDestination(Set<Position> reachable, GameState gameState) {
        return askPosition("Ingrese destino como fila,columna (" + CANCEL_HINT + "):",
                reachable, true, gameState);
    }

    @Override
    public Ship askTarget(List<Ship> targets) {
        return askListItem("Seleccione un objetivo:", targets, this::describeShip, true);
    }

    // -------------------------------------------------------------------------
    // Presentacion del estado
    // -------------------------------------------------------------------------

    @Override
    public void showBoard(GameState gameState) {
        out.print(boardRenderer.render(gameState));
    }

    @Override
    public void showTurnSummary(GameState gameState) {
        out.println("Turno " + gameState.getTurnNumber() + " | Jugador: "
                + gameState.getCurrentPlayer() + " | " + gameState.getCurrentPlayer().getResources());
    }

    @Override
    public void showFullStatus(GameState gameState) {
        showBoard(gameState);
        showTurnSummary(gameState);
        for (Player player : gameState.getPlayers()) {
            out.println();
            out.println(player + " | Recursos: " + player.getResources());
            out.println("Puerto inicial: " + displayPosition(player.getHomePortPosition()));
            out.println("Flota:");
            if (player.getActiveFleet().isEmpty()) {
                out.println("  Sin barcos activos");
            }
            player.getActiveFleet().forEach(ship -> out.println("  " + describeShip(ship)));
            out.println("Tesoro dinastico: " + describeTreasure(player.getOwnTreasure()));
        }
    }

    @Override
    public void showShipStats(Ship ship) {
        out.println(describeShip(ship));
        out.println("  MOV efectivo: " + ship.getEffectiveMovement()
                + " | RNG efectivo: " + ship.getEffectiveRange()
                + " | ATK efectivo: " + ship.getEffectiveAttack());
        out.println("  Efectos: " + (ship.getActiveEffects().isEmpty()
                ? "ninguno"
                : ship.getActiveEffects().stream()
                        .map(effect -> effect.getType().name())
                        .toList()));
        out.println("  Transporta tesoro: " + (ship.isCarryingTreasure() ? "si" : "no"));
    }

    @Override
    public void showReachablePositions(Set<Position> reachable) {
        out.println("Posiciones alcanzables: " + sortedPositions(reachable));
    }

    @Override
    public void showAttackResult(AttackResult result) {
        out.println(result);
    }

    // -------------------------------------------------------------------------
    // Eventos y fin de partida
    // -------------------------------------------------------------------------

    @Override
    public void showEventAnnouncement(GameEvent event) {
        out.println();
        out.println("Evento: " + event.getName());
        out.println(event.getDescription());
    }

    @Override
    public void showEventResult(EventResult result) {
        out.println("Resultado de " + result.getEventName() + ":");
        if (!result.getAffectedEntities().isEmpty()) {
            out.println("Afectados: " + String.join(", ", result.getAffectedEntities()));
        }
        if (result.getDurationTurns() > 0) {
            out.println("Duracion: " + result.getDurationTurns() + " turno(s)");
        }
        result.getConsequences().forEach(consequence -> out.println("- " + consequence));
    }

    @Override
    public void showVictory(Player winner, VictoryCondition condition) {
        out.println();
        out.println("=== FIN DE LA PARTIDA ===");
        out.println("Ganador: " + winner);
        out.println("Motivo: " + describeVictory(condition));
    }

    // -------------------------------------------------------------------------
    // Mensajes generales
    // -------------------------------------------------------------------------

    @Override
    public void showError(String message) {
        err.println("[ERROR] " + message);
    }

    @Override
    public void showMessage(String message) {
        out.println(message);
    }

    @Override
    public void showFirstPlayer(Player player) {
        out.println("Comienza la partida: " + player);
    }

    @Override
    public void showNoTargets() {
        showMessage("No hay barcos enemigos en rango.");
    }

    @Override
    public void showInvalidOption() {
        showError("Opcion invalida. Intente nuevamente.");
    }

    @Override
    public void showCellInvalid() {
        showError("Elija otra posición para colocar el barco o puerto!");
    }

    @Override
    public void showInitShipPlace() {
        showMessage("");
        showMessage("--- FASE DE DESPLIEGUE INICIAL DE FLOTAS ---");
    }

    /** Muestra una lista de archivos preseleccionados por el loader. */
    private String askFile(String label, List<String> files) {
        return askListItem("Seleccione archivo de " + label + ":", files,
                Function.identity(), false);
    }

    /**
     * Solicita coordenadas y repite el pedido hasta recibir una posicion valida
     * o una cancelacion, si el flujo permite volver al menu anterior.
     */
    private Position askPosition(String prompt, Set<Position> validPositions, boolean cancellable, GameState gameState) {
        out.println(prompt);
        if (gameState != null) {
            out.println("Las zonas permitidas están marcadas con '•' en el mapa:");
            out.println("No puede colocar elementos sobre puertos P o islas I.");
            out.println();
            out.println(boardRenderer.render(gameState, validPositions));
        }

        while (true) {
            out.println("Ingrese coordenadas como fila,columna: ");
            out.print("> ");
            String input = readLine().trim();
            if (cancellable && isCancel(input)) {
                return null;
            }
            Position position = parsePosition(input);
            if (position != null && validPositions.contains(position)) {
                return position;
            }
            showCellInvalid();
        }
    }

    /** Convierte el formato publico fila,columna en un value object Position. */
    private Position parsePosition(String input) {
        String[] coordinates = input.split(",");
        if (coordinates.length != 2) {
            return null;
        }
        try {
            return new Position(parseInt(coordinates[0].trim()),
                    parseInt(coordinates[1].trim()));
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    /**
     * Presenta opciones numeradas y devuelve el objeto elegido.
     * El helper evita duplicar la validacion de menus en toda la vista.
     */
    private <T> T askListItem(String prompt, List<T> options,
                              Function<T, String> formatter, boolean cancellable) {
        if (options.isEmpty()) {
            showError("No hay opciones disponibles.");
            return null;
        }
        out.println(prompt);
        for (int index = 0; index < options.size(); index++) {
            out.println((index + 1) + ". " + formatter.apply(options.get(index)));
        }
        if (cancellable) {
            out.println(CANCEL_HINT);
        }

        while (true) {
            out.print("> ");
            String input = readLine().trim();
            if (cancellable && isCancel(input)) {
                return null;
            }
            try {
                int index = parseInt(input) - 1;
                if (index >= 0 && index < options.size()) {
                    return options.get(index);
                }
            } catch (NumberFormatException ignored) {
            }
            showInvalidOption();
        }
    }

    /** Lee una linea o retorna vacio si el stream ya finalizo. */
    private String readLine() {
        return scanner.hasNextLine() ? scanner.nextLine() : "";
    }

    /** Admite 0 y la letra c como convenciones de cancelacion. */
    private boolean isCancel(String input) {
        return input.equals("0") || input.equalsIgnoreCase("c");
    }

    /** Ordena coordenadas para que la salida sea estable y facil de leer. */
    private List<Position> sortedPositions(Set<Position> positions) {
        return positions.stream()
                .sorted(Comparator.comparingInt(Position::getRow)
                        .thenComparingInt(Position::getCol))
                .toList();
    }

    /** Construye el resumen de estadisticas base visible en listados. */
    private String describeShip(Ship ship) {
        return ship.getName() + " [" + ship.getType() + "]"
                + " | HP " + ship.getCurrentHp() + "/" + ship.getMaxHp()
                + " | ATK " + ship.getAtk()
                + " | DEF " + ship.getDef()
                + " | MOV " + ship.getMov()
                + " | RNG " + ship.getRng()
                + " | Pos " + displayPosition(ship.getPosition());
    }

    /** Describe la ubicacion o el barco portador de un tesoro dinastico. */
    private String describeTreasure(DynasticTreasure treasure) {
        if (treasure == null) {
            return "sin asignar";
        }
        if (treasure.isCarried()) {
            return "transportado por " + treasure.getCarrierTreasure().getName();
        }
        return (treasure.isFloating() ? "flotando en " : "ubicado en ")
                + displayPosition(treasure.getPositionCarrierTreasure());
    }

    /** Traduce el enum interno a una opcion legible para el usuario. */
    private String describeAction(ShipAction action) {
        return switch (action) {
            case MOVE -> "Mover";
            case ATTACK -> "Atacar";
            case REPAIR -> "Reparar";
            case BUY_PORT -> "Comprar puerto";
            case EXTINGUISH_FIRE -> "Apagar incendio";
            case CANCEL -> "Cancelar";
        };
    }

    /** Traduce la condicion de victoria a un mensaje de cierre. */
    private String describeVictory(VictoryCondition condition) {
        if (condition == null) {
            return "condicion no informada";
        }
        return switch (condition) {
            case FLAGSHIP_SUNK -> "barco insignia rival hundido";
            case TREASURE_CAPTURED -> "tesoro enemigo entregado en puerto propio";
            case SURRENDER -> "rendicion del rival";
        };
    }

    /** Evita imprimir null cuando una entidad aun no fue posicionada. */
    private String displayPosition(Position position) {
        return position == null ? "sin posicion" : position.toString();
    }
}
