package kalah.board;

import kalah.player.Player;
import kalah.player.PlayerMove;

import java.util.ArrayList;
import java.util.List;

public class Board {
    protected final int numHousesPerPlayer;
    protected final int numSeedsPerHouseInitial;
    private final int zeroBasedPositionOfStoreInPlayerPits;
    private List<Pit> playerOnePits;
    private List<Pit> playerTwoPits;

    public Board(int numHousesPerPlayer, int numSeedsPerHouseInitial) {
        this.numHousesPerPlayer = numHousesPerPlayer;
        this.numSeedsPerHouseInitial = numSeedsPerHouseInitial;
        zeroBasedPositionOfStoreInPlayerPits = numHousesPerPlayer;
        //初始化玩家的house和store
        initialisePlayerPits();
        //设置所有玩家的house和store的联系
        arrangePitsAsCircularLinkedList();
    }

    //初始化玩家的pits
    private void initialisePlayerPits() {
        playerOnePits = new ArrayList<Pit>();
        playerTwoPits = new ArrayList<Pit>();
        addHousesToPlayerPits();
        addStoresToPlayerPits();
    }

    //设置所有玩家house的seed
    private void addHousesToPlayerPits() {
        for (int i = 0; i < numHousesPerPlayer; i++) {
            playerOnePits.add(new Pit(numSeedsPerHouseInitial, Pit.PitType.HOUSE, Player.PLAYER_ONE, i));
            playerTwoPits.add(new Pit(numSeedsPerHouseInitial, Pit.PitType.HOUSE, Player.PLAYER_TWO, i));
        }
    }

    //设置所有玩家的store的seed
    private void addStoresToPlayerPits() {
        playerOnePits.add(new Pit(0, Pit.PitType.STORE, Player.PLAYER_ONE, zeroBasedPositionOfStoreInPlayerPits));
        playerTwoPits.add(new Pit(0, Pit.PitType.STORE, Player.PLAYER_TWO, zeroBasedPositionOfStoreInPlayerPits));
    }
    //设置所有玩家的house和store的联系
    private void arrangePitsAsCircularLinkedList() {
        //连接玩家的house
        linkPlayersHouses();
        //连接玩家的store
        linkPlayersStores();
    }
    //设置玩家的house联系
    private void linkPlayersHouses() {
        for (int i = 0; i < numHousesPerPlayer - 1; i++) {
            playerOnePits.get(i).setNextPit(playerOnePits.get(i + 1));
            playerTwoPits.get(i).setNextPit(playerTwoPits.get(i + 1));
        }
    }
    //设置玩家最后的house与自己的store的联系
    //设置玩家store与对方house的联系
    private void linkPlayersStores() {
        linkPlayerLastHouseToStore(playerOnePits.get(numHousesPerPlayer - 1), playerOnePits.get(zeroBasedPositionOfStoreInPlayerPits));
        linkPlayerLastHouseToStore(playerTwoPits.get(numHousesPerPlayer - 1), playerTwoPits.get(zeroBasedPositionOfStoreInPlayerPits));
        linkPlayerStoreToOpponentFirstHouse(playerOnePits.get(zeroBasedPositionOfStoreInPlayerPits), playerTwoPits.get(0));
        linkPlayerStoreToOpponentFirstHouse(playerTwoPits.get(zeroBasedPositionOfStoreInPlayerPits), playerOnePits.get(0));
    }

    private void linkPlayerLastHouseToStore(Pit lastHouse, Pit store) {
        lastHouse.setNextPit(store);
    }

    private void linkPlayerStoreToOpponentFirstHouse(Pit store, Pit opponentFirstHouse) {
        store.setNextPit(opponentFirstHouse);
    }

    //获取当前面板的玩家信息
    public BoardState getCurrentBoardState() {
        int[] primitivePlayerOnePits = new int[playerOnePits.size()];
        int[] primitivePlayerTwoPits = new int[playerTwoPits.size()];
        for (int i = 0; i < playerOnePits.size(); i++) {
            primitivePlayerOnePits[i] = playerOnePits.get(i).getSeeds();
            primitivePlayerTwoPits[i] = playerTwoPits.get(i).getSeeds();
        }
        return new BoardState(primitivePlayerOnePits, primitivePlayerTwoPits);
    }

    //执行播种动作
    protected Pit sow(PlayerMove playerMove) {
        //选择玩家的pits库
        List<Pit> playerPits = selectPlayerPits(playerMove.player);
        //选择目标的houses
        Pit houseSelectedByPlayer = playerPits.get(playerMove.selectedHouse - 1);
        //获取需要移动的sees数
        int seedsToSow = houseSelectedByPlayer.getSeeds();
        //设置目标的house为0
        houseSelectedByPlayer.setSeedsToZero();
        //获取下一个houses的pit
        Pit currentPitToSowSeed = houseSelectedByPlayer.getNextPit();
        //设置玩家当前选择的house的pit，用于后续判断是否在store上
        Pit pitOfLastSownSeed = houseSelectedByPlayer;
        //执行移动seeds
        while (seedsToSow > 0) {
            //判断不是对方的store
            if (!isOpponentStore(currentPitToSowSeed, playerMove.player)) {
                //在对方的house中进行seed+1
                currentPitToSowSeed.incrementSeeds();
                //需要移动的sees-1
                seedsToSow--;
            }
            //获取下一个pit
            currentPitToSowSeed = currentPitToSowSeed.getNextPit();
            //更新当前玩家的上一步的pit
            pitOfLastSownSeed = pitOfLastSownSeed.getNextPit();
        }
        return pitOfLastSownSeed;
    }

    //判断是否为对方的store
    private boolean isOpponentStore(Pit pit, Player player) {
        //判断 当前pit不是自己的pit 且 pit是store
        return (!isPitBelongedToPlayer(pit, player) && pit.getPitType() == Pit.PitType.STORE);
    }

    //判断是否为当前玩家的pit
    private boolean isPitBelongedToPlayer(Pit pit, Player player) {
        return pit.getOwnerPlayer() == player;
    }
    //进行capture移动
    protected void capture(PlayerMove playerMove) {
        //获取当前玩家的pit信息
        List<Pit> captorPits = selectPlayerPits(playerMove.player);
        //获取对方玩家的信息,重复的实现逻辑
        List<Pit> captivePits = (captorPits == playerOnePits) ? playerTwoPits : playerOnePits;
        //获取captorStore
        Pit captorStore = captorPits.get(zeroBasedPositionOfStoreInPlayerPits);
        //获取自己的captorHouse
        Pit captorHouse = captorPits.get(playerMove.selectedHouse - 1);
        //获取对方的captorHouse
        Pit capturedHouse = captivePits.get(numHousesPerPlayer - playerMove.selectedHouse);
        //重新设置当前玩家的store中的seeds数量
        captorStore.addSeeds(captorHouse.getSeeds() + capturedHouse.getSeeds());
        //设置当前玩家的captorHouse为0
        captorHouse.setSeedsToZero();
        //设置对方玩家的captorHouse为0
        capturedHouse.setSeedsToZero();
    }

    //选择玩家的pits
    private List<Pit> selectPlayerPits(Player player) {
        switch (player) {
            case PLAYER_ONE:
                return playerOnePits;
            default:
                return playerTwoPits;
        }
    }
}
