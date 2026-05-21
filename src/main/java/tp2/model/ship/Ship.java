package tp2.model.ship;

import tp2.model.board.Position;
import tp2.model.resource.ResourcePool;
import tp2.model.treasure.DynasticTreasure;

import java.util.List;

/**
 * Clase abstracta base para todos los tipos de barco.
 *
 * Herencia (is-a): Balandra, Fragata, Galeón y BarcoInsignia extienden Ship.
 * Cada subclase define sus estadísticas base (Template Method implícito).
 *
 * Principio: LSP — cualquier Ship puede usarse donde se espera un Ship.
 * Principio: OCP — agregar un nuevo tipo de barco = nueva subclase, sin tocar Ship.
 *
 * UML: Player --agrupa--> List<Ship> (agregación, el Ship puede existir solo).
 *      Ship --transporta--> DynasticTreasure (asociación opcional).
 *
 * Estado de turno encapsulado en ShipTurnState para SRP.
 */
public abstract class Ship {

    // Identificación
    private final String name;

    // Estadísticas base (definidas por subclase)
    private int currentHp;
    private final int maxHp;
    private final int atk;
    private final int def;
    private final int mov;
    private final int rng;

    // Posición actual en el tablero
    private Position position;

    // Tesoro que transporta (null si ninguno)
    private DynasticTreasure carriedTreasure;

    // Estado interno de turno
    private boolean hasMovedThisTurn;
    private boolean hasAttackedThisTurn;

    // Efectos de estado activos (Strategy: cada efecto sabe cómo aplicarse)
    private final List<ShipEffect> activeEffects;

    protected Ship(String name, int maxHp, int atk, int def, int mov, int rng) {
        this.name = name;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.atk = atk;
        this.def = def;
        this.mov = mov;
        this.rng = rng;
        this.activeEffects = new java.util.ArrayList<>();
    }

    // -------------------------------------------------------------------------
    // Tipo de barco (Template Method / polimorfismo)
    // -------------------------------------------------------------------------

    /**
     * @return el tipo de este barco (para la vista y lógica especial)
     */
    public abstract ShipType getType();

    /**
     * @return true si este tipo de barco es "ágil" (Balandra o Fragata).
     *         Usado por el evento Viento Favorable.
     */
    public boolean isAgile() {
        return false; // sobreescribir en Balandra y Fragata
    }

    // -------------------------------------------------------------------------
    // Combate
    // -------------------------------------------------------------------------

    /**
     * Recibe daño luego de aplicar la defensa.
     * Daño efectivo = max(0, damage - DEF).
     * Si HP llega a 0, el barco queda en estado hundido.
     *
     * @param rawDamage daño bruto del atacante (su ATK)
     * @return daño efectivo aplicado
     */
    public int receiveDamage(int rawDamage) {
        int effectiveDamage = Math.max(0, rawDamage - def);
        currentHp = Math.max(0, currentHp - effectiveDamage);
        return effectiveDamage;
    }

    /**
     * @return true si el barco está hundido (currentHp <= 0)
     */
    public boolean isSunk() {
        return currentHp <= 0;
    }

    /**
     * Recupera HP (reparación en puerto o con madera).
     * No puede superar maxHp.
     *
     * @param amount cantidad de HP a recuperar
     */
    public void repairHp(int amount) {
        if (amount <= 0) {
            return;
        }
        currentHp = Math.min(maxHp, currentHp + amount);
    }

    // -------------------------------------------------------------------------
    // Movimiento y acciones por turno
    // -------------------------------------------------------------------------

    /**
     * Calcula el movimiento efectivo del barco considerando efectos activos
     * (tormenta, vendaval, viento favorable, etc.).
     *
     * @return cantidad de casillas que puede moverse este turno
     */
    public int getEffectiveMovement() {
        int effectiveMovement = mov;
        for (ShipEffect effect : activeEffects) {
            effectiveMovement = effect.modifyMovement(effectiveMovement);
        }
        return Math.max(0, effectiveMovement);
    }

    /**
     * Calcula el rango de disparo efectivo considerando efectos activos
     * (niebla, viento favorable, etc.).
     *
     * @return rango de disparo efectivo
     */
    public int getEffectiveRange() {
        int effectiveRange = rng;
        for (ShipEffect effect : activeEffects) {
            effectiveRange = effect.modifyRange(effectiveRange);
        }
        return Math.max(0, effectiveRange);
    }

