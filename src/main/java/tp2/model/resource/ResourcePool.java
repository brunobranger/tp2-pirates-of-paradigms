package tp2.model.resource;

import tp2.config.GameConfig;

/**
 * Pool de recursos global de un jugador: oro y madera.
 *
 * Principio: SRP — esta clase solo gestiona los recursos, no las reglas de qué
 * acción cuesta qué (eso lo sabe quien invoca: GameController o las acciones).
 *
 * Principio: Tell Don't Ask — expone mé...s de acción (spend, earn) en lugar
 * de solo getters para que el cliente calcule si alcanza.
 */
public class ResourcePool {

    private int gold;
    private int wood;

    /**
     * Inicializacion de constructor para recursos iniciales
     * @param initialGold debe ser positivo
     * @param initialWood debe ser positivo
     */
    public ResourcePool(int initialGold, int initialWood) {
        this.gold = setGold(initialGold);
        this.wood = setWood(initialWood);
    }

    /**
     * Sobrecarga de constructor para recursos predeterminados
     */
    public ResourcePool() {
        this.gold = GameConfig.INITIAL_GOLD;
        this.wood = GameConfig.INITIAL_WOOD;
    }

    // -------------------------------------------------------------------------
    // Consultas
    // -------------------------------------------------------------------------

    public int getGold() { return gold; }
    public int getWood() { return wood; }

    /**
     * @param amount cantidad de oro requerida
     * @return true si hay oro suficiente
     */
    public boolean hasEnoughGold(int amount) { return gold >= amount; }

    /**
     * @param amount cantidad de madera requerida
     * @return true si hay madera suficiente
     */
    public boolean hasEnoughWood(int amount) { return wood >= amount; }

    // -------------------------------------------------------------------------
    // Modificaciones
    // -------------------------------------------------------------------------

    /**
     * Gasta oro. Lanza excepción si no hay suficiente.
     *
     * @param amount cantidad a gastar
     * @throws IllegalStateException si el oro es insuficiente
     */
    public void spendGold(int amount) {
        // validar y descontar
    }

    /**
     * Gasta madera. Lanza excepción si no hay suficiente.
     *
     * @param amount cantidad a gastar
     * @throws IllegalStateException si la madera es insuficiente
     */
    public void spendWood(int amount) {
        // validar y descontar
    }

    /**
     * Agrega oro (por hundir barcos, capturar tesoros flotantes, etc.).
     *
     * @param amount cantidad a agregar
     */
    public void earnGold(int amount) {
        // ...
    }

    /**
     * Agrega madera.
     *
     * @param amount cantidad a agregar
     */
    public void earnWood(int amount) {
        // ...
    }

    /**
     * Aumenta temporalmente el costo de acciones (efecto Motín).
     * Hipótesis: documentar en informe cómo se implementa el modificador.
     *
     * @param multiplier factor multiplicador de costos
     * @param turns      duración en turnos
     */
    public void applyCostIncrease(double multiplier, int turns) {
        // ...
    }

    private int setWood(int initialWood) {
        if (initialWood < 0) {
            throw new RuntimeException("Invalid Wood");
        }
        return initialWood;
    }

    private int setGold(int initialGold) {
        if (initialGold < 0) {
            throw new RuntimeException("Invalid Gold");
        }
        return initialGold;
    }

    @Override
    public String toString() {
        return "Oro: " + getGold() + " | Madera: " + getWood();
    }
}