package kalah.Move;

import kalah.KalahException.InvalidInputException;
import kalah.Player;

public class MoveFactory {
	/**
	 * 根据输入情况确认移动对象
	 *
	 * @param move      移动的按键
	 * @param player    玩家的信息
	 * @param numHouses
	 * @return
	 * @throws InvalidInputException
	 */
	public Move makeMove(String move, Player player, int numHouses) throws InvalidInputException {
		try {
			//如果move=q，则退出游戏？
			if ("Q".equalsIgnoreCase(move)) {
				//发送没有可移动的house信息
				return new QuitMove(player);
			} else {
				//将游戏的key转化为house的ID
				int selectedHouseId = Integer.parseInt(move);
				//判断是否输入的为house的ID
				if (selectedHouseId < 1 || selectedHouseId > numHouses) {
					//houseID输入错误
					throw new InvalidInputException("Could not find specified house number");
				} else {
					//生成移动对象
					return new ReapMove(player, selectedHouseId);
				}
			}
		} catch (NumberFormatException e) {
			throw new InvalidInputException("Invalid command. Please enter a valid house number or 'q' to quit .");
		}
	}
}
