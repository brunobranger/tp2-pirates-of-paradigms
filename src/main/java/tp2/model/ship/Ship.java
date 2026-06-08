package tp2.model.ship;

import tp2.model.board.Position;
import tp2.model.ship.effect.ShipEffect;
import tp2.model.treasure.DynasticTreasure;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta base para todos los tipos de barco.
 */
public abstract class Ship {

    private final String name;
    private int currentHp;
    private final int maxHp;
    private final int atk;
    private final int def;
    private final int mov;
    private final int rng;
    private Position position;
    private DynasticTreasure carriedTreasure;
    private boolean hasMovedThisTurn;
    private boolean hasAttackedThisTurn;
    private final List<ShipEffect> activeEffects;

    protected Ship(String name, int maxHp, int atk, int def, int mov, int rng) {
        this.name = name;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.atk = atk;
        this.def = def;
        this.mov = mov;
        this.rng = rng;
        this.activeEffects = new ArrayList<>();
    }

    // -------------------------------------------------------------------------
    // Tipo de barco
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
        return false;
    }

    // -------------------------------------------------------------------------
    // Combate
    // -------------------------------------------------------------------------

    /**
     * Recibe daño luego de aplicar la defensa.
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

    public boolean isInRange(Ship other, int range) {
        return position.distanceTo(other.getPosition()) <= range;
    }

    // -------------------------------------------------------------------------
    // Movimiento y acciones por turno
    // -------------------------------------------------------------------------

    /**
     * Calcula el movimiento efectivo del barco considerando efectos activos
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
    // Efectos de estado
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

    // -------------------------------------------------------------------------
    // Setters
    // -------------------------------------------------------------------------

    public void setPosition(Position position) { this.position = position; }
}

