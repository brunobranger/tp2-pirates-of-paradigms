package tp2.model.game.system;

import org.junit.jupiter.api.Test;
import tp2.model.board.Board;
import tp2.model.board.Position;
import tp2.model.board.TerrainType;
import tp2.model.game.GameState;
import tp2.model.player.Faction;
import tp2.model.player.Player;
import tp2.model.ship.type.Flagship;
import tp2.model.ship.type.Sloop;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PortServiceTest {

    private final PortService service = new PortService();

    @Test
    void placeInitialPortAssignsOwnerAndFlagship() {
        Board board = new Board(5, 5);
        Player player = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);
        Flagship flagship = new Flagship("Insignia");

        player.addShip(flagship);

        Position port = new Position(2, 2);
        service.placeInitialPort(board, player, port);

        assertEquals(TerrainType.PORT, board.getCellAt(port).getTerrain());
        assertEquals(player, board.getCellAt(port).getPortOwner());
        assertEquals(port, player.getHomePortPosition());
        assertEquals(flagship, board.getCellAt(port).getOccupant());
    }

    @Test
    void placeInitialShipPlacesShipOnBoard() {
        Board board = new Board(5, 5);
        Sloop ship = new Sloop("Balandra");
        Position position = new Position(1, 1);

        service.placeInitialShip(board, ship, position);
        assertEquals(ship, board.getCellAt(position).getOccupant());
    }

    @Test
    void shipInOwnedPortReturnsTrue() {
        Board board = new Board(5, 5);
        Player player = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);
        Flagship flagship = new Flagship("Insignia");

        player.addShip(flagship);
        Position port = new Position(2, 2);
        service.placeInitialPort(board, player, port);

        GameState state = new GameState(board, List.of(player));
        assertTrue(service.isShipInCurrentPlayerPort(state, flagship));
    }

    @Test
    void repairIsAppliedWhenDocked() {
        Board board = new Board(2, 2);
        Player player = new Player("Jugador", Faction.CRIMSON_BROTHERHOOD);
        Flagship flagship = new Flagship("Flagship");

        player.addShip(flagship);

        PortService service = new PortService();
        service.placeInitialPort(board, player, new Position(0, 0));

        flagship.receiveDamage(9999);
        int before = flagship.getCurrentHp();

        assertTrue(board.getCellAt(flagship.getPosition()).isOwnedPortOf(player));
        service.applyEndTurnRepairIfDocked(board, flagship, player);

        int after = flagship.getCurrentHp();

        assertTrue(after > before);
    }
}