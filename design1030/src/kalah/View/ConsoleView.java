package kalah.View;

import java.util.List;
import java.util.Map;

import com.qualitascorpus.testsupport.IO;
import kalah.BoardAccess;
import kalah.KalahException.InvalidInputException;
import kalah.Player;
import kalah.SeedStorage.SeedStorage;
import kalah.SeedStorage.Store;
import kalah.View.BoardView;

/**
 * 游戏面板打印对象
 */
public class ConsoleView implements BoardView {
    private IO _io;

    public ConsoleView(IO io) {
        _io = io;
    }

    /**
     * 打印游戏面板信息
     *
     * @param accessObject 游戏盘面信息
     * @param players      玩家信息
     */
    @Override
    public void update(BoardAccess accessObject, List<Player> players) {
        Player player1 = players.get(0);
        Player player2 = players.get(1);
        //打印横线
        _io.println(createBorder());
        //打印第二个玩家的信息
        _io.println(createPlayer2Board(player2, accessObject));
        _io.println("|    |-------+-------+-------+-------+-------+-------|    |");
        //打印第一个玩家的信息
        _io.println(createPlayer1Board(player1, accessObject));
        //打印横线
        _io.println(createBorder());
    }

    @Override
    public void showGameOver() {
        _io.println("Game over");
    }

    @Override
    public void displayEmptyHousePrompt() {
        _io.println("House is empty. Move again.");
    }

    /**
     * 展示游戏得分，胜利方或者平局
     *
     * @param players
     * @param accessObject
     */
    @Override
    public void showScore(List<Player> players, BoardAccess accessObject) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            _io.println("\tplayer " + player.toString() + ":" + accessObject.getPlayerScore(player));
        }
        if (accessObject.isTie()) {
            _io.println("A tie!");
        } else {
            _io.println("Player " + accessObject.getWinner().toString() + " wins!");
        }
    }

    //格式化提示玩家输入
    @Override
    public String promptInput(String playerId) {
        String promptString = String.format("Player P%s's turn - Specify house number or 'q' to quit: ", playerId);
        return _io.readFromKeyboard(promptString);
    }

    //打印输入异常的错误信息
    @Override
    public void displayInvalidInput(InvalidInputException e) {
        _io.print(e.getMessage());
    }

    /**
     * 打印第二个玩家的游戏信息
     *
     * @param player       玩家
     * @param accessObject 盘面信息
     * @return
     */
    private String createPlayer2Board(Player player, BoardAccess accessObject) {
        StringBuilder sb = new StringBuilder();
        //获取玩家名字
        sb.append("| P" + player.toString() + " |");
        //获取玩家的house信息
        List<Integer> houseSeeds = accessObject.getHousesSeeds(player);
        for (int i = houseSeeds.size() - 1; i >= 0; i--) {
            //组装每个house的数据
            sb.append(String.format(" %s[%2s] |", i + 1, houseSeeds.get(i)));
        }
        //获取对面玩家的store信息
        sb.append(String.format(" %2s |", accessObject.getStoreSeeds(player.getOpponent())));
        return sb.toString();
    }

    /**
     * 打印第一个玩家的游戏信息
     *
     * @param player       玩家
     * @param accessObject 盘面信息
     * @return
     */
    private String createPlayer1Board(Player player, BoardAccess accessObject) {
        StringBuilder sb = new StringBuilder();
        //获取对面玩家的store信息
        sb.append(String.format("| %2s |", accessObject.getStoreSeeds(player.getOpponent())));
        //获取玩家的house信息
        List<Integer> houseSeeds = accessObject.getHousesSeeds(player);
        for (int i = 0; i < houseSeeds.size(); i++) {
            //组装每个house的数据
            sb.append(String.format(" %s[%2s] |", i + 1, houseSeeds.get(i)));
        }
        //获取玩家的名字
        sb.append(" P" + player.toString() + " |");
        return sb.toString();
    }

    //打印横线
    private String createBorder() {
        return "+----+-------+-------+-------+-------+-------+-------+----+";
    }
}
