package tp2.model.treasure;

import tp2.model.board.Position;
import tp2.model.player.Player;
import tp2.model.ship.Ship;

import static tp2.model.resource.ResourceCosts.FLOATING_TREASURE_GOLD;
import static tp2.model.resource.ResourceCosts.FLOATING_TREASURE_HP_REPAIR;

/**
 * Generado por el evento "Tesoro flotante".
 * Es temporal, tiene un timer de desaparición
 */
public class FloatingTreasure implements Treasure{

    private final Position position;
    private int turnsUntilDisappear;
    private boolean claimed;

    public FloatingTreasure(Position position, int turnsUntilDisappear) {
        this.position = position;
        this.turnsUntilDisappear = turnsUntilDisappear;
        this.claimed = false;
    }

    // -------------------------------------------------------------------------
    // Interfaz Treasure
    // -------------------------------------------------------------------------
    public TreasureType getType(){
        return TreasureType.FLOATING;
    }

    public void onPickUp(Ship ship, Player player){
        player.addGold(FLOATING_TREASURE_GOLD);
        ship.repairHp(FLOATING_TREASURE_HP_REPAIR);
    }

    // -------------------------------------------------------------------------
    // Behavior methods
    // -------------------------------------------------------------------------

    /**
     * Avanza un turno. Si llegó a 0, el tesoro desaparece.
     */
    public void tick() {
        setTurnsUntilDisappear();
    }

    /**
     * Marca el tesoro como reclamado por un barco.
     * Las recompensas las aplica el GameController (SRP).
     */
    public void claim() {
        setClaimed();
    }

    // -------------------------------------------------------------------------
    // State methods
    // -------------------------------------------------------------------------

    /**
     * @return es true si es reclamado, false contrariamente.
     */
    public boolean isClaimed() { return claimed; }

    /** @return true si el tesoro ya no está disponible (reclamado o expirado) */
    public boolean isGone() {
        return getTurnsUntilDisappear() == 0 || isClaimed();
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Devuelve la posicion del tesoro flotante.
     * @return posicion del tesoro flotante
     */
    public Position getPosition() { return position; }

    /**
     * Devuelve la cantidad de la turnera actual
     * @return cantidad neta de turnos restantes
     */
    public int getTurnsUntilDisappear() { return turnsUntilDisappear; }

    // -------------------------------------------------------------------------
    // Setters
    // -------------------------------------------------------------------------

    /**
     * Establece el estado del tesoro flotante en verdadero.
     */
    private void setClaimed() {
        this.claimed = !isClaimed();
    }

    /**
     * Disminuye el contador de turnos hasta
     * desaparecer el tesoro flotante.
     */
    private void setTurnsUntilDisappear() {
        this.turnsUntilDisappear--;
    }
}