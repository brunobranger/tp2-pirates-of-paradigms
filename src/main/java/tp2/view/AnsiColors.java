// AnsiColors.java
package tp2.view;

/**
 * Centraliza los códigos de escape ANSI usados para dar color a la salida
 * por consola. Mantenerlos en un solo lugar evita strings mágicos repetidos
 * en BoardRenderer y ConsoleView, y facilita ajustar la paleta sin tocar
 * lógica de renderizado.

 * Nota: el soporte de ANSI depende de la terminal del usuario. Funciona en
 * la mayoría de terminales modernas en Linux/Mac y en Windows Terminal /
 * PowerShell reciente, pero puede no renderizar en el CMD clásico.
 */
public final class AnsiColors {

    private AnsiColors() {}

    public static final String RESET = "\u001B[0m";

    // Flotas: un color por jugador, distinguibles entre sí
    public static final String PLAYER_ONE = "\u001B[36m"; // cian
    public static final String PLAYER_TWO = "\u001B[35m"; // magenta

    // Terreno peligroso / bloqueante
    public static final String DANGER = "\u001B[31m"; // rojo

    // Objetivo / tesoros
    public static final String TREASURE = "\u001B[33m"; // amarillo

    // Resaltado de la última acción del jugador (movimiento)
    public static final String HIGHLIGHT = "\u001B[32m"; // verde

    /**
     * Envuelve un texto con el color dado y lo resetea al final,
     * para que el color no se "contagie" al resto de la línea.
     */
    public static String colorize(String text, String color) {
        return color + text + RESET;
    }
}