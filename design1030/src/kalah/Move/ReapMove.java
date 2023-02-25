package kalah.Move;

import kalah.Move.Move;
import kalah.Player;

/**
 * 获取移动的house
 */
public class ReapMove extends Move {
	private int _targetHouse;

	//获取移动步骤
	public ReapMove(Player player, int houseToReap) {
		super(player, true);
		_targetHouse = houseToReap;
	}

	public int getHouseChoice() {
		return _targetHouse;
	}
}
