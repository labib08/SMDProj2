package lucky.players;

import ch.aplu.jcardgame.*;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;
import lucky.CardsDealer;
import java.util.*;

public class HumanPlayer extends Player {

    Card selected;

    public HumanPlayer(int position, CardsDealer cardsDealer) {
        super(position, cardsDealer);
    }

    public Card selectCardToDiscard(int delayTime, Hand playingArea, List<Card> cardsPlayed){
        getHand().setTouchEnabled(true);
        selected = null;
        while (null == selected) delay(delayTime);
        return selected;
    }

    public void setUpForInteraction(){ // Set up human player for interaction

        CardListener cardListener = new CardAdapter()  // Human Player plays card
        {
            public void leftDoubleClicked(Card card) {
                selected = card;
                getHand().setTouchEnabled(false);
            }
        };
        this.getHand().addCardListener(cardListener);
    }
}
