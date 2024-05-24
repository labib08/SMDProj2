package lucky.players;

import ch.aplu.jcardgame.*;
import lucky.CardsDealer;
import lucky.Rank;
import lucky.Suit;
import java.util.*;

public class BasicPlayer extends Player {
    int getScorePrivateCard(Card card) {
        Rank rank = (Rank) card.getRank();
        Suit suit = (Suit) card.getSuit();
        return rank.getScoreCardValue() * suit.getMultiplicationFactor();
    }
    public Card selectCardToDiscard(int delayTime, Hand playingArea, List<Card> cardsPlayed) {
        Card selected = getLowestScoreCard(getHand(), delayTime);
        return selected;
    }

    public BasicPlayer(int position, CardsDealer cardsDealer) {
        super(position, cardsDealer);
    }
    public Card getLowestScoreCard(Hand hand, int thinkingTime){

        getCardsDealer().dealACardToHand(hand, getCardsDealer().getPack());

        delay(thinkingTime);
        int min = 57;
        int score;
        Card card = null;
        for (int i =0; i< hand.getCardList().size(); i++) {
            score = getScorePrivateCard(hand.getCardList().get(i));
            if (score < min) {
                min = score;
                card = hand.getCardList().get(i);
            }
        }
        return card;
    }
}
