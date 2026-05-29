package tp2.model.event;

import tp2.model.game.GameState;

import java.util.List;

// =============================================================================
// VENDAVAL
// =============================================================================

/**
 * Vendaval: reduce movimiento a 1, daño leve, convierte casillas en tormenta.
 */
class Squall implements GameEvent {

    private static final int DURATION = 1; // próximo turno
    private static final int LIGHT_DAMAGE = 5;

    @Override
    public String getName() { return "Vendaval"; }

    @Override
    public String getDescription() {
        return "Un vendaval azota el campo de batalla. Movimiento reducido a 1, daño leve a todos los barcos.";
    }

    @Override
    public EventResult apply(GameState gameState) {
        //
        // 1. Aplicar ReducedMovementEffect(1) a todos los barcos de ambos jugadores
        // 2. Aplicar LIGHT_DAMAGE a cada barco afectado
        // 3. Convertir algunas casillas (hipótesis: zona central) a STORM temporalmente
        // 4. Construir y retornar EventResult
        return new EventResult(getName(), List.of(), DURATION, List.of());
    }

    @Override
    public int getDurationInTurns() { return DURATION; }
}

// =============================================================================
// MOTÍN
// =============================================================================

/**
 * Motín: un barco pierde su próxima acción, costos aumentan, ATK reducido.
 * Requiere gastar oro para estabilizar.
 */
class Mutiny implements GameEvent {

    private static final int DURATION = 2;

    @Override
    public String getName() { return "Motín"; }

    @Override
    public String getDescription() {
        return "¡Problemas internos! Un barco pierde su acción, los costos aumentan y el ataque se reduce.";
    }

    @Override
    public EventResult apply(GameState gameState) {
        //
        // 1. Seleccionar al azar un barco del jugador activo
        // 2. Aplicar LostActionEffect al barco seleccionado
        // 3. Aplicar ReducedAttackEffect a la flota
        // 4. gameState.getCurrentPlayer().getResources().applyCostIncrease(1.5, DURATION)
        // 5. Construir y retornar EventResult
        return new EventResult(getName(), List.of(), DURATION, List.of());
    }

    @Override
    public int getDurationInTurns() { return DURATION; }
}

// =============================================================================
// TESORO FLOTANTE
// =============================================================================

/**
 * Aparece un cargamento abandonado en una casilla libre.
 * Otorga oro y reparación al capturarlo. Desaparece tras N turnos.
 */
class FloatingTreasureEvent implements GameEvent {

    private static final int DISAPPEAR_AFTER_TURNS = 3;

    @Override
    public String getName() { return "Tesoro Flotante"; }

    @Override
    public String getDescription() {
        return "¡Un cargamento abandonado aparece a la deriva! Captúralo antes de que desaparezca.";
    }

    @Override
    public EventResult apply(GameState gameState) {
        //
        // 1. Obtener posición libre aleatoria: gameState.getBoard().getRandomFreePosition()
        // 2. Crear FloatingTreasure en esa posición con timer DISAPPEAR_AFTER_TURNS
        // 3. Registrar el FloatingTreasure en GameState
        // 4. Construir y retornar EventResult
        return new EventResult(getName(), List.of(), DISAPPEAR_AFTER_TURNS, List.of());
    }

    @Override
    public int getDurationInTurns() { return DISAPPEAR_AFTER_TURNS; }
}

// =============================================================================
// KRAKEN
// =============================================================================

/**
 * Kraken: bloquea 9 casillas, ataca barcos cercanos (HP a la mitad),
 * inmoviliza temporalmente una embarcación.
 */
class Kraken implements GameEvent {

    private static final int DURATION = 3;

    @Override
    public String getName() { return "Kraken Marino"; }

    @Override
    public String getDescription() {
        return "¡El Kraken emerge de las profundidades! Bloquea una zona y ataca a los barcos cercanos.";
    }

    @Override
    public EventResult apply(GameState gameState) {
        //
        // 1. Elegir posición aleatoria libre para el Kraken
        // 2. Marcar la casilla del Kraken y sus 8 adyacentes como bloqueadas (KrakenBlockedCell o similar)
        // 3. Barcos en casillas aledañas: reducir HP a la mitad
        // 4. Inmovilizar un barco cercano con ImmobilizedEffect(1)
        // 5. Registrar el Kraken en GameState para que persista DURATION turnos
        return new EventResult(getName(), List.of(), DURATION, List.of());
    }

    @Override
    public int getDurationInTurns() { return DURATION; }
}

// =============================================================================
// VIENTO FAVORABLE
// =============================================================================

/**
 * Viento Favorable: +2 MOV a barcos ágiles, +1 RNG a todos, moverse Y atacar en el mismo turno.
 */
class FavorableWind implements GameEvent {

    private static final int DURATION = 1;

    @Override
    public String getName() { return "Viento Favorable"; }

    @Override
    public String getDescription() {
        return "¡Vientos favorables! Balandras y fragatas ganan movimiento, todos ganan alcance.";
    }

    @Override
    public EventResult apply(GameState gameState) {
        //
        // 1. A barcos ágiles (isAgile()): FavorableWindEffect con +2 MOV
        // 2. A todos los barcos: +1 RNG este turno
        // 3. Activar flag en GameState que permita moverse y atacar en el mismo turno este turno
        return new EventResult(getName(), List.of(), DURATION, List.of());
    }

    @Override
    public int getDurationInTurns() { return DURATION; }
}

// =============================================================================
// INCENDIO EN CUBIERTA
// =============================================================================

/**
 * Incendio en cubierta: afecta a un barco aleatorio.
 * El barco pierde HP por turno y no puede atacar hasta que se apague.
 *
 * Nota: este evento aplica FireEffect sobre un barco;
 * la acción "apagar incendio" se gestiona en GameController.
 */
class DeckFire implements GameEvent {

    @Override
    public String getName() { return "Incendio en Cubierta"; }

    @Override
    public String getDescription() {
        return "¡Fuego a bordo! Un barco está en llamas. Usa madera o llega a puerto para apagarlo.";
    }

    @Override
    public EventResult apply(GameState gameState) {
        //
        // 1. Seleccionar un barco aleatorio (hipótesis: del jugador activo, o de cualquiera — documentar)
        // 2. ship.addEffect(new FireEffect())
        // 3. Retornar EventResult indicando qué barco está en llamas
        return new EventResult(getName(), List.of(), 0, List.of());
    }

    @Override
    public int getDurationInTurns() { return 0; } // permanente hasta apagarse
}