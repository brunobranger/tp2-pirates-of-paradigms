package tp2.view;

import org.junit.jupiter.api.Test;
import tp2.model.board.Board;
import tp2.model.board.Position;
import tp2.model.game.GameState;
import tp2.model.player.Faction;
import tp2.model.player.Player;
import tp2.model.ship.type.Sloop;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardRendererTest {

    @Test
    void validPositionsAreMarkedWithBullet() {
        Board board = new Board(2, 2);
        Player player = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);
        GameState state = new GameState(board, List.of(player));
        String rendered = new BoardRenderer().render(state, Set.of(new Position(0, 0)));

        assertTrue(rendered.contains("•"));
    }

    @Test
    void occupiedCellKeepsShipSymbolInsteadOfBullet() {
        Board board = new Board(2, 2);
        Player player = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);
        Sloop ship = new Sloop("Balandra");

        player.addShip(ship);
        board.getCellAt(new Position(0, 0)).placeShip(ship);

        GameState state = new GameState(board, List.of(player));
        String rendered = new BoardRenderer().render(state, Set.of(new Position(0, 0)));

        assertTrue(rendered.contains("B1"));
    }

    @Test
    void rendererShowsColumnHeaders() {
        Board board = new Board(2, 2);
        Player player = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);

        GameState state = new GameState(board, List.of(player));
        String rendered = new BoardRenderer().render(state);

        assertTrue(rendered.contains("0"));
        assertTrue(rendered.contains("1"));
    }

    @Test
    void rendererShowsRowHeaders() {
        Board board = new Board(2, 2);
        Player player = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);

        GameState state = new GameState(board, List.of(player));
        String rendered = new BoardRenderer().render(state);

        assertTrue(rendered.contains("0"));
        assertTrue(rendered.contains("1"));
    }

    @Test
    void rendererShowsLegend() {
        Board board = new Board(1, 1);
        Player player = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);

        GameState state = new GameState(board, List.of(player));
        String rendered = new BoardRenderer().render(state);

        assertTrue(rendered.contains("Leyenda:"));
        assertTrue(rendered.contains("B1/B2 barco"));
        assertTrue(rendered.contains("FT tesoro flotante"));
    }

    @Test
    void shipWithoutOwnerIsRenderedAsUnknownShip() {
        Board board = new Board(1, 1);
        Player player = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);
        Sloop ship = new Sloop("Balandra");

        board.getCellAt(new Position(0, 0)).placeShip(ship);

        GameState state = new GameState(board, List.of(player));
        String rendered = new BoardRenderer().render(state);

        assertTrue(rendered.contains("B?"));
    }
}