package tp2.model.treasure;

import tp2.model.board.Position;

/**
 * Tesoro flotante generado por el evento "Tesoro flotante".
 * Distinto al DynasticTreasure: este es temporal, tiene un timer de desaparición
 * y otorga oro + reparación al capturarlo.
 * Principio: SRP — separar de DynasticTreasure porque tienen ciclo de vida y
 * propósito completamente distintos.
 */
public class FloatingTreasure {

    private final Position position;
    private final int turnsUntilDisappear; // desaparece si nadie lo reclama
    private final boolean claimed;

    public FloatingTreasure(Position position, int turnsUntilDisappear) {
        this.position = position;
        this.turnsUntilDisappear = turnsUntilDisappear;
        this.claimed = false;
    }

    /**
     * Avanza un turno. Si llegó a 0, el tesoro desaparece.
     */
    public void tick() {
        // turnsUntilDisappear--
    }

    /** @return true si el tesoro ya no está disponible (reclamado o expirado) */
    public boolean isGone() {
        // claimed || turnsUntilDisappear <= 0
        return false;
    }

    /**
     * Marca el tesoro como reclamado por un barco.
     * Las recompensas las aplica el GameController (SRP).
     */
    public void claim() {
        // claimed = true
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public Position getPosition() { return position; }
    public boolean isClaimed() { return claimed; }
    public int getTurnsUntilDisappear() { return turnsUntilDisappear; }
}