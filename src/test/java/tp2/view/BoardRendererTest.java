package tp2.view;

import org.junit.jupiter.api.Test;
import tp2.model.board.Board;
import tp2.model.board.Position;
import tp2.model.board.TerrainType;
import tp2.model.game.GameState;
import tp2.model.player.Faction;
import tp2.model.player.Player;
import tp2.model.ship.type.Sloop;
import tp2.model.treasure.DynasticTreasure;
import tp2.model.treasure.FloatingTreasure;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifica que el renderer convierta el modelo en una grilla ASCII legible
 * sin modificar el estado de la partida.
 */
class BoardRendererTest {

    @Test
    void rendersShipsTerrainAndBothTreasureKinds() {
        Board board = new Board(2, 4);
        Player first = new Player("Uno", Faction.CRIMSON_BROTHERHOOD);
        Player second = new Player("Dos", Faction.ABYSS_CROWN);
        Sloop ship = new Sloop("Nave");
        ship.setPosition(new Position(0, 0));
        first.addShip(ship);
        board.getCellAt(ship.getPosition()).placeShip(ship);
        board.getCellAt(new Position(0, 1)).setTerrain(TerrainType.STORM);
        board.getCellAt(new Position(0, 2)).setTerrain(TerrainType.MARINE_CURRENT);
        second.setOwnTreasure(new DynasticTreasure(second, new Position(1, 0)));
        GameState state = new GameState(board, List.of(first, second));
        state.addFloatingTreasure(new FloatingTreasure(new Position(1, 1), 2));

        String rendered = new BoardRenderer().render(state, null);

        assertTrue(rendered.contains("B1 T  MC"));
        assertTrue(rendered.contains("TR FT"));
        assertTrue(rendered.contains("Leyenda:"));
    }
}
