package tp2.util;

import java.util.List;
import java.util.Random;

/**
 * Utilidades para operaciones aleatorias.
 * Centralizadas aquí para facilitar el testing (se puede inyectar un Random con seed fija).
 */
public final class RandomUtils {

    private static final Random RANDOM = new Random();

    private RandomUtils() {}

    /**
     * Retorna un elemento aleatorio de la lista.
     *
     * @param list lista no vacía
     * @param <T>  tipo de elemento
     * @return elemento aleatorio
     * @throws IllegalArgumentException si la lista está vacía
     */
    public static <T> T pickRandom(List<T> list) {
        if (list.isEmpty()) throw new IllegalArgumentException("Lista vacía");
        return list.get(RANDOM.nextInt(list.size()));
    }

    /**
     * @return true o false al azar (para sorteo inicial de quién comienza)
     */
    public static boolean coinFlip() {
        return RANDOM.nextBoolean();
    }

    /**
     * @param bound límite superior exclusivo
     * @return entero aleatorio en [0, bound)
     */
    public static int nextInt(int bound) {
        return RANDOM.nextInt(bound);
    }
}