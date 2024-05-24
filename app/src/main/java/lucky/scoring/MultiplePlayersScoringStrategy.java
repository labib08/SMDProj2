package lucky.scoring;

import ch.aplu.jcardgame.*;
import lucky.Rank;
import lucky.Suit;
import lucky.scoring.PrivateScoreSumming;

import java.util.*;

public class MultiplePlayersScoringStrategy extends PrivateScoreSumming {

    @Override
    public int calculateScore(int playerIndex, List<Hand> hands, List<Map<String, ArrayList<Card>>> thirteenChecks) {
        int maxScore = 0;

        for (Map<String, ArrayList<Card>> check : thirteenChecks) {
            int currentCheckMaxScore = 0;

            // Iterate over each entry in the current check map
            for (Map.Entry<String, ArrayList<Card>> entry : check.entrySet()) {

                String key = entry.getKey();
                ArrayList<Card> cards = entry.getValue();

                int score = 0;

                if (key.equals("bothPrivate")) {
                    // Option 1 of summation: Two private cards
                    score = getScorePrivateCard(cards.get(0)) + getScorePrivateCard(cards.get(1));

                } else if (key.equals("publicAndPrivate")) {
                    // Option 2 of summation: One private card and one public card
                    score = getScorePrivateCard(cards.get(0)) + getScorePublicCard(cards.get(1));

                } else if (key.equals("allCards")) {
                    // Option 3 of summation: Two private cards and two public cards
                    score = getScorePrivateCard(cards.get(0)) + getScorePrivateCard(cards.get(1)) +
                            getScorePublicCard(cards.get(2)) + getScorePublicCard(cards.get(3));
                }

                // Update the current check's max score
                if (score > currentCheckMaxScore) {
                    currentCheckMaxScore = score;
                }
            }

            // Update the overall max score if the current check's max score is greater
            if (currentCheckMaxScore > maxScore) {
                maxScore = currentCheckMaxScore;
            }
        }

        return maxScore;
    }

    private int getScorePublicCard(Card card) {
        Rank rank = (Rank) card.getRank();
        return rank.getScoreCardValue() * Suit.PUBLIC_CARD_MULTIPLICATION_FACTOR;
    }
}
