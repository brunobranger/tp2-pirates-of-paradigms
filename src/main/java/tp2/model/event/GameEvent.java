package tp2.model.event;

import tp2.model.game.GameState;

/**
 * Interfaz para todos los eventos aleatorios del juego.
 *
 * Patrón: Strategy — cada evento encapsula su lógica de aplicación.
 * El GameController no sabe qué tipo de evento ocurre, solo llama apply().
 *
 * Patrón: también se puede ver como Command — el evento es una acción
 * diferida que se ejecuta sobre el GameState.
 *
 * Principio: OCP — agregar un evento nuevo = nueva clase que implementa GameEvent,
 * sin modificar el sistema de eventos existente.
 *
 * UML: GameController --usa--> GameEvent (dependencia).
 *      GameEvent <|-- Squall, Mutiny, FloatingTreasureEvent, Kraken, FavorableWind, DeckFire
 */
public interface GameEvent {

    /**
     * @return nombre legible del evento (para mostrar en consola)
     */
    String getName();

    /**
     * @return descripción del efecto que ocurrirá (para mostrar antes de aplicar)
     */
    String getDescription();

    /**
     * Aplica el efecto del evento sobre el estado del juego.
     * Modifica barcos, tablero y/o recursos según corresponda.
     *
     * @param gameState estado actual del juego (acceso a tablero, jugadores, etc.)
     * @return resumen del evento para mostrar al usuario (qué ocurrió, a quién)
     */
    EventResult apply(GameState gameState);

    /**
     * @return duración en turnos del efecto (0 si es instantáneo)
     */
    int getDurationInTurns();
}