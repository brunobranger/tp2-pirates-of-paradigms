package tp2.model.treasure;

import tp2.model.player.Player;
import tp2.model.ship.Ship;

public interface TreasureCollectionReporter {
    void reportDynasticTreasureCollected(Ship ship, Player owner);
    void reportFloatingTreasureCollected(Ship ship, int goldGained, int hpRecovered);
}
