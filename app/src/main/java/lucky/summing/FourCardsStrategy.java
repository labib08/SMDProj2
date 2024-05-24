package lucky.summing;

import ch.aplu.jcardgame.*;
import lucky.Rank;

import java.util.*;

public class FourCardsStrategy implements SummingCardsStrategy {

    private static final int THIRTEEN_GOAL = 13;

    @Override
    public Map<String, ArrayList<Card>> isThirteen(List<Card> privateCards, List<Card> publicCards) {
        Map<String, ArrayList<Card>> strategyAndCards = new HashMap<>();

        ArrayList<Card> allCards = getTogetherCards(privateCards, publicCards);

        if (isThirteenFromFourCards(allCards)){
            strategyAndCards.put("allCards", allCards);
        }

        return strategyAndCards;
    }

    private ArrayList<Card> getTogetherCards(List<Card> privateCards, List<Card> publicCards) {
        ArrayList<Card> allCards = new ArrayList<>(privateCards);
        allCards.addAll(publicCards);
        return allCards;
    }

    private boolean isThirteenFromFourCards(List<Card> cards) {
        for (int i = 0; i < cards.size(); i++) {
            for (int j = i + 1; j < cards.size(); j++) {
                for (int k = j + 1; k < cards.size(); k++) {
                    for (int l = k + 1; l < cards.size(); l++) {
                        int sum = sumOfPossibleValues(cards.get(i), cards.get(j), cards.get(k), cards.get(l));
                        if (sum == THIRTEEN_GOAL) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private int sumOfPossibleValues(Card card1, Card card2, Card card3, Card card4) {
        Rank rank1 = (Rank) card1.getRank();
        Rank rank2 = (Rank) card2.getRank();
        Rank rank3 = (Rank) card3.getRank();
        Rank rank4 = (Rank) card4.getRank();

        int sum = rank1.getPossibleSumValues()[0] +
                rank2.getPossibleSumValues()[0] +
                rank3.getPossibleSumValues()[0] +
                rank4.getPossibleSumValues()[0];
        return sum;
    }
}