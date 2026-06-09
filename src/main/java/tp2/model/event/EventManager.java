package tp2.model.event;

import tp2.model.game.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Gestiona la frecuencia y selección de eventos aleatorios.
 *
 * Hipótesis de frecuencia (documentar en informe):
 *   - Un evento puede ocurrir al finalizar ciertos turnos (ej. cada 3 turnos).
 *   - Se elige al azar entre los eventos disponibles con probabilidades iguales.
 *
 * Patrón: el EventManager actúa como Simple Factory de GameEvent — elige
 * qué evento instanciar según RNG, sin que el controlador sepa los tipos concretos.
 * A su vez se eligió modelarlo como un Singleton ya que sólo debe existir una única
 * instancia durante el juego.
 *
 * Principio: OCP — registrar un nuevo evento = agregarlo a la lista AVAILABLE_EVENTS.
 */
public class EventManager {

    // Hipótesis: evento cada N turnos completados (turno = ambos jugadores jugaron)
    private static final int EVENT_FREQUENCY_TURNS = 3;

    private static EventManager instance;
    private final Random random;
    private int turnsSinceLastEvent;

    // Lista de eventos disponibles — agregar aquí para registrar nuevos (OCP)
    private final List<GameEvent> availableEvents;

    /** Objeto Singleton, tiene una única instancia durante la ejecución */
    private EventManager() {
        this.random = new Random();
        this.turnsSinceLastEvent = 0;
        this.availableEvents = new ArrayList<>(List.of(
                new Squall(),
                new Mutiny(),
                new FloatingTreasureEvent(),
                new Kraken(),
                new FavorableWind(),
                new DeckFire())
        );
    }

    /**
     * Obtiene una instancia del objeto Singleton EventManager
     * @return instancia de EventManager
     * */
    public static EventManager getInstance(){
        if (instance == null){
            instance = new EventManager();
        }
        return instance;
    }

    /**
     * Debe llamarse al finalizar cada turno completo (ambos jugadores).
     * Si se cumple la condición de frecuencia, retorna un evento a aplicar.
     *
     * @param gameState estado actual
     * @return evento a aplicar, o null si no corresponde este turno
     */
    public GameEvent checkAndGetEvent(GameState gameState) {

        turnsSinceLastEvent++;
        if (turnsSinceLastEvent >= EVENT_FREQUENCY_TURNS) {
            turnsSinceLastEvent = 0;
            return selectRandomEvent();
        }
        return null;
    }

    /**
     * Agrega un evento en la lista de los posibles
     * @param event evento a agregar
     * */
    public void addEvent(GameEvent event){
        if (event == null)
            throw new RuntimeException("Evento a agregar no puede ser nulo");
        this.availableEvents.add(event);
    }

    /**
     * Selecciona un evento al azar de la lista disponible.
     *
     * @return evento seleccionado
     */
    private GameEvent selectRandomEvent() {
        return availableEvents.get(random.nextInt(availableEvents.size()));
    }


}