package tp2.view;

import org.junit.jupiter.api.Test;
import tp2.model.board.Board;
import tp2.model.board.Position;
import tp2.model.event.EventResult;
import tp2.model.game.GameState;
import tp2.model.game.VictoryCondition;
import tp2.model.player.Faction;
import tp2.model.player.Player;
import tp2.model.ship.effect.FireEffect;
import tp2.model.ship.type.Sloop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifica la interaccion de consola mediante streams inyectados.
 * Esto permite probar menus y mensajes sin depender de input manual.
 */
class ConsoleViewTest {

    @Test
    void destinationRejectsInvalidInputAndAcceptsValidCoordinates() {
        TestConsole console = console("texto\n5,5\n1,2\n");
        Board board = new Board(3, 3);
        Player player = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);
        GameState gameState = new GameState(board, List.of(player));

        Position selected = console.view.askDestination(Set.of(new Position(1, 2)), gameState);

        assertEquals(new Position(1, 2), selected);

        assertTrue(console.errors().contains("Elija otra"));
    }

    @Test
    void destinationCanBeCancelled() {
        TestConsole console = console("0\n");
        Board board = new Board(3, 3);
        Player player = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);
        GameState gameState = new GameState(board, List.of(player));

        Position selected = console.view.askDestination(Set.of(new Position(1, 2)), gameState);
        assertNull(selected);
    }

    @Test
    void actionMenuIncludesContextualFireAction() {
        TestConsole console = console("5\n");

        Board board = new Board(2, 2);
        Player player = new Player("Uno", Faction.CRIMSON_BROTHERHOOD);

        Sloop ship = new Sloop("Nave");
        ship.setPosition(new Position(0, 0));
        ship.addEffect(new FireEffect());

        player.addShip(ship);
        board.getCellAt(ship.getPosition()).placeShip(ship);

        GameState state = new GameState(board, List.of(player));

        ShipAction action = console.view.askShipAction(ship, state);

        assertEquals(ShipAction.EXTINGUISH_FIRE, action);

        assertTrue(console.output().contains("Apagar incendio"));
    }

    @Test
    void mapSelectionUsesInjectedConfigurationOptions() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        ConsoleView view = new ConsoleView(
                new ByteArrayInputStream("2\n".getBytes(StandardCharsets.UTF_8)),
                new PrintStream(output),
                new PrintStream(new ByteArrayOutputStream()),
                List.of("clasico.json", "islas.json"),
                List.of("balanceada.json")
        );

        assertEquals("islas.json", view.askMapFile());
        assertTrue(output.toString(StandardCharsets.UTF_8).contains("1. clasico.json"));
        assertTrue(output.toString(StandardCharsets.UTF_8).contains("2. islas.json"));
    }

    @Test
    void fleetSelectionUsesInjectedConfigurationOptions() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        ConsoleView view = new ConsoleView(
                new ByteArrayInputStream("1\n".getBytes(StandardCharsets.UTF_8)),
                new PrintStream(output),
                new PrintStream(new ByteArrayOutputStream()),
                List.of("mapa.json"),
                List.of("balanced.json", "heavy.json")
        );

        assertEquals("balanced.json", view.askFleetFile());
        assertTrue(output.toString(StandardCharsets.UTF_8).contains("1. balanced.json"));
        assertTrue(output.toString(StandardCharsets.UTF_8).contains("2. heavy.json"));
    }

    @Test
    void mainMenuReturnsSelectedOption() {
        TestConsole console = console("3\n");
        assertEquals(3, console.view.showMainMenu());
    }

    @Test
    void mainMenuReturnsMinusOneForInvalidInput() {
        TestConsole console = console("hola\n");
        assertEquals(-1, console.view.showMainMenu());
    }

    @Test
    void fullStatusIncludesTurnResourcesFleetAndTreasureState() {
        TestConsole console = console("");

        Board board = new Board(1, 1);
        Player player = new Player("Uno", Faction.CRIMSON_BROTHERHOOD);

        Sloop ship = new Sloop("Nave");
        ship.setPosition(new Position(0, 0));

        player.addShip(ship);
        board.getCellAt(ship.getPosition()).placeShip(ship);

        GameState state = new GameState(board, List.of(player));

        console.view.showFullStatus(state);

        String output = console.output();

        assertTrue(output.contains("Turno 1"));
        assertTrue(output.contains("Recursos"));
        assertTrue(output.contains("Nave [SLOOP]"));
        assertTrue(output.contains("Tesoro dinastico: sin asignar"));
    }

    @Test
    void eventResultShowsAffectedEntitiesDurationAndConsequences() {
        TestConsole console = console("");

        console.view.showEventResult(
                new EventResult("Vendaval", List.of("Nave"), 1, List.of("Movimiento reducido"))
        );

        String output = console.output();

        assertTrue(output.contains("Afectados: Nave"));
        assertTrue(output.contains("Duracion: 1 turno(s)"));
        assertTrue(output.contains("- Movimiento reducido"));
    }

    @Test
    void victoryMessageIncludesWinnerAndReason() {
        TestConsole console = console("");

        Player player = new Player("Bruno", Faction.CRIMSON_BROTHERHOOD);

        console.view.showVictory(player, VictoryCondition.SURRENDER);

        String output = console.output();

        assertTrue(output.contains("FIN DE LA PARTIDA"));
        assertTrue(output.contains("Bruno"));
        assertTrue(output.contains("rendicion del rival"));
    }

    @Test
    void showNoTargetsDisplaysExpectedMessage() {
        TestConsole console = console("");
        console.view.showNoTargets();
        assertTrue(console.output().contains("No hay barcos enemigos en rango"));
    }

    @Test
    void showFirstPlayerDisplaysPlayerName() {
        TestConsole console = console("");

        Player player = new Player("Bruno", Faction.CRIMSON_BROTHERHOOD);

        console.view.showFirstPlayer(player);

        assertTrue(console.output().contains("Comienza la partida"));

        assertTrue(console.output().contains("Bruno"));
    }

    @Test
    void showErrorWritesToErrorStream() {
        TestConsole console = console("");

        console.view.showError("Fallo");
        assertTrue(console.errors().contains("[ERROR] Fallo"));
    }

    @Test
    void showMessageWritesToOutput() {
        TestConsole console = console("");
        console.view.showMessage("Hola");
        assertTrue(console.output().contains("Hola"));
    }

    private TestConsole console(String input) {
        return new TestConsole(input);
    }

    /**
     * Fixture liviano que captura salida normal y errores de la vista.
     */
    private static class TestConsole {

        private final ByteArrayOutputStream output = new ByteArrayOutputStream();
        private final ByteArrayOutputStream errors = new ByteArrayOutputStream();
        private final ConsoleView view;

        private TestConsole(String input) {
            view = new ConsoleView(
                    new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                    new PrintStream(output),
                    new PrintStream(errors)
            );
        }

        private String output() {
            return output.toString(StandardCharsets.UTF_8);
        }

        private String errors() {
            return errors.toString(StandardCharsets.UTF_8);
        }
    }
}