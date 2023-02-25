package kalah.Move;

import kalah.KalahException.MoveException;
import kalah.Player;

/**
 * 移动对象
 */
public abstract class Move {
    //玩家信息
    private Player _player;
    //是否继续
    private boolean _shouldContinue;

    public Move(Player player, boolean shouldContinue) {
        _player = player;
        _shouldContinue = shouldContinue;
    }
    //是否结束
    public void finishGame() {
        _shouldContinue = false;
    }

    public abstract int getHouseChoice() throws MoveException;
    //检查是否继续
    public boolean checkForContinue() {
        return _shouldContinue;
    }

    public Player getPlayer() {
        return _player;
    }
}
