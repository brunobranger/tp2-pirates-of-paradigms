package tp2.controller.command;

import tp2.model.game.GameState;

public interface ShipCommand {
    void execute(GameState gameState);
    boolean canExecute(GameState gameState);
}