    public int getEffectiveAttack() {
        int effectiveAttack = atk;
        for (ShipEffect effect : activeEffects) {
            effectiveAttack = effect.modifyAttack(effectiveAttack);
        }
        return Math.max(0, effectiveAttack);
    }

    /**
     * Marca que el barco se movió en este turno.
     */
    public void registerMove() {
        this.hasMovedThisTurn = true;
    }

    /**
     * Marca que el barco atacó en este turno.
     * Un barco que atacó no puede moverse este turno.
     */
    public void registerAttack() {
        this.hasAttackedThisTurn = true;
    }

    /**
     * @return true si el barco puede moverse (no atacó, no se movió, no está incendiado ni inmovilizado)
     */
    public boolean canMove() {
        if (isSunk()) {
            return false;
        }
        if (hasMovedThisTurn || hasAttackedThisTurn) {
            return false;
        }
        return activeEffects.stream()
                .noneMatch(ShipEffect::preventsMovement);
    }

    /**
     * @return true si el barco puede atacar (no atacó ya y no está incendiado)
     */
    public boolean canAttack() {
        if (isSunk()) {
            return false;
        }
        if (hasAttackedThisTurn) {
            return false;
        }
        return activeEffects.stream()
                .noneMatch(ShipEffect::preventsAttack);
    }

    /**
     * Reinicia el estado de turno del barco (llamado al inicio del turno del jugador).
     * También reduce un tick todos los efectos temporales activos y elimina los vencidos.
     */
    public void resetTurnState() {
        hasMovedThisTurn = false;
        hasAttackedThisTurn = false;

        for (ShipEffect effect : activeEffects) {
            effect.applyEndOfTurnDamage(this);
        }
        activeEffects.forEach(ShipEffect::tick);
        activeEffects.removeIf(ShipEffect::isExpired);
    }

    // -------------------------------------------------------------------------
    // Efectos de estado (Incendio, Inmovilización, etc.)
    // -------------------------------------------------------------------------

    /**
     * Agrega un efecto activo al barco.
     *
     * @param effect efecto a aplicar
     */
    public void addEffect(ShipEffect effect) {

        if (effect == null) {
            throw new IllegalArgumentException("Effect cannot be null");
        }
        activeEffects.add(effect);
    }

    /**
     * @return true si el barco tiene un incendio activo
     */
    public boolean isOnFire() {
        return activeEffects.stream()
                .anyMatch(effect -> effect.getType() == EffectType.FIRE);
    }

    /**
     * Extingue el incendio del barco (usando madera o llegando a puerto propio).
     */
    public void extinguishFire() {
        activeEffects.removeIf(
                effect -> effect.getType() == EffectType.FIRE
        );
    }

    // -------------------------------------------------------------------------
    // Tesoro
    // -------------------------------------------------------------------------

    /**
     * El barco recoge un tesoro dinástico.
     *
     * @param treasure tesoro a transportar
     * @throws IllegalStateException si ya transporta uno
     */
    public void pickUpTreasure(DynasticTreasure treasure) {

        if (treasure == null) {
            throw new IllegalArgumentException("Treasure cannot be null");
        }
        if (carriedTreasure != null) {
            throw new IllegalStateException("Ship already carries a treasure");
        }
        carriedTreasure = treasure;
    }

    /**
     * El barco suelta el tesoro que transporta (por hundimiento o entrega).
     *
     * @return el tesoro soltado, o null si no transportaba ninguno
     */
    public DynasticTreasure dropTreasure() {

        DynasticTreasure dropped = carriedTreasure;
        carriedTreasure = null;
        return dropped;
    }

    public boolean isCarryingTreasure() {
        return carriedTreasure != null;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public String getName() { return name; }
    public int getCurrentHp() { return currentHp; }
    public int getMaxHp() { return maxHp; }
    public int getAtk() { return atk; }
    public int getDef() { return def; }
    public int getMov() { return mov; }
    public int getRng() { return rng; }
    public Position getPosition() { return position; }
    public DynasticTreasure getCarriedTreasure() { return carriedTreasure; }
    public boolean hasMovedThisTurn() { return hasMovedThisTurn; }
    public boolean hasAttackedThisTurn() { return hasAttackedThisTurn; }
    public List<ShipEffect> getActiveEffects() { return List.copyOf(activeEffects); }

    public void setPosition(Position position) { this.position = position; }
}