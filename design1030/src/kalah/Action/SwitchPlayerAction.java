package kalah.Action;
import kalah.Action.Action;
import kalah.Player;
public class SwitchPlayerAction extends Action {
    public SwitchPlayerAction(Player nextPlayer) {
        super(nextPlayer);
    }
    @Override
    public Player executeAction() {
        return super.determineNextPlayer();
    }
}
