package kalah.SeedStorage;

import kalah.*;
import kalah.Action.Action;
import kalah.KalahException.InvalidInputException;
import kalah.KalahException.StorageNotFoundException;

import java.util.List;
import java.util.Map;

//种子仓库
public abstract class SeedStorage {
	//当前的种子数量
	protected SeedCollection _seeds;
	//下一个种子仓库
	protected SeedStorage _next;
	//当前玩家信息
	protected Player _owner;

	public SeedStorage(int initialSeeds, Player owner) {
		_seeds = new SeedCollection(initialSeeds);
		_owner = owner;
	}

	//设置下一个仓库
	public void setNext(SeedStorage seedStorage) {
		_next = seedStorage;
	}

	public boolean isEmpty() {
		return _seeds.isEmpty();
	}

	public int harvestSeeds() {
		return _seeds.collectSeeds();
	}

	public int getSeeds() {
		return _seeds.getSeeds();
	}

	public SeedStorage sow(int seedsToSow, Player sourcePlayer) {
		//返回移动后剩下的种子
		int remainingSeeds = _seeds.sowSeed(seedsToSow);
		if (remainingSeeds == 0) {
			//返回当前的对象
			return this;
		} else {
			//继续移动
			return _next.sow(remainingSeeds, sourcePlayer);
		}
	}

	protected boolean compareOwners(Player player) {
		return _owner.equals(player);
	}

	public abstract boolean checkEmptyPlayerHouses();

	public abstract Action createAction(
			int numPlayerHouses, Player currentPlayer, Store currentPlayerStore,
			List<Player> players, Map<Player, SeedStorage> firstStorageMap) throws StorageNotFoundException;

	public abstract SeedStorage getSeedStorageOfId(int id) throws StorageNotFoundException;
	//移动种子
	public abstract SeedStorage reap(int targetHouseId) throws InvalidInputException;
}
