package tp2.model.event;

import java.util.List;

/**
 * Resultado de aplicar un evento. Value Object.
 * Contiene la información que debe mostrarse al usuario según el enunciado:
 *   - qué evento ocurrió
 *   - qué barcos/jugadores fueron afectados
 *   - duración del efecto
 *   - consecuencias aplicadas
 */
public class EventResult {

    private final String eventName;
    private final List<String> affectedEntities;
    private final int durationTurns;
    private final List<String> consequences;

    public EventResult(String eventName, List<String> affectedEntities,
                       int durationTurns, List<String> consequences) {
        this.eventName = eventName;
        this.affectedEntities = List.copyOf(affectedEntities);
        this.durationTurns = durationTurns;
        this.consequences = List.copyOf(consequences);
    }

    public String getEventName() { return eventName; }
    public List<String> getAffectedEntities() { return affectedEntities; }
    public int getDurationTurns() { return durationTurns; }
    public List<String> getConsequences() { return consequences; }
}