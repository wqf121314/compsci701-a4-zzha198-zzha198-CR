package kalah.View;
import kalah.BoardAccess;
import kalah.KalahException.InvalidInputException;
import kalah.Player;
import kalah.SeedStorage.SeedStorage;
import kalah.SeedStorage.Store;
import java.util.List;
import java.util.Map;
public interface BoardView {
    public void update(BoardAccess accessObject, List<Player> players);
    public void showGameOver();
    public void displayEmptyHousePrompt();
    public void showScore(List<Player> players, BoardAccess accessObject);
    public String promptInput(String playerId);
    public void displayInvalidInput(InvalidInputException e);
}
