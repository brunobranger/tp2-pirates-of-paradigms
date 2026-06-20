package tp2.model.event;

import tp2.model.game.GameState;
import tp2.model.board.Position;
import tp2.model.ship.effect.*;

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
    private static final float AFFECTED_BOARD_AREA_PERCENT = 0.20f;

    @Override
    public String getName() { return "Vendaval"; }

    @Override
    public String getDescription() {
        return "Un vendaval azota el campo de batalla. Movimiento reducido a 1, daño leve a todos los barcos.";
    }

    @Override
    public EventResult apply(GameState gameState) {
        gameState.applyEffectToAllShips(new ReducedMovementEffect(1));
        List<Position> stormArea = gameState.applyCentralStorm(AFFECTED_BOARD_AREA_PERCENT, DURATION);
        List<String> affectedShips = gameState.applyDamageToShipsInArea(stormArea, LIGHT_DAMAGE);
        List<String> consequences = List.of(
                "Movimiento de todos los barcos reducido a 1",
                "Los barcos listados reciben " + LIGHT_DAMAGE + " de daño",
                "Casillas centrales convertidas en Tormenta" );

        return new EventResult(getName(), affectedShips, DURATION, consequences);
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
    private static final double ATTACK_MULTIPLIER = 0.75;
    private static final double COST_MULTIPLIER = 1.5;

    @Override
    public String getName() { return "Motín"; }

    @Override
    public String getDescription() {
        return "¡Problemas internos! Un barco pierde su acción, los costos aumentan y el ataque se reduce.";
    }

    @Override
    public EventResult apply(GameState gameState) {
        gameState.applyEffectToCurrentPlayerFleet(new ReducedAttackEffect(DURATION, ATTACK_MULTIPLIER));
        String affectedShip = gameState.applyEffectToCurrentPlayerRandomShip(new LostActionEffect(1));
        gameState.applyCostIncreaseToCurrentPlayer(COST_MULTIPLIER, DURATION);
        List<String> consequences = List.of(
                "La flota del jugador actual reduce su ataque a " + (int) (ATTACK_MULTIPLIER * 100) + "%",
                affectedShip + " pierde la próxima acción",
                "Los costos del jugador se multiplican por " + COST_MULTIPLIER );
        return new EventResult(getName(), List.of(affectedShip), DURATION, consequences);
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
        Position position = gameState.spawnFloatingTreasure(DISAPPEAR_AFTER_TURNS);
        List<String> affected = List.of("Casilla " + position.toString());
        List<String> consequences = List.of(
                "Apareció un botín abandonado a la deriva.",
                "Otorga Oro y Reparación al recogerlo.",
                "Desaparecerá en " + DISAPPEAR_AFTER_TURNS + " turnos si no es reclamado."
        );

        return new EventResult(getName(), affected, DISAPPEAR_AFTER_TURNS, consequences);
    }

    @Override
    public int getDurationInTurns() { return DISAPPEAR_AFTER_TURNS; }
}

// =============================================================================
// VIENTO FAVORABLE
// =============================================================================

/**
 * Viento Favorable: +2 MOV a barcos ágiles, +1 RNG a todos, moverse Y atacar en el mismo turno.
 */
class FavorableWind implements GameEvent {

    private static final int DURATION = 1;
    private static final int MOVEMENT_BONUS = 2;
    private static final int RANGE_BONUS = 1;

    @Override
    public String getName() { return "Viento Favorable"; }

    @Override
    public String getDescription() {
        return "¡Vientos favorables! Balandras y fragatas ganan movimiento, todos ganan alcance.";
    }

    @Override
    public EventResult apply(GameState gameState) {
        gameState.applyEffectToAllAgileShips(new FavorableWindEffect(DURATION, MOVEMENT_BONUS, RANGE_BONUS));
        gameState.applyEffectToAllNonAgileShips(new FavorableWindEffect(DURATION, 0, RANGE_BONUS));

        List<String> consequences = List.of(
                "Barcos ágiles reciben +" + MOVEMENT_BONUS + " MOV",
                "Todos los barcos reciben +" + RANGE_BONUS + " RNG",
                "Todos pueden mover y atacar en el mismo turno"
        );

        return new EventResult(getName(), List.of("Balandras y Fragatas (Movimiento)/Todos (Rango)"), DURATION, consequences);
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

    private final int DAMAGE_PER_TURN = 3;

    @Override
    public String getName() { return "Incendio en Cubierta"; }

    @Override
    public String getDescription() {
        return "¡Fuego a bordo! Un barco está en llamas. Usa madera o llega a puerto para apagarlo.";
    }

    @Override
    public EventResult apply(GameState gameState) {
        String affected = gameState.applyEffectToCurrentPlayerRandomShip(new FireEffect(DAMAGE_PER_TURN));
        List<String> consequences = List.of(
                "El barco se incendia, se debe reparar utilizando madera o llegar a un puerto propio",
                "Pierde " + DAMAGE_PER_TURN + " HP por turno hasta que se apague"
        );

        return new EventResult(getName(), List.of(affected), 0, consequences);
    }

    @Override
    public int getDurationInTurns() { return 0; }
}


// =============================================================================
// KRAKEN
// =============================================================================

/**
 * Kraken:
 * Permanece varios turnos hasta desaparecer.
 * Bloquea la casilla donde se encuentra y las 8 aledañas.
 * Ataca a los barcos en su cercanía, reduciendo su HP a la mitad.
 * Inmoviliza temporalmente una embarcación.
 */
class Kraken implements GameEvent {

    private static final int DURATION = 3;
    private static final double DAMAGE_FACTOR = 0.5;
    private static final int BLOCKED_AREA_RADIUS = 1;
    private static final int DAMAGE_AREA_RADIUS = 3;

    @Override
    public String getName() { return "Kraken"; }

    @Override
    public String getDescription() {
        return "El Kraken emerge desde las aguas profundas!";
    }

    @Override
    public EventResult apply(GameState gameState) {

        Position position = gameState.spawnKraken(DURATION);
        List<Position> blockedArea = gameState.getBoardSquareArea(position, BLOCKED_AREA_RADIUS);
        List<Position> damageArea = gameState.getBoardSquareArea(position, DAMAGE_AREA_RADIUS);
        blockedArea.remove(position);
        gameState.applyKrakenBlockedArea(blockedArea, DURATION);
        List<String> damagedShips = gameState.applyDamageFactorToShipsInArea(damageArea, DAMAGE_FACTOR);
        String immobilizedShip = gameState.applyEffectToRandomShipInArea(damageArea, new ImmobilizedEffect(DURATION));
        List<String> consequences = List.of(
                "Se bloquean las casillas aledañas al Kraken",
                "Los barcos listados reducen su HP a la mitad",
                immobilizedShip + " se encuentra inmovilizado por " + DURATION + " turnos"
        );

        return new EventResult(getName(), damagedShips, DURATION, consequences);
    }

    @Override
    public int getDurationInTurns() { return DURATION; }
}
