package kalah.SeedStorage;

import kalah.*;
import kalah.Action.Action;
import kalah.Action.CaptureAction;
import kalah.Action.SwitchPlayerAction;
import kalah.KalahException.InvalidInputException;
import kalah.KalahException.StorageNotFoundException;

import java.util.List;
import java.util.Map;

/**
 * house对象
 */
public class House extends SeedStorage {
	//house的ID
	private int _id;
	private final int ID_INCREMENT = 1;

	/**
	 * 初始化house信息
	 *
	 * @param player          玩家
	 * @param store           仓库
	 * @param initialSeeds    初始化种子
	 * @param numPlayerHouses 玩家的house数量
	 * @param id              玩家house的ID
	 */
	public House(Player player, Store store, int initialSeeds, int numPlayerHouses, int id) {
		super(initialSeeds, player);
		_id = id;
		//house的种子对象
		SeedStorage nextStorage;
		//设置house关联的下一个移动对象
		if (id == numPlayerHouses) {
			nextStorage = store;
		} else {
			//新增house
			nextStorage = new House(player, store, initialSeeds, numPlayerHouses, id + ID_INCREMENT);
		}
		//设置下一个仓库
		super.setNext(nextStorage);
	}

	//递归检查当前的seed是不是空的
	@Override
	public boolean checkEmptyPlayerHouses() {
		//_seeds=SeedStorage.SeedCollection
		if (!_seeds.isEmpty()) {
			return false;
		}
		//下一个房子的检测 _next=SeedStorage.SeedStorage
		return _next.checkEmptyPlayerHouses();
	}

	/**
	 * 在house中进行移动seeds
	 *
	 * @param numPlayerHouses    玩家剩余移动的houses数
	 * @param currentPlayer      当前玩家
	 * @param currentPlayerStore 当前玩家的store
	 * @param players            所有玩家
	 * @param firstStorageMap    所有玩家的store
	 * @return
	 * @throws StorageNotFoundException
	 */
	@Override
	public Action createAction(
			int numPlayerHouses, Player currentPlayer, Store currentPlayerStore,
			List<Player> players, Map<Player, SeedStorage> firstStorageMap) throws StorageNotFoundException {
		//获取下一个玩家的信息
		Player nextPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
		//对面玩家的id
		int oppositeId = numPlayerHouses - _id + 1;
		//获取对方的store对象
		SeedStorage oppositeStorage = firstStorageMap.get(_owner.getOpponent()).getSeedStorageOfId(oppositeId);
		//判断下一个pit是否是自己的pit
		if (this.compareOwners(currentPlayer) && _seeds.hasOneLeft()) {
			//如果对面的pit是空，则执行capture活动
			if (!oppositeStorage.isEmpty()) {
				//生成合并动作
				return new CaptureAction(nextPlayer, this, oppositeStorage, currentPlayerStore);
			}
		}
		//判断下一步动作
		return new SwitchPlayerAction(nextPlayer);
	}
	//获取当前house的ID
	@Override
	public SeedStorage getSeedStorageOfId(int id) throws StorageNotFoundException {
		if (_id == id) {
			return this;
		} else {
			return _next.getSeedStorageOfId(id);
		}
	}
	//往下一个移动
	@Override
	public SeedStorage reap(int targetHouseId) throws InvalidInputException {
		if (_id == targetHouseId) {
			if (_seeds.isEmpty()) {
				throw new InvalidInputException("Selected house has no seeds left!");
			}
			//收集玩家house中的seeds
			int seedsToSow = _seeds.collectSeeds();
			//获取最新的种子移动后的结果对象
			SeedStorage lastSeedStorage = _next.sow(seedsToSow, _owner);
			return lastSeedStorage;
		} else {
			//继续移动
			return _next.reap(targetHouseId);
		}
	}
}