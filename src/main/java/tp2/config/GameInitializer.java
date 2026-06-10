package tp2.config;

import tp2.model.board.Board;
import tp2.model.board.Position;
import tp2.model.game.GameState;
import tp2.model.player.Player;
import tp2.model.treasure.DynasticTreasure;

import java.util.List;

/**
 * Responsable de crear una partida nueva válida.
 * No lee archivos ni parsea JSON.
 */
public class GameInitializer {

    public GameState initializeGame(Board board, Player player1, Player player2) {
        Position posTreasure1 = board.getRandomFreePosition();
        Position posTreasure2 = board.getRandomFreePosition();

        DynasticTreasure treasure1 = new DynasticTreasure(player1, posTreasure1);
        DynasticTreasure treasure2 = new DynasticTreasure(player2, posTreasure2);

        board.getCellAt(posTreasure1).placeTreasure(treasure1);
        board.getCellAt(posTreasure2).placeTreasure(treasure2);

        player1.setOwnTreasure(treasure1);
        player2.setOwnTreasure(treasure2);

        return new GameState(board, List.of(player1, player2));
    }

}