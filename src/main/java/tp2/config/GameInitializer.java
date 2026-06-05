package tp2.config;

import tp2.model.board.Board;
import tp2.model.game.GameState;
import tp2.model.player.Player;
import tp2.model.treasure.DynasticTreasure;

/**
 * Responsable de crear una partida nueva válida.
 * No lee archivos ni parsea JSON.
 */
public class GameInitializer {

    public GameState initializeGame(Board board, Player player1, Player player2) {
        DynasticTreasure treasure1 = new DynasticTreasure(player1, board.getRandomFreePosition());
        DynasticTreasure treasure2 = new DynasticTreasure(player2, board.getRandomFreePosition());
        player1.setOwnTreasure(treasure1);
        player2.setOwnTreasure(treasure2);

        // Quizas podriamos aplicar Patron Builder aca, pero lo veo un toque innecesario porque no son tantos parametros
        // (y hay solamente dos de ellos que los genera el GameInitializer)
        return new GameState(board, player1, player2, treasure1, treasure2);
    }
}