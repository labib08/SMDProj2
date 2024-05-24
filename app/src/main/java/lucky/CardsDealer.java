package lucky;

import ch.aplu.jcardgame.*;
import lucky.players.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CardsDealer {

    private Hand pack;

    public CardsDealer(Hand pack){
        this.pack = pack;
    }

    public static int getRandomInt(int bound) {
        return LuckyThirdteen.random.nextInt(bound);
    }

    public void setPack(Hand pack) {
        this.pack = pack;
    }

    public Hand getPack() {
        return pack;
    }
    public void dealingOut(int nbPlayers, int nbCardsPerPlayer, int nbSharedCards, Hand pack, Properties properties, Player[] players, Hand playingArea) {
        String initialShareKey = "shared.initialcards";
        String initialShareValue = properties.getProperty(initialShareKey);
        if (initialShareValue != null) {
            String[] initialCards = initialShareValue.split(",");
            for (String initialCard : initialCards) {
                if (initialCard.length() <= 1) {
                    continue;
                }
                Card card = getCardFromList(pack.getCardList(), initialCard);
                if (card != null) {
                    card.removeFromHand(true);
                    playingArea.insert(card, true);
                }
            }
        }
        int cardsToShare = nbSharedCards - playingArea.getNumberOfCards();

        for (int j = 0; j < cardsToShare; j++) {
            if (pack.isEmpty()) return;
            Card dealt = randomCard(pack.getCardList());
            dealt.removeFromHand(true);
            playingArea.insert(dealt, true);
        }

        for (int i = 0; i < nbPlayers; i++) {
            String initialCardsKey = "players." + i + ".initialcards";
            String initialCardsValue = properties.getProperty(initialCardsKey);
            if (initialCardsValue == null) {
                continue;
            }
            String[] initialCards = initialCardsValue.split(",");
            Card[] initialHand = new Card[nbCardsPerPlayer];
            for (int j = 0; j < initialCards.length; j++) {
                if (initialCards[j].length() <= 1) {
                    continue;
                }
                Card card = getCardFromList(pack.getCardList(), initialCards[j]);
                initialHand[j] = card;
            }
            players[i].setUpInitialCards(initialHand);
        }

        for (int i = 0; i < nbPlayers; i++) {
            int cardsToDealt = nbCardsPerPlayer - players[i].getHand().getNumberOfCards();
            for (int j = 0; j < cardsToDealt; j++) {
                if (pack.isEmpty()) return;
                Card dealt = randomCard(pack.getCardList());
                dealt.removeFromHand(false);
                players[i].getHand().insert(dealt, false);
            }
        }
    }

    public void dealACardToHand(Hand hand, Hand pack) {
        if (pack.isEmpty()) return;
        Card dealt = randomCard(pack.getCardList());
        dealt.removeFromHand(false);
        hand.insert(dealt, true);
    }

    public Card applyAutoMovement(Hand hand, String nextMovement, Hand pack) {
        if (pack.isEmpty()) return null;
        String[] cardStrings = nextMovement.split("-");
        String cardDealtString = cardStrings[0];
        Card dealt = getCardFromList(pack.getCardList(), cardDealtString);
        if (dealt != null) {
            dealt.removeFromHand(false);
            hand.insert(dealt, true);
        } else {
            System.out.println("cannot draw card: " + cardDealtString + " - hand: " + hand);
        }

        if (cardStrings.length > 1) {
            String cardDiscardString = cardStrings[1];
            return getCardFromList(hand.getCardList(), cardDiscardString);
        } else {
            return null;
        }
    }

    public static Card randomCard(ArrayList<Card> list) {
        int x = LuckyThirdteen.random.nextInt(list.size());
        return list.get(x);
    }

    private Card getCardFromList(List<Card> cards, String cardName) {
        Rank cardRank = getRankFromString(cardName);
        Suit cardSuit = getSuitFromString(cardName);
        for (Card card : cards) {
            if (card.getSuit() == cardSuit && card.getRank() == cardRank) {
                return card;
            }
        }
        return null;
    }

    private Rank getRankFromString(String cardName) {
        String rankString = cardName.substring(0, cardName.length() - 1);
        Integer rankValue = Integer.parseInt(rankString);

        for (Rank rank : Rank.values()) {
            if (rank.getRankCardValue() == rankValue) {
                return rank;
            }
        }

        return Rank.ACE;
    }

    private Suit getSuitFromString(String cardName) {
        String rankString = cardName.substring(0, cardName.length() - 1);
        String suitString = cardName.substring(cardName.length() - 1, cardName.length());
        Integer rankValue = Integer.parseInt(rankString);

        for (Suit suit : Suit.values()) {
            if (suit.getSuitShortHand().equals(suitString)) {
                return suit;
            }
        }
        return Suit.CLUBS;
    }
}
