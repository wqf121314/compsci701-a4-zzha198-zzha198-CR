package kalah;

import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;
import kalah.KalahException.StorageNotFoundException;
import kalah.View.BoardView;
import kalah.View.ConsoleView;

public class Kalah {
    //初始化的每个house的seeds数量
    public static final int INITIAL_SEEDS = 4;
    //初始化每个玩家的house数量
    public static final int HOUSE_PER_PLAYER = 6;
    //初始化游戏的玩家
    public static final int NUM_PLAYERS = 2;

    public static void main(String[] args) {
        new Kalah().play(new MockIO());
    }

    public void play(IO io) {
        //初始化游戏面板信息
        BoardView boardView = new ConsoleView(io);
        //游戏的全局变量
        Game game = null;
        try {
            //初始化游戏对象
            game = new Game(INITIAL_SEEDS, HOUSE_PER_PLAYER, NUM_PLAYERS, boardView);
            //开始游戏
            game.play();
        } catch (StorageNotFoundException e) {
            e.printStackTrace();
        }
    }
}
