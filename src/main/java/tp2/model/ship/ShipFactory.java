package tp2.model.ship;

import tp2.model.ship.type.*;

/**
 * Fábrica de barcos.
 *
 * Patrón: Factory Method — centraliza la creación de Ships según su tipo.
 * Evita que el cliente (GameController, config loader) dependa de las clases
 * concretas (Sloop, Frigate, etc.), respetando DIP.
 *
 * Alternativa: si en el futuro se necesitan familias de barcos por facción,
 * escalar a Abstract Factory.
 */
public class ShipFactory {

    private ShipFactory() {
        // No instanciable: es una clase utilitaria de creación
    }

    /**
     * Crea un barco del tipo indicado con el nombre dado.
     *
     * @param type tipo de barco
     * @param name nombre identificador único del barco
     * @return instancia concreta de Ship
     * @throws IllegalArgumentException si el tipo no es reconocido
     */
    public static Ship create(ShipType type, String name) {
        return switch (type) {
            case SLOOP    -> new Sloop(name);
            case FRIGATE  -> new Frigate(name);
            case GALLEON  -> new Galleon(name);
            case FLAGSHIP -> new Flagship(name);
        };
    }
}