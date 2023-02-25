package kalah.board;

import kalah.GameState;
import kalah.player.Player;
import kalah.player.PlayerMove;

import java.util.Arrays;

public class BoardControllerConcrete implements BoardController {
    private Board board;

    public BoardControllerConcrete(Board board) {
        this.board = board;
    }

    //执行移动动作
    public GameState processValidPlayerMove(PlayerMove playerMove) {
        Player currentPlayer = playerMove.player;
        Player opponentPlayer = playerMove.player.determineOpponentPlayer();
        //获取移动后的最新pit
        Pit pitOfLastSownSeed = board.sow(playerMove);
        //判断是否落在了自己的store上
        if (isLastSeedInPlayerStore(pitOfLastSownSeed, currentPlayer)) {
            //返回当前玩家继续玩
            return choosePlayerTurn(currentPlayer);
        } else if (isPlayerHouseEmptyBeforeLastSeedWasPlaced(pitOfLastSownSeed, currentPlayer)
                && !isOppositeHouseCurrentlyEmpty(pitOfLastSownSeed)) {
            //进行capture移动
            this.playCaptureMove(pitOfLastSownSeed);
        }
        //返回对方玩家继续游戏
        return choosePlayerTurn(opponentPlayer);
    }

    //判断是否可以移动，
    public boolean isPlayerMoveValidOnBoard(PlayerMove playerMove) {
        return !isHouseSelectedByPlayerEmpty(board.getCurrentBoardState(), playerMove);
    }

    //判断对方玩家的相对house是否有seed
    private boolean isOppositeHouseCurrentlyEmpty(Pit pitOfLastSownSeed) {
        //获取玩家最后的pit
        BoardState currentBoardState = board.getCurrentBoardState();
        //获取玩家信息
        Player ownerOfPitOfLastSownSeed = pitOfLastSownSeed.getOwnerPlayer();
        //获取对方玩家的pits信息
        int[] oppositePits = (ownerOfPitOfLastSownSeed == Player.PLAYER_ONE
                ? currentBoardState.playerTwoPits : currentBoardState.playerOnePits);
        //获取相对应的对方的house的位置
        int zeroBasedPositionOfOppositeHouse = board.numHousesPerPlayer
                - pitOfLastSownSeed.getZeroBasedPitPositionInOwnerPits() - 1;
        //判断对方的相对house是否为空
        return (oppositePits[zeroBasedPositionOfOppositeHouse] == 0);
    }

    //是否当前玩家的house的seed被重新放上了棋子
    private boolean isPlayerHouseEmptyBeforeLastSeedWasPlaced(Pit pitOfLastSownSeed, Player player) {
        return (pitOfLastSownSeed.getOwnerPlayer() == player
                && pitOfLastSownSeed.getSeeds() == 1);
    }

    //判断是否最后的落点是不是在当前玩家的store上
    private boolean isLastSeedInPlayerStore(Pit pitOfLastSownSeed, Player player) {
        return (pitOfLastSownSeed.getOwnerPlayer() == player
                && pitOfLastSownSeed.getPitType() == Pit.PitType.STORE);
    }

    //判断选择house是否为空
    private boolean isHouseSelectedByPlayerEmpty(BoardState preMoveBoardState, PlayerMove playerMove) {
        int[] playerPits = choosePlayerPits(preMoveBoardState, playerMove.player);
        //获取house的seeds数
        int seedsAtSelectedHouse = playerPits[playerMove.selectedHouse - 1];
        return (seedsAtSelectedHouse == 0);
    }

    //进行capture移动
    private void playCaptureMove(Pit pitOfLastSownSeed) {
        //获取相对应的对方的house的seeds
        int houseNumberOfLastSownSeed = pitOfLastSownSeed.getZeroBasedPitPositionInOwnerPits() + 1;
        //设置移动对象
        PlayerMove captureMove = new PlayerMove(pitOfLastSownSeed.getOwnerPlayer(), houseNumberOfLastSownSeed);
        //设置capture数据
        board.capture(captureMove);
    }

    //获取玩家的pits
    private int[] choosePlayerPits(BoardState boardState, Player player) {
        return (player == Player.PLAYER_ONE ? boardState.playerOnePits : boardState.playerTwoPits);
    }

    //获取下一个移动的玩家
    private GameState choosePlayerTurn(Player player) {
        return (player == Player.PLAYER_ONE ? GameState.PLAYER_ONE_TURN : GameState.PLAYER_TWO_TURN);
    }

    //判断玩家的house是否全部为空
    public boolean isPlayerHousesAllEmpty(Player player) {
        //获取所有玩家信息
        BoardState currentBoardState = board.getCurrentBoardState();
        //获取玩家的pit信息
        int[] playerPits = choosePlayerPits(currentBoardState, player);
        //获取pit为house的信息
        int[] playerHouses = Arrays.copyOfRange(playerPits, 0, playerPits.length - 1);
        for (int seeds : playerHouses) {
            if (seeds > 0) {
                return false;
            }
        }
        return true;
    }
}
