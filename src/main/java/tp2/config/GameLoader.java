package tp2.config;

import java.io.IOException;

import tp2.model.game.GameState;
import tp2.model.board.Board;
import tp2.model.player.Faction;
import tp2.model.player.Player;

/**
 * Carga el estado inicial del juego desde archivos de configuración.
 *
 * Responsabilidades:
 *   - Leer el archivo de mapa y construir el Board.
 *   - Leer el archivo de flotas y construir las listas de Ships por jugador.
 *   - Construir el GameState inicial.
 *
 * Principio: SRP — separado del modelo y del controlador; solo lee y construye.
 * Principio: DIP — el controlador depende de GameLoader (abstracción),
 *                   no del formato de archivo concreto.
 *
 * Hipótesis de formato (documentar en informe):
 *   - Mapa: JSON con filas, columnas y lista de celdas especiales.
 *   - Flotas: JSON con lista de barcos por bando (tipo y nombre).
 *
 * Ejemplo de mapa JSON (resources/maps/default.json):
 * {
 *   "rows": 10,
 *   "cols": 10,
 *   "cells": [
 *     {"row": 2, "col": 3, "terrain": "REEF"},
 *     {"row": 5, "col": 5, "terrain": "ISLAND"}
 *   ]
 * }
 */
public class GameLoader {

    private final String resourcesBasePath;

    public GameLoader(String resourcesBasePath) {
        this.resourcesBasePath = resourcesBasePath;
    }

    /**
     * Carga el juego completo a partir de los archivos seleccionados.
     *
     * @param mapFileName   nombre del archivo de mapa (ej. "default.json")
     * @param fleetFileName nombre del archivo de flotas (ej. "balanced.json")
     * @return GameState inicial listo para comenzar (sin puertos ni barcos colocados aún)
     * @throws IOException si no se puede leer algún archivo
     */
    public GameState loadGame(String mapFileName, String fleetFileName) throws IOException {
        // 
        // Board board = loadBoard(mapFileName)
        // Player p1 = loadPlayer(fleetFileName, Faction.CRIMSON_BROTHERHOOD)
        // Player p2 = loadPlayer(fleetFileName, Faction.ABYSS_CROWN)
        // Tesoros: posición aleatoria, se ubican antes del inicio
        // return new GameState(board, p1, p2, treasure1, treasure2)
        return null;
    }

    /**
     * Construye el tablero desde el archivo de mapa.
     *
     * @param mapFileName nombre del archivo
     * @return Board configurado
     * @throws IOException si no se puede leer el archivo
     */
    private Board loadBoard(String mapFileName) throws IOException {
        //  parsear JSON, crear Board(rows, cols), setear terrenos especiales
        return null;
    }

    /**
     * Construye un jugador con su flota según el archivo de configuración.
     *
     * @param fleetFileName nombre del archivo
     * @param faction       facción del jugador
     * @return Player con su flota lista (sin posicionar)
     * @throws IOException si no se puede leer el archivo
     */
    private Player loadPlayer(String fleetFileName, Faction faction) throws IOException {
        //  parsear JSON, crear Player, agregar Ships con ShipFactory.create(type, name)
        return null;
    }

    /**
     * Retorna la lista de archivos de mapa disponibles en resources/maps/.
     *
     * @return nombres de archivos disponibles
     */
    public java.util.List<String> getAvailableMaps() {
        //  listar archivos en resourcesBasePath + "/maps/"
        return java.util.List.of();
    }

    /**
     * Retorna la lista de archivos de flotas disponibles en resources/fleets/.
     *
     * @return nombres de archivos disponibles
     */
    public java.util.List<String> getAvailableFleets() {
        //  listar archivos en resourcesBasePath + "/fleets/"
        return java.util.List.of();
    }
}