package lucky.gameFunctions;

import ch.aplu.jcardgame.*;
import lucky.Rank;
import lucky.Suit;
import lucky.players.Player;

import java.util.List;
import java.util.stream.Collectors;

public class ResultLogger {
    private StringBuilder logResult;
    private Player[] players;

    public ResultLogger(Player[] players) {
        this.players = players;
        logResult = new StringBuilder();
    }

    public void addCardPlayedToLog(int player, List<Card> cards, Rank[] ranks, Suit[] suits) {
        if (cards.size() < 2) {
            return;
        }
        logResult.append("P" + player + "-");

        for (int i = 0; i < cards.size(); i++) {
            Rank cardRank = ranks[cards.get(i).getRankId()];
            Suit cardSuit = suits[cards.get(i).getSuitId()];
            logResult.append(cardRank.getRankCardLog() + cardSuit.getSuitShortHand());
            if (i < cards.size() - 1) {
                logResult.append("-");
            }
        }
        logResult.append(",");
    }

    public void addRoundInfoToLog(int roundNumber) {
        logResult.append("Round" + roundNumber + ":");
    }

    public void addEndOfRoundToLog() {
        logResult.append("Score:");
        for (int i = 0; i < players.length; i++) {
            logResult.append(players[i].getScore() + ",");
        }
        logResult.append("\n");
    }

    public void addEndOfGameToLog(List<Integer> winners) {
        logResult.append("EndGame:");
        for (int i = 0; i < players.length; i++) {
            logResult.append(players[i].getScore() + ",");
        }
        logResult.append("\n");
        logResult.append("Winners:" + String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList())));
    }

    public String getLogResult() {
        return logResult.toString();
    }
}