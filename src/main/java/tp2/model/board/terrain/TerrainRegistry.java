package tp2.model.board.terrain;

import tp2.model.board.TerrainType;

import java.util.EnumMap;
import java.util.Map;

public class TerrainRegistry {
    private final Map<TerrainType, Terrain> terrains;

    public TerrainRegistry(Map<TerrainType, Terrain> terrains) {
        this.terrains = new EnumMap<>(terrains);
    }

    public Terrain resolve(TerrainType terrainType) {
        Terrain terrain = terrains.get(terrainType);
        if (terrain == null) {
            throw new IllegalArgumentException("No terrain registered for type: " + terrainType);
        }
        return terrain;
    }

    public static TerrainRegistry defaultRegistry() {
        Map<TerrainType, Terrain> terrainMap = new EnumMap<>(TerrainType.class);
        terrainMap.put(TerrainType.OPEN_SEA, new OpenSeaTerrain());
        terrainMap.put(TerrainType.STORM, new StormTerrain());
        terrainMap.put(TerrainType.PORT, new PortTerrain());
        terrainMap.put(TerrainType.FOG, new FogTerrain());
        terrainMap.put(TerrainType.REEF, new ReefTerrain());
        terrainMap.put(TerrainType.MARINE_CURRENT, new MarineCurrentTerrain());
        terrainMap.put(TerrainType.ISLAND, new IslandTerrain());
        return new TerrainRegistry(terrainMap);
    }
}
