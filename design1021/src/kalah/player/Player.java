package kalah.player;
public enum Player {
    PLAYER_ONE, PLAYER_TWO;
    public Player determineOpponentPlayer() {
        return (this == PLAYER_ONE ? PLAYER_TWO : PLAYER_ONE);
    }
}
