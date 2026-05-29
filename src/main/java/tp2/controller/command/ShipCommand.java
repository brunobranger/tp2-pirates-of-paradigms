package tp2.controller.command;

import tp2.model.GameState;

public interface ShipCommand {
    void execute(GameState gameState);
    boolean canExecute(GameState gameState);
}