package lucky.summing;

import ch.aplu.jcardgame.*;
import lucky.Rank;

import java.util.*;

public class OnePrivateOnePublicStrategy implements SummingCardsStrategy {

    private static final int THIRTEEN_GOAL = 13;

    public boolean isThirteenFromPossibleValues(int[] possibleValues1, int[] possibleValues2) {
        for (int value1 : possibleValues1) {
            for (int value2 : possibleValues2) {
                if (value1 + value2 == THIRTEEN_GOAL) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isThirteenCards(Card card1, Card card2) {
        Rank rank1 = (Rank) card1.getRank();
        Rank rank2 = (Rank) card2.getRank();
        return isThirteenFromPossibleValues(rank1.getPossibleSumValues(), rank2.getPossibleSumValues());
    }

    public Map<String, ArrayList<Card>> isThirteen(List<Card> privateCards, List<Card> publicCards) {
        Map<String, ArrayList<Card>> strategyAndCards = new HashMap<>();

        for (Card privateCard : privateCards) {
            for (Card publicCard : publicCards) {
                if (isThirteenCards(privateCard, publicCard)) {
                    ArrayList<Card> cards = new ArrayList<>();
                    cards.add(privateCard);
                    cards.add(publicCard);
                    strategyAndCards.put("publicAndPrivate", cards);
                }
            }
        }
        return strategyAndCards;
    }

}