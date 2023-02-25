package kalah.SeedStorage;

import kalah.*;
import kalah.Action.Action;
import kalah.Action.SwitchPlayerAction;
import kalah.KalahException.InvalidInputException;
import kalah.KalahException.StorageNotFoundException;

import java.util.List;
import java.util.Map;

public class Store extends SeedStorage {
    public Store(Player player) {
        super(0, player);
    }

    public void connectToHouse(SeedStorage house) {
        super.setNext(house);
    }

    @Override
    public boolean checkEmptyPlayerHouses() {
        return true;
    }

    @Override
    public Action createAction(int numPlayerHouses, Player currentPlayer, Store currentPlayerStore, List<Player> players, Map<Player, SeedStorage> firstStorageMap) throws StorageNotFoundException {
        if (currentPlayerStore.equals(this)) {
            return new SwitchPlayerAction(currentPlayer);
        }
        Player nextPlayer = players.get((players.indexOf(currentPlayer) + 1) % 2);
        return new SwitchPlayerAction(nextPlayer);
    }

    @Override
    public SeedStorage getSeedStorageOfId(int id) throws StorageNotFoundException {
        throw new StorageNotFoundException("Storage of " + id + " was not found.");
    }

    @Override
    public SeedStorage reap(int targetHouseId) throws InvalidInputException {
        throw new InvalidInputException("Input house not found.");
    }

    @Override
    public SeedStorage sow(int seedsToSow, Player sourcePlayer) {
        if (compareOwners(sourcePlayer)) {
            return super.sow(seedsToSow, sourcePlayer);
        } else {
            return _next.sow(seedsToSow, sourcePlayer);
        }
    }

    /**
     * 执行capture 将对面house中的seeds和自己house的seeds加入到自己的store中
     *
     * @param lastSeedStorage
     * @param oppositeSeedStorage
     */
    public void capture(SeedStorage lastSeedStorage, SeedStorage oppositeSeedStorage) {
        _seeds.storeSeeds(lastSeedStorage.harvestSeeds() + oppositeSeedStorage.harvestSeeds());
    }
}