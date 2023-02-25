package kalah.Action;

import kalah.Action.Action;
import kalah.Player;
import kalah.SeedStorage.SeedStorage;
import kalah.SeedStorage.Store;

/**
 * capture活动
 */
public class CaptureAction extends Action {
	//最后的store
	private SeedStorage _lastStorage;
	private Store _store;
	//对方的store
	private SeedStorage _oppositeStorage;

	public CaptureAction(Player nextPlayer, SeedStorage lastStorage, SeedStorage oppositeStorage, Store store) {
		//设置action父类的对象
		super(nextPlayer);
		_lastStorage = lastStorage;
		_oppositeStorage = oppositeStorage;
		_store = store;
	}

	/**
	 * 执行seed合并动作
	 *
	 * @return
	 */
	@Override
	public Player executeAction() {
		_store.capture(_lastStorage, _oppositeStorage);
		return super.determineNextPlayer();
	}
}
