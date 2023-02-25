package kalah.board;
import kalah.GameState;
import kalah.player.Player;
import kalah.player.PlayerMove;
public interface BoardController {
    public GameState processValidPlayerMove(PlayerMove playerMove);
    public boolean isPlayerMoveValidOnBoard(PlayerMove playerMove);
    public boolean isPlayerHousesAllEmpty(Player player);
}
