package kalah;

import com.qualitascorpus.testsupport.IO;
import kalah.KalahException.InvalidInputException;
import kalah.KalahException.MoveException;
import kalah.KalahException.StorageNotFoundException;
import kalah.Move.Move;
import kalah.View.BoardView;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Board _board;
    private List<Player> _playerList;
    private int _initialSeeds;
    private int _numPlayerHouses;
    private BoardView _boardView;

    //初始化游戏对象
    public Game(int initialSeeds, int numPlayerHouses, int numPlayers, BoardView boardView) throws StorageNotFoundException {
        //初始化 _playerList对象
        createPlayers(numPlayers, numPlayerHouses);
        //设置对手？
        setOpponents();
        _initialSeeds = initialSeeds;
        _numPlayerHouses = numPlayerHouses;
        _boardView = boardView;
        //初始化面板信息
        _board = new Board(_initialSeeds, _numPlayerHouses, _playerList, _boardView);
    }

    //初始化 _playerList对象
    private void createPlayers(int numPlayers, int numPlayerHouses) {
        _playerList = new ArrayList<>();
        for (int i = 1; i <= numPlayers; i++) {
            _playerList.add(new Player(i, numPlayerHouses));
        }
    }

    //设置当前玩家中设置对手玩家
    private void setOpponents() {
        for (int i = 0; i < _playerList.size(); i++) {
            //获取对方的player
            int opponentIndex = (i + 1) % _playerList.size();
            //获取对方玩家的对象
            Player opponent = _playerList.get(opponentIndex);
            //获取当前玩家的对象
            Player player = _playerList.get(i);
            //给当前玩家绑定对手
            player.setOpponent(opponent);
        }
    }

    public void play() throws StorageNotFoundException {
        //设置先手玩家信息
        Player currentPlayer = _playerList.get(0);
        //是否游戏完成
        boolean gameCompleted = false;
        //玩家开始游戏
        Move nextMove = currentPlayer.play(_boardView);
        //通过移动对象判断游戏是否继续执行
        while (nextMove.checkForContinue()) {
            try {
                //执行seeds的移动
                currentPlayer = _board.executeMove(nextMove);
                //检测游戏是否结束
                if (_board.checkGameEndStatus(currentPlayer)) {
                    gameCompleted = true;
                    //标记游戏结束
                    nextMove.finishGame();
                } else {
                    //继续游戏
                    nextMove = currentPlayer.play(_boardView);
                }
            } catch (InvalidInputException e) {
                //提示house为空
                _board.displayEmptyHousePrompt();
                //继续移动
                nextMove = currentPlayer.play(_boardView);
            } catch (MoveException e) {
                //初始化面板
                _board = new Board(_initialSeeds, _numPlayerHouses, _playerList, _boardView);
            }
        }
        //提示游戏结束
        _board.displayGameOver();
        if (gameCompleted) {
            _board.gameFinish();
        }
    }
}