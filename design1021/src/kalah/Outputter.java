package kalah;

import com.qualitascorpus.testsupport.IO;
import kalah.board.BoardState;

public class Outputter {
    private IO io;

    public Outputter(IO io) {
        this.io = io;
    }

    protected void showErrorPlayerSelectedEmptyHouse() {
        io.println("House is empty. Move again.");
    }

    //展示游戏结果
    protected void showFinalScoreAndWinner(BoardState finalBoardState) {
        int[] numSeedsInPlayerOnePits = finalBoardState.playerOnePits;
        int[] numSeedsInPlayerTwoPits = finalBoardState.playerTwoPits;
        int scorePlayerOne = calculatePlayerScore(numSeedsInPlayerOnePits);
        int scorePlayerTwo = calculatePlayerScore(numSeedsInPlayerTwoPits);
        io.println("\tplayer 1:" + scorePlayerOne);
        io.println("\tplayer 2:" + scorePlayerTwo);
        String winnerString = "";
        if (scorePlayerOne > scorePlayerTwo) {
            winnerString = "Player 1 wins!";
        } else if (scorePlayerOne < scorePlayerTwo) {
            winnerString = "Player 2 wins!";
        } else {
            winnerString = "A tie!";
        }
        io.println(winnerString);
    }

    protected void showGameOver(BoardState finalBoardState) {
        io.println("Game over");
        showCurrentBoardState(finalBoardState);
    }

    //展示面板信息
    protected void showCurrentBoardState(BoardState currentBoardState) {
        int[] numSeedsInPlayerOnePits = currentBoardState.playerOnePits;
        int[] numSeedsInPlayerTwoPits = currentBoardState.playerTwoPits;
        io.println("+----+-------+-------+-------+-------+-------+-------+----+");
        io.print("| P2 ");
        for (int i = numSeedsInPlayerTwoPits.length - 2; i >= 0; i--) {
            int houseNumber = i + 1;
            int seedsCount = numSeedsInPlayerTwoPits[i];
            String houseNumberAsString = addPrefixBlankSpaceIfSingleDigit(houseNumber);
            String seedsCountAsString = addPrefixBlankSpaceIfSingleDigit(seedsCount);
            io.print("|" + houseNumberAsString + "[" + seedsCountAsString + "] ");
        }
        String p1SeedsInStoreAsString = createStringOfPlayerSeedsInStore(numSeedsInPlayerOnePits[numSeedsInPlayerOnePits.length - 1]);
        io.println(p1SeedsInStoreAsString);
        io.println("|    |-------+-------+-------+-------+-------+-------|    |");
        String p2SeedsInStoreAsString = createStringOfPlayerSeedsInStore(numSeedsInPlayerTwoPits[numSeedsInPlayerTwoPits.length - 1]);
        io.print(p2SeedsInStoreAsString);
        for (int i = 0; i < numSeedsInPlayerOnePits.length - 1; i++) {
            int houseNumber = i + 1;
            int seedsCount = numSeedsInPlayerOnePits[i];
            String houseNumberAsString = addPrefixBlankSpaceIfSingleDigit(houseNumber);
            String seedsCountAsString = addPrefixBlankSpaceIfSingleDigit(seedsCount);
            io.print(houseNumberAsString + "[" + seedsCountAsString + "] |");
        }
        io.println(" P1 |");
        io.println("+----+-------+-------+-------+-------+-------+-------+----+");
    }

    private String createStringOfPlayerSeedsInStore(int numSeedsInStore) {
        String numSeedsInStoreAsString = addPrefixBlankSpaceIfSingleDigit(numSeedsInStore);
        return "| " + numSeedsInStoreAsString + " |";
    }

    private String addPrefixBlankSpaceIfSingleDigit(int digit) {
        return (digit < 10 ? (" " + digit) : Integer.toString(digit));
    }

    private int calculatePlayerScore(int[] numSeedsInPlayerPits) {
        int sum = 0;
        for (int seeds : numSeedsInPlayerPits) {
            sum += seeds;
        }
        return sum;
    }
}
