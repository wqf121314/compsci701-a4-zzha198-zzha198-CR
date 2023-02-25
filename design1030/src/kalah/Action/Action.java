package kalah.Action;

import kalah.Player;

import java.util.Map;

//命令模式
public abstract class Action {
	private Player _nextPlayer;

	public Action(Player player) {
		_nextPlayer = player;
	}

	//内部函数，确定下一个玩家
	protected Player determineNextPlayer() {
		return _nextPlayer;
	}

	//执行命令
	public abstract Player executeAction();
}
