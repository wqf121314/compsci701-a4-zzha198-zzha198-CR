package kalah.board;
import kalah.player.Player;
public class Pit {
    public enum PitType {
        STORE, HOUSE
    }
    private int seeds;
    private PitType pitType;
    private Player ownerPlayer;
    private int zeroBasedPitPositionInOwnerPits;
    private Pit nextPit;
    public Pit(int initialSeeds, PitType pitType, Player ownerPlayer, int zeroBasedPitPositionInOwnerPits) {
        this.seeds = initialSeeds;
        this.pitType = pitType;
        this.ownerPlayer = ownerPlayer;
        this.zeroBasedPitPositionInOwnerPits = zeroBasedPitPositionInOwnerPits;
        this.nextPit = null;
    }
    protected Pit getNextPit() {
        return this.nextPit;
    }
    protected void setNextPit(Pit nextPit) {
        this.nextPit = nextPit;
    }
    protected int getSeeds() {
        return seeds;
    }
    protected void addSeeds(int newSeeds) {
        this.seeds += newSeeds;
    }
    protected void setSeedsToZero() {
        this.seeds = 0;
    }
    protected void incrementSeeds() {
        this.seeds++;
    }
    protected PitType getPitType() {
        return this.pitType;
    }
    protected Player getOwnerPlayer() {
        return this.ownerPlayer;
    }
    protected int getZeroBasedPitPositionInOwnerPits() {
        return this.zeroBasedPitPositionInOwnerPits;
    }
}
