package tp2;

import tp2.config.GameLoader;
import tp2.controller.GameController;
import tp2.model.combat.CombatSystem;
import tp2.model.event.EventManager;
import tp2.model.game.GameState;
import tp2.view.ConsoleView;
import tp2.view.GameView;

public class Main {

    public static void main(String[] args) {
        GameView view = new ConsoleView();
        GameLoader loader = new GameLoader();

        try {
            String mapFile = view.askMapFile();
            String fleetFile = view.askFleetFile();
            EventManager eventManager = EventManager.getInstance();

            GameState gameState = loader.loadGame(mapFile, fleetFile);
            CombatSystem combatSystem = new CombatSystem(gameState.getBoard());

            GameController controller = new GameController(gameState, view, combatSystem, eventManager);

            controller.start();

        } catch (Exception e) {
            //System.err.println("Error initiating game: " + e.getMessage());
            e.printStackTrace();
        }
    }
}