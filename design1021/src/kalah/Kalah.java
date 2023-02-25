package kalah;

import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;
import kalah.board.Board;
import kalah.board.BoardController;
import kalah.board.BoardControllerConcrete;
import kalah.player.Player;
import kalah.player.PlayerMove;

public class Kalah {
    private static final int NUM_HOUSES_PER_PLAYER = 6;
    private static final int NUM_SEEDS_PER_HOUSE_INITIAL = 4;
    //游戏状态
    private GameState currentGameState;
    //游戏面板
    private Board board;
    //输出对象
    private Outputter outputter;
    //控制对象
    private BoardController boardController;

    public static void main(String[] args) {
        new Kalah().play(new MockIO());
    }

    public void play(IO io) {
        //初始化
        setupFields(io);
        //循环执行游戏动作
        while (currentGameState == GameState.PLAYER_ONE_TURN || currentGameState == GameState.PLAYER_TWO_TURN) {
            //打印游戏面板信息
            outputter.showCurrentBoardState(board.getCurrentBoardState());
            //获取游戏的输入
            int playerInput = readInputFromPlayer(io, currentGameState);
            //判断非法输入
            if (isPlayerQuitting(playerInput)) {
                currentGameState = GameState.QUIT;
                break;
            }
            //设置游戏动作对象
            PlayerMove playerMove = createPlayerMove(currentGameState, playerInput);
            //判断是否可以移动
            if (isPlayerMoveInvalid(playerMove)) {
                //打印空的house
                outputter.showErrorPlayerSelectedEmptyHouse();
                continue;
            }
            //执行移动动作
            GameState nextGameState = boardController.processValidPlayerMove(playerMove);
            //获取下一个玩家
            Player nextPlayer = determineCurrentPlayer(nextGameState);
            //判断游戏是否结束，通过判断下一个玩家的houses
            if (isGameFinished(nextPlayer)) {
                //设置游戏结束
                currentGameState = GameState.FINISHED;
                break;
            } else {
                //设置游戏继续
                currentGameState = nextGameState;
            }
        }
        //进行游戏状态判断
        switch (currentGameState) {
            //游戏终止
            case QUIT:
                outputter.showGameOver(board.getCurrentBoardState());
                break;
            //游戏完成
            case FINISHED:
                //打印游戏面板
                outputter.showCurrentBoardState(board.getCurrentBoardState());
                //显示游戏结束信息
                outputter.showGameOver(board.getCurrentBoardState());
                outputter.showFinalScoreAndWinner(board.getCurrentBoardState());
                break;
        }
    }

    private PlayerMove createPlayerMove(GameState currentGameState, int playerInput) {
        Player currentPlayer = determineCurrentPlayer(currentGameState);
        return new PlayerMove(currentPlayer, playerInput);
    }

    private boolean isPlayerQuitting(int playerInput) {
        return playerInput == -1;
    }

    //混乱的逻辑判断
    private boolean isPlayerMoveInvalid(PlayerMove playerMove) {
        return !boardController.isPlayerMoveValidOnBoard(playerMove);
    }

    private boolean isGameFinished(Player nextPlayer) {
        return boardController.isPlayerHousesAllEmpty(nextPlayer);
    }

    //在Player对象中存在重复的代码
    private Player determineCurrentPlayer(GameState currentGameState) {
        return (currentGameState == GameState.PLAYER_ONE_TURN ? Player.PLAYER_ONE : Player.PLAYER_TWO);
    }

    //必须输入1-NUM_HOUSES_PER_PLAYER的数字否则为-1
    private int readInputFromPlayer(IO io, GameState currentGameState) {
        String playerString = "";
        String partialPrompt = "'s turn - Specify house number or 'q' to quit: ";
        switch (currentGameState) {
            case PLAYER_ONE_TURN:
                playerString = "Player P1";
                break;
            case PLAYER_TWO_TURN:
                playerString = "Player P2";
                break;
            default:
                return -1;
        }
        String fullPrompt = playerString + partialPrompt;
        //i2: cancelResult - What value to return if the attempt at input is cancelled
        //s1: cancelStr - What string the user should enter to cancel the input
        return io.readInteger(fullPrompt, 1, NUM_HOUSES_PER_PLAYER, -1, "q");
    }

    private void setupFields(IO io) {
        //设置当前执行的玩家
        currentGameState = GameState.PLAYER_ONE_TURN;
        //初始化游戏面板
        board = new Board(NUM_HOUSES_PER_PLAYER, NUM_SEEDS_PER_HOUSE_INITIAL);
        //声明打印对象
        outputter = new Outputter(io);
        //声明游戏控制对象
        boardController = new BoardControllerConcrete(board);
    }
}
