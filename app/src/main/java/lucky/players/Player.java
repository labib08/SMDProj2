package lucky.players;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import lucky.CardsDealer;
import lucky.LuckyThirdteen;

import java.util.Arrays;
import java.util.List;

public abstract class Player extends Actor {
    private int position;
    private Hand hand;
    private int score;
    private List<String> movements;
    private CardsDealer cardsDealer;

    public Player(int position, CardsDealer cardsDealer){
        this.position = position;
        this.score = 0;
        this.cardsDealer = cardsDealer;
    }

    public CardsDealer getCardsDealer() {
        return cardsDealer;
    }

    public abstract Card selectCardToDiscard(int delayTime, Hand playingArea);

    public void setUpInitialCards(Card[] initialCards){ // sets up hand
        for (Card card : initialCards){
            if (card != null){
                card.removeFromHand(false);
                this.hand.insert(card, false);
            }
        }
    }

    public void setUpAutoMovement(String autoMovement){
        this.movements = Arrays.asList(autoMovement.split(","));
    }

    public void setHand(Hand hand){
        this.hand = hand;
    }

    public Hand getHand() {
        return hand;
    }

    public List<String> getMovements() {
        return movements;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public int getPosition() {
        return position;
    }
}
