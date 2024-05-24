package lucky.summing;

import ch.aplu.jcardgame.*;
import lucky.Rank;
import lucky.summing.SummingCardsStrategy;

import java.util.*;

public class TwoPrivateStrategy implements SummingCardsStrategy {

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

    public Map<String, ArrayList<Card>> isThirteen(List<Card> privateCards, List<Card> publicCards) {
        Map<String, ArrayList<Card>> strategyAndCards = new HashMap<>();
        Rank rank1 = (Rank) privateCards.get(0).getRank();
        Rank rank2 = (Rank) privateCards.get(1).getRank();

        if (isThirteenFromPossibleValues(rank1.getPossibleSumValues(), rank2.getPossibleSumValues())) {
            ArrayList<Card> cards = new ArrayList<>();
            cards.add(privateCards.get(0));
            cards.add(privateCards.get(1));

            strategyAndCards.put("bothPrivate", cards);
        }

        return strategyAndCards;
    }

}