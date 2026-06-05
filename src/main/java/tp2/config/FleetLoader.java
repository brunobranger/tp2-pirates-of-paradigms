package tp2.config;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

import tp2.model.player.Faction;
import tp2.model.player.Player;
import tp2.model.ship.ShipFactory;
import tp2.model.ship.ShipType;

/**
 * Responsable únicamente de construir jugadores y sus flotas.
 */
public class FleetLoader {
    private final JsonLoader jsonLoader;

    public FleetLoader(JsonLoader jsonLoader) {
        this.jsonLoader = jsonLoader;
    }

    public Player load(String fleetFileName, Faction faction) throws IOException {
        JsonNode root = jsonLoader.readJson("fleets/" + fleetFileName);

        Player player = new Player(faction.name(), faction);
        JsonNode ships = getShips(root, faction);

        if (ships == null) {
            return player;
        }

        for (JsonNode shipNode : ships) {
            ShipType type = ShipType.valueOf(shipNode.get("type").asText());
            String name = shipNode.get("name").asText();
            player.addShip(ShipFactory.create(type, name));
        }

        return player;
    }

    private JsonNode getShips(JsonNode root, Faction faction) {
        JsonNode fleets = root.get("fleets");
        if (fleets == null) return null;
        return fleets.get(faction.name());
    }
}