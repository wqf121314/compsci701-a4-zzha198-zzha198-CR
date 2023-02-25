package kalah;

import com.qualitascorpus.testsupport.IO;
import kalah.KalahException.InvalidInputException;
import kalah.Move.Move;
import kalah.Move.MoveFactory;
import kalah.View.BoardView;

public class Player {
    //玩家编号
    private final int _id;
    //对手
    private Player _opponent;
    private int _numHousesOwned;
    //移动工程？
    private MoveFactory _moveFactory;

    public Player(int id, int numPlayerHouses) {
        _id = id;
        _numHousesOwned = numPlayerHouses;
        //初始化移动工程？？
        _moveFactory = new MoveFactory();
    }

    public Player getOpponent() {
        return _opponent;
    }

    public void setOpponent(Player player) {
        _opponent = player;
    }

    /**
     * 根据游戏面板的信息获取玩家的输入，并返回移动对象
     *
     * @param view 游戏打印对象
     * @return
     */
    public Move play(BoardView view) {
        //获取玩家输入
        String input = view.promptInput(toString());
        try {
            //将玩家的输入生成移动对象
            return _moveFactory.makeMove(input, this, _numHousesOwned);
        } catch (InvalidInputException e) {
            //当输入异常时，打印异常信息：2个异常信息
            //Could not find specified house number.
            //Invalid command. Please enter a valid house number or 'q' to quit .
            view.displayInvalidInput(e);
            //递归重新执行本方法获取玩家输入
            return play(view);
        }
    }

    //获取玩家名字
    @Override
    public String toString() {
        return "" + _id;
    }
}