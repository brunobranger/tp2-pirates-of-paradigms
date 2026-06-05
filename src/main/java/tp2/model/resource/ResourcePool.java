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
    private double costMultiplier = 1.0;
    private int costMultiplierTurnsRemaining = 0;

    /**
     * Inicializacion de constructor para recursos iniciales
     * @param initialGold debe ser positivo
     * @param initialWood debe ser positivo
     */
    public ResourcePool(int initialGold, int initialWood) {
        this.gold = validateGold(initialGold);
        this.wood = validateWood(initialWood);
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
    public boolean hasEnoughGold(int amount) { return gold >= effectiveCost(amount); }

    /**
     * @param amount cantidad de madera requerida
     * @return true si hay madera suficiente
     */
    public boolean hasEnoughWood(int amount) { return wood >= effectiveCost(amount); }

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
        int cost = effectiveCost(amount);
        if (!hasEnoughGold(amount)) throw new IllegalStateException("Not enough gold");
        gold -= cost;
    }

    /**
     * Gasta madera. Lanza excepción si no hay suficiente.
     *
     * @param amount cantidad a gastar
     * @throws IllegalStateException si la madera es insuficiente
     */
    public void spendWood(int amount) {
        int cost = effectiveCost(amount);
        if (!hasEnoughWood(amount)) throw new IllegalStateException("Not enough wood");
        wood -= cost;
    }

    /**
     * Agrega oro (por hundir barcos, capturar tesoros flotantes, etc.).
     *
     * @param amount cantidad a agregar
     */
    public void earnGold(int amount) {
        if (amount < 0) throw new IllegalArgumentException("The amount can't be negative");
        gold += amount;
    }

    /**
     * Agrega madera.
     *
     * @param amount cantidad a agregar
     */
    public void earnWood(int amount) {
        if (amount < 0) throw new IllegalArgumentException("The amount can't be negative");
        wood += amount;
    }

    /**
     * Aumenta temporalmente el costo de acciones (efecto Motín).
     * Hipótesis: documentar en informe cómo se implementa el modificador.
     *
     * @param multiplier factor multiplicador de costos
     * @param turns      duración en turnos
     */
    public void applyCostIncrease(double multiplier, int turns) {
        this.costMultiplier = multiplier;
        this.costMultiplierTurnsRemaining = turns;
    }

    /**
     * Llamado al finalizar cada turno para reducir la duración del efecto Motín.
     */
    public void tickCostMultiplier() {
        if (costMultiplierTurnsRemaining > 0) {
            costMultiplierTurnsRemaining--;
            if (costMultiplierTurnsRemaining == 0) {
                costMultiplier = 1.0;
            }
        }
    }

    private int effectiveCost(int base) {
        return (int) Math.ceil(base * costMultiplier);
    }

    private int validateWood(int initialWood) {
        if (initialWood < 0) throw new IllegalArgumentException("Invalid wood");
        return initialWood;
    }

    @Override
    public String toString() {
        return "Gold: " + gold + " | Wood: " + wood;}package tp2.model.resource;

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
        private double costMultiplier = 1.0;
        private int costMultiplierTurnsRemaining = 0;

        /**
         * Inicializacion de constructor para recursos iniciales
         * @param initialGold debe ser positivo
         * @param initialWood debe ser positivo
         */
        public ResourcePool(int initialGold, int initialWood) {
            this.gold = validateGold(initialGold);
            this.wood = validateWood(initialWood);
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
        public boolean hasEnoughGold(int amount) { return gold >= effectiveCost(amount); }

        /**
         * @param amount cantidad de madera requerida
         * @return true si hay madera suficiente
         */
        public boolean hasEnoughWood(int amount) { return wood >= effectiveCost(amount); }

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
            int cost = effectiveCost(amount);
            if (!hasEnoughGold(amount)) throw new IllegalStateException("Not enough gold");
            gold -= cost;
        }

        /**
         * Gasta madera. Lanza excepción si no hay suficiente.
         *
         * @param amount cantidad a gastar
         * @throws IllegalStateException si la madera es insuficiente
         */
        public void spendWood(int amount) {
            int cost = effectiveCost(amount);
            if (!hasEnoughWood(amount)) throw new IllegalStateException("Not enough wood");
            wood -= cost;
        }

        /**
         * Agrega oro (por hundir barcos, capturar tesoros flotantes, etc.).
         *
         * @param amount cantidad a agregar
         */
        public void earnGold(int amount) {
            if (amount < 0) throw new IllegalArgumentException("The amount can't be negative");
            gold += amount;
        }

        /**
         * Agrega madera.
         *
         * @param amount cantidad a agregar
         */
        public void earnWood(int amount) {
            if (amount < 0) throw new IllegalArgumentException("The amount can't be negative");
            wood += amount;
        }

        /**
         * Aumenta temporalmente el costo de acciones (efecto Motín).
         * Hipótesis: documentar en informe cómo se implementa el modificador.
         *
         * @param multiplier factor multiplicador de costos
         * @param turns      duración en turnos
         */
        public void applyCostIncrease(double multiplier, int turns) {
            this.costMultiplier = multiplier;
            this.costMultiplierTurnsRemaining = turns;
        }

        /**
         * Llamado al finalizar cada turno para reducir la duración del efecto Motín.
         */
        public void tickCostMultiplier() {
            if (costMultiplierTurnsRemaining > 0) {
                costMultiplierTurnsRemaining--;
                if (costMultiplierTurnsRemaining == 0) {
                    costMultiplier = 1.0;
                }
            }
        }

        private int effectiveCost(int base) {
            return (int) Math.ceil(base * costMultiplier);
        }

        private int validateWood(int initialWood) {
            if (initialWood < 0) throw new IllegalArgumentException("Invalid wood");
            return initialWood;
        }

        @Override
        public String toString() {
            return "Gold: " + gold + " | Wood: " + wood;}