package kalah;

import kalah.Action.Action;
import kalah.KalahException.InvalidInputException;
import kalah.KalahException.MoveException;
import kalah.KalahException.StorageNotFoundException;
import kalah.Move.Move;
import kalah.SeedStorage.SeedStorage;
import kalah.SeedStorage.House;
import kalah.SeedStorage.Store;
import kalah.View.BoardView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private final int START_ID = 1;
    //玩家的houses
    private int _numPlayerHouses;
    //玩家信息
    private List<Player> _players;
    private Map<Player, SeedStorage> _firstHouseMap = new HashMap<>();
    private Map<Player, Store> _storeMap = new HashMap<>();
    //面板显示对象
    private BoardView _view;

    //初始化面板信息
    public Board(int initialSeeds, int numPlayerHouses, List<Player> playerList, BoardView view) throws StorageNotFoundException {
        _players = playerList;
        _numPlayerHouses = numPlayerHouses;
        _view = view;
        for (Player player : playerList) {
            //设置玩家的store
            Store store = new Store(player);
            //设置玩家的house
            House house = new House(player, store, initialSeeds, numPlayerHouses, START_ID);
            _storeMap.put(player, store);
            _firstHouseMap.put(player, house);
        }
        //简历store到house到连接
        connectStoreToHouse();
        //展示游戏面板信息
        updateView();
    }

    //关联玩家的store到对面玩家的house
    private void connectStoreToHouse() {
        for (Player player : _storeMap.keySet()) {
            Store store = _storeMap.get(player);
            SeedStorage opponentHouse = _firstHouseMap.get(player.getOpponent());
            store.connectToHouse(opponentHouse);
        }
    }

    /**
     * 执行移动
     *
     * @param move 移动对象
     * @return
     * @throws InvalidInputException
     * @throws StorageNotFoundException
     * @throws MoveException
     */
    public Player executeMove(Move move) throws InvalidInputException, StorageNotFoundException, MoveException {
        //获取需要移动的玩家信息
        Player currentPlayer = move.getPlayer();
        //获取需要移动的house信息
        int targetHouse = move.getHouseChoice();
        //获取玩家houses的种子仓库对象
        SeedStorage lastSeedStorage = _firstHouseMap.get(currentPlayer).reap(targetHouse);
        //获取玩家的store的信息
        Store currentPlayerStore = _storeMap.get(currentPlayer);
        //生成移动动作对象
        Action action = lastSeedStorage.createAction(_numPlayerHouses, currentPlayer, currentPlayerStore, _players, _firstHouseMap);
        //执行移动的动作
        currentPlayer = action.executeAction();
        //执行打印
        updateView();
        //返回玩家信息
        return currentPlayer;
    }

    /**
     * 检测游戏是否结束
     *
     * @param player
     * @return
     * @throws StorageNotFoundException
     */
    public boolean checkGameEndStatus(Player player) throws StorageNotFoundException {
        SeedStorage firstHouse = _firstHouseMap.get(player);
        if (firstHouse.checkEmptyPlayerHouses()) {
            return true;
        }
        return false;
    }

    /**
     * 提示游戏的house为空
     *
     * @throws StorageNotFoundException
     */
    public void displayEmptyHousePrompt() throws StorageNotFoundException {
        _view.displayEmptyHousePrompt();
        //打印面板
        updateView();
    }

    /**
     * 提示游戏结束
     * @throws StorageNotFoundException
     */
    public void displayGameOver() throws StorageNotFoundException {
        _view.showGameOver();
        updateView();
    }

    /**
     * 提示游戏完成，并展示分数
     * @throws StorageNotFoundException
     */
    public void gameFinish() throws StorageNotFoundException {
        BoardAccess accessObject = new BoardAccess(generateHouseMap(), generateStoreMap());
        _view.showScore(_players, accessObject);
    }

    //展示游戏面板信息
    private void updateView() throws StorageNotFoundException {
        //组装游戏面板对象信息
        BoardAccess accessObject = new BoardAccess(generateHouseMap(), generateStoreMap());
        //打印游戏面板信息
        _view.update(accessObject, _players);
    }

    //获取所有玩家的houses信息
    private Map<Player, Map<Integer, Integer>> generateHouseMap() throws StorageNotFoundException {
        Map<Player, Map<Integer, Integer>> idHouseMap = new HashMap<>();
        //遍历所有环境
        for (Player player : _players) {
            Map<Integer, Integer> houseMap = new HashMap<>();
            //获取玩家信息
            SeedStorage firstHouse = _firstHouseMap.get(player);
            for (int i = 1; i <= _numPlayerHouses; i++) {
                //将玩家的house信息放入houseMap
                houseMap.put(i, firstHouse.getSeedStorageOfId(i).getSeeds());
            }
            //将获取的玩家的hoses信息放入idHouseMap
            idHouseMap.put(player, houseMap);
        }
        return idHouseMap;
    }

    //获取所有玩家的store信息
    private Map<Player, Integer> generateStoreMap() {
        Map<Player, Integer> storeSeedsMap = new HashMap<>();
        for (Player player : _players) {
            //将玩家的store信息放入storeSeedsMap
            storeSeedsMap.put(player, _storeMap.get(player).getSeeds());
        }
        return storeSeedsMap;
    }
}
