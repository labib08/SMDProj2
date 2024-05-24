package lucky.players;

import ch.aplu.jcardgame.*;
import lucky.CardsDealer;
import lucky.LuckyThirdteen;
import lucky.Rank;
import lucky.Suit;

import java.util.*;

public class CleverPlayer extends Player {
    int THIRTEEN_GOAL = 13;
    Hand playingArea;
    List<Card>cardsPlayed = new ArrayList<>();

    List <Integer> cardType = new ArrayList<>();
    private int getScorePublicCard(Card card) {
        Rank rank = (Rank) card.getRank();
        return rank.getScoreCardValue() * Suit.PUBLIC_CARD_MULTIPLICATION_FACTOR;
    }
    int getScorePrivateCard(Card card) {
        Rank rank = (Rank) card.getRank();
        Suit suit = (Suit) card.getSuit();
        return rank.getScoreCardValue() * suit.getMultiplicationFactor();
    }
    public void addPossibleValues(HashMap<Integer, Integer> map, int[] possibleValues) {
        for (int cardValue : possibleValues) {
            if (map.containsKey(cardValue)) {
                map.put(cardValue, map.get(cardValue) + 1);
            }
            else {
                map.put(cardValue, 1);
            }
        }
    }
    public void initPossibleCardsMap(HashMap<Integer, Integer> map, List<Card> packList) {
        for (int i =0; i< packList.size(); i++) {
            Rank cardRank = (Rank)packList.get(i).getRank();
            addPossibleValues(map, cardRank.getPossibleSumValues());
        }
    }
    public void removePossibleValues(HashMap<Integer, Integer> map,int[] possibleValues) {
        for (int cardValue : possibleValues) {
            if (map.containsKey(cardValue)) {
                map.put(cardValue, map.get(cardValue)-1);
            }
        }
    }
    public void removePrivAndPubCard(HashMap<Integer, Integer> map, List<Card> privateCards, List<Card> publicCards) {
        Rank cardRank = null;
        for (int i =0; i< 2; i++) {
            cardRank = (Rank)privateCards.get(i).getRank();
            removePossibleValues(map, cardRank.getPossibleSumValues());
        }
        for (int i =0; i< 2; i++) {
            cardRank = (Rank)publicCards.get(i).getRank();
            removePossibleValues(map, cardRank.getPossibleSumValues());
        }

    }

    public void findRequiredScore(List<Integer> reqScore, Card card) {
        Rank rank = (Rank)card.getRank();
        requiredScoreFromPossibleValues(reqScore, rank.getPossibleSumValues());
    }
    public void requiredScoreFromPossibleValues(List<Integer> reqScore, int[] possibleValues) {
        for (int value : possibleValues) {
            int requiredValue = THIRTEEN_GOAL - value;
            reqScore.add(requiredValue);
        }
    }
    public void sumPublicCardsFromPossibleValues(List<Integer> reqScore, int[] possibleValues1, int[] possibleValues2) {
        for (int value1 : possibleValues1) {
            for (int value2 : possibleValues2) {
                reqScore.add(value1 + value2);
            }
        }
    }
    public void sumPublicCards(List<Integer> reqScore, Card card1, Card card2) {
        Rank rank1 = (Rank)card1.getRank();
        Rank rank2 = (Rank)card2.getRank();
        sumPublicCardsFromPossibleValues(reqScore, rank1.getPossibleSumValues(), rank2.getPossibleSumValues());
    }
    public Card findLowProbCard(List<Card> privateCards, List<Card> publicCards, HashMap<Integer, Integer> map) {
        Card card = null;
        card = privateCards.get(0);
        List<Integer> reqScorePriv1 = new ArrayList<>();
        List<Integer> reqScorePriv2 = new ArrayList<>();
        List<Integer> reqScorePriv3 = new ArrayList<>();
        List<Integer> reqScorePub = new ArrayList<>();
        findRequiredScore(reqScorePriv1, privateCards.get(0));
        findRequiredScore(reqScorePriv2, privateCards.get(1));
        findRequiredScore(reqScorePriv3, privateCards.get(2));
        sumPublicCards(reqScorePub, publicCards.get(0), publicCards.get(1));
        System.out.println(reqScorePriv1);
        System.out.println(reqScorePriv2);
        System.out.println(reqScorePriv3);
        System.out.println(reqScorePub);
        card = findProbability(reqScorePriv1, reqScorePriv2, reqScorePriv3, reqScorePub, map, privateCards);
        return card;
    }

    private boolean isThirteenFromPossibleValues(int[] possibleValues1, int[] possibleValues2) {
        for (int value1 : possibleValues1) {
            for (int value2 : possibleValues2) {
                if (value1 + value2 == THIRTEEN_GOAL) {
                    return true;
                }
            }
        }
        return false;
    }
    private boolean isThirteenFromPossibleValuesAll(int[] possibleValues1, int[] possibleValues2, int[] possibleValues3, int[] possibleValues4) {
        for (int value1 : possibleValues1) {
            for (int value2 : possibleValues2) {
                for (int value3 : possibleValues3) {
                    for (int value4 : possibleValues4) {
                        if (value1 + value2 + value3 + value4 == THIRTEEN_GOAL) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    private boolean isThirteenCards(Card card1, Card card2) {
        Rank rank1 = (Rank) card1.getRank();
        Rank rank2 = (Rank) card2.getRank();
        return isThirteenFromPossibleValues(rank1.getPossibleSumValues(), rank2.getPossibleSumValues());
    }
    private int calculateMaxScoreForThirteenPlayer(int playerIndex, List<Card> cardList, List<Integer> cardType) {
        List<Card> privateCards = getHand().getCardList();
        List<Card> publicCards = playingArea.getCardList();
        Card privateCard1 = privateCards.get(0);
        Card privateCard2 = privateCards.get(1);
        Card privateCard3 = null;
        Card publicCard1 = publicCards.get(0);
        Card publicCard2 = publicCards.get(1);

        int maxScore = 0;
        cardList.clear();
        cardType.clear();
        if (privateCards.size() == 3) {
            System.out.println("SJCNSJCDBNJSDBJDJDJ");
            privateCard3 = privateCards.get(2);
            if (isThirteenCards(privateCard1, privateCard3)) {

                int score = getScorePrivateCard(privateCard1) + getScorePrivateCard(privateCard3);
                if (maxScore < score) {
                    maxScore = score;
                    cardList.clear();
                    cardType.clear();
                    cardList.add(privateCard1);
                    cardList.add(privateCard3);
                    cardType.add(0);
                    cardType.add(1);
                }
            }
            if (isThirteenCards(privateCard2, privateCard3)) {

                int score = getScorePrivateCard(privateCard2) + getScorePrivateCard(privateCard3);
                if (maxScore < score) {
                    maxScore = score;
                    cardList.clear();
                    cardType.clear();
                    cardList.add(privateCard2);
                    cardList.add(privateCard3);
                    cardType.add(0);
                    cardType.add(1);
                }
            }
            if (isThirteenCards(privateCard3, publicCard1)) {
                int score = getScorePrivateCard(privateCard3) + getScorePublicCard(publicCard1);
                if (maxScore < score) {
                    maxScore = score;
                    cardList.clear();
                    cardType.clear();
                    cardList.add(privateCard3);
                    cardList.add(publicCard1);
                    cardType.add(0);
                    cardType.add(2);
                }
            }
            if (isThirteenCards(privateCard3, publicCard2)) {
                int score = getScorePrivateCard(privateCard3) + getScorePublicCard(publicCard2);
                if (maxScore < score) {
                    maxScore = score;
                    cardList.clear();
                    cardType.clear();
                    cardList.add(privateCard3);
                    cardList.add(publicCard2);
                    cardType.add(0);
                    cardType.add(3);
                }
            }
            if (isThirteenCardsAll(privateCard1, privateCard3, publicCard1, publicCard2)) {
                int score = getScorePrivateCard(privateCard1) + getScorePrivateCard(privateCard3)
                        + getScorePublicCard(publicCard1) + getScorePublicCard(publicCard2);
                if (maxScore < score) {
                    maxScore = score;
                    cardList.clear();
                    cardType.clear();
                    cardList.add(privateCard1);
                    cardList.add(privateCard3);
                    cardList.add(publicCard1);
                    cardList.add(publicCard2);
                    cardType.add(0);
                    cardType.add(1);
                    cardType.add(2);
                    cardType.add(3);
                }
            }
            if (isThirteenCardsAll(privateCard3, privateCard2, publicCard1, publicCard2)) {
                int score = getScorePrivateCard(privateCard3) + getScorePrivateCard(privateCard2)
                        + getScorePublicCard(publicCard1) + getScorePublicCard(publicCard2);
                if (maxScore < score) {
                    maxScore = score;
                    cardList.clear();
                    cardType.clear();
                    cardList.add(privateCard3);
                    cardList.add(privateCard2);
                    cardList.add(publicCard1);
                    cardList.add(publicCard2);
                    cardType.add(0);
                    cardType.add(1);
                    cardType.add(2);
                    cardType.add(3);
                }
            }
        }

        if (isThirteenCards(privateCard1, privateCard2)) {
            int score = getScorePrivateCard(privateCard1) + getScorePrivateCard(privateCard2);
            if (maxScore < score) {
                maxScore = score;
                cardList.clear();
                cardType.clear();
                cardList.add(privateCard1);
                cardList.add(privateCard2);
                cardType.add(0);
                cardType.add(1);
            }
        }


        if (isThirteenCards(privateCard1, publicCard1)) {
            int score = getScorePrivateCard(privateCard1) + getScorePublicCard(publicCard1);
            if (maxScore < score) {
                maxScore = score;
                cardList.clear();
                cardType.clear();
                cardList.add(privateCard1);
                cardList.add(publicCard1);
                cardType.add(0);
                cardType.add(2);
            }
        }

        if (isThirteenCards(privateCard1, publicCard2)) {
            int score = getScorePrivateCard(privateCard1) + getScorePublicCard(publicCard2);
            if (maxScore < score) {
                maxScore = score;
                cardList.clear();
                cardType.clear();
                cardList.add(privateCard1);
                cardList.add(publicCard2);
                cardType.add(0);
                cardType.add(3);
            }
        }

        if (isThirteenCards(privateCard2, publicCard1)) {
            int score = getScorePrivateCard(privateCard2) + getScorePublicCard(publicCard1);
            if (maxScore < score) {
                maxScore = score;
                cardList.clear();
                cardType.clear();
                cardList.add(privateCard2);
                cardList.add(publicCard1);
                cardType.add(1);
                cardType.add(2);
            }
        }

        if (isThirteenCards(privateCard2, publicCard2)) {
            int score = getScorePrivateCard(privateCard2) + getScorePublicCard(publicCard2);
            if (maxScore < score) {
                maxScore = score;
                cardList.clear();
                cardType.clear();
                cardList.add(privateCard2);
                cardList.add(publicCard2);
                cardType.add(1);
                cardType.add(3);
            }
        }

        if (isThirteenCardsAll(privateCard1, privateCard2, publicCard1, publicCard2)) {
            int score = getScorePrivateCard(privateCard1) + getScorePrivateCard(privateCard2)
                    + getScorePublicCard(publicCard1) + getScorePublicCard(publicCard2);
            if (maxScore < score) {
                maxScore = score;
                cardList.clear();
                cardType.clear();
                cardList.add(privateCard1);
                cardList.add(privateCard2);
                cardList.add(publicCard1);
                cardList.add(publicCard2);
                cardType.add(0);
                cardType.add(1);
                cardType.add(2);
                cardType.add(3);
            }
        }

        return maxScore;
    }

    private boolean isThirteenCardsAll(Card card1, Card card2, Card card3, Card card4) {
        Rank rank1 = (Rank) card1.getRank();
        Rank rank2 = (Rank) card2.getRank();
        Rank rank3 = (Rank) card3.getRank();
        Rank rank4 = (Rank) card4.getRank();
        return isThirteenFromPossibleValuesAll(rank1.getPossibleSumValues(), rank2.getPossibleSumValues(), rank3.getPossibleSumValues(), rank4.getPossibleSumValues());
    }

    public CleverPlayer(int position, CardsDealer cardsDealer) {
        super(position, cardsDealer);
    }

    public Card selectCardToDiscard(int delayTime, Hand playingArea) {
        this.playingArea = playingArea;

        Card selected = getCleverPlayerCard(getHand(), getPosition(), cardsPlayed, getCardsDealer().getPack().getCardList(), possibleCardsMap, delayTime);
        return selected;
    }

    private List<PlayerObserver> observers = new ArrayList<>();
    HashMap<Integer, Integer> possibleCardsMap = new HashMap<>();

    public void registerObserver(PlayerObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(PlayerObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(int playerIndex, List<Card> cards) {
        for (PlayerObserver observer : observers) {
            observer.notify(playerIndex, cards);
        }
    }
//    public Card selectCardToDiscardClever(int delayTime, List<Card> cardsPlayed) {
//        Card selected = getCleverPlayerCard(getHand(), getPosition(), cardsPlayed, getCardsDealer().getPack(), possibleCardsMap, delayTime);
//        return selected;
//    }

    public Card findProbability(List<Integer> reqScorePriv1, List<Integer> reqScorePriv2, List<Integer> reqScorePriv3, List<Integer> reqScorePrivCards, HashMap<Integer, Integer> map, List<Card> privateCards) {
        int num = -1;
        int freq = 0;
        int minFreq = 57;
        for (int i=0; i<reqScorePriv1.size(); i++) {
            if (map.containsKey(reqScorePriv1.get(i))) {
                freq += map.get(reqScorePriv1.get(i));
            }
        }
        System.out.println(freq);

        if (freq < minFreq) {
            minFreq = freq;
            num = 0;
        }
        freq = 0;
        for (int i=0; i<reqScorePriv2.size(); i++) {
            if (map.containsKey(reqScorePriv2.get(i))) {
                freq += map.get(reqScorePriv2.get(i));
            }
        }
        System.out.println(freq);
        if (freq == minFreq) {
            int currCardVal = getScorePrivateCard(privateCards.get(1));
            int minCardVal = getScorePrivateCard(privateCards.get(num));
            if (currCardVal < minCardVal) {
                num = 1;
            }
        }
        if (freq < minFreq) {
            minFreq = freq;
            num = 1;
        }
        freq = 0;
        for (int i=0; i<reqScorePriv3.size(); i++) {
            if (map.containsKey(reqScorePriv3.get(i))) {
                freq += map.get(reqScorePriv3.get(i));
            }
        }
        System.out.println(freq);
        if (freq == minFreq) {
            int currCardVal = getScorePrivateCard(privateCards.get(2));
            int minCardVal = getScorePrivateCard(privateCards.get(num));
            if (currCardVal < minCardVal) {
                num = 2;
            }
        }
        if (freq < minFreq) {
            minFreq = freq;
            num = 2;
        }


        for (int j =0; j< privateCards.size(); j++) {
            Rank rank = (Rank)privateCards.get(j).getRank();
            freq = 0;
            for (int i =0; i< reqScorePrivCards.size(); i++) {
                freq += getAllPossibleFreq(rank.getPossibleSumValues(), reqScorePrivCards.get(i), map);
            }
            if (freq == minFreq) {
                int currCardVal = getScorePrivateCard(privateCards.get(j));
                int minCardVal = getScorePrivateCard(privateCards.get(num));
                if (currCardVal < minCardVal) {
                    num = j;
                }
            }
            if (freq < minFreq) {
                minFreq = freq;
                num = j;
            }
        }
        System.out.println("Lowest == " + privateCards.get(num));
        return privateCards.get(num);
    }
    public int getAllPossibleFreq(int[] possibleValues, int sum, HashMap<Integer, Integer> map) {
        int freq =0;
        for (int value : possibleValues) {
            int requiredVal = THIRTEEN_GOAL - (sum + value);
            if (map.containsKey(requiredVal)) {
                freq += map.get(requiredVal);
            }
        }
        return freq;
    }

    public Card getLowestScoreCard(List <Card> cardList) {

        int min = 57;
        int score = 0;
        Card card = null;
        for (int i =0; i< cardList.size(); i++) {
            score = getScorePrivateCard(cardList.get(i));
            if (score < min) {
                min = score;
                card = cardList.get(i);
            }
        }
        return card;
    }

    public Card getCleverPlayerCard(Hand hand, int nextPlayer, List<Card> cardsPlayed, List<Card> packList, HashMap<Integer, Integer> map, int thinkingTime) {
        System.out.println("CARDS PLAYED ======= " + cardsPlayed);
        getCardsDealer().dealACardToHand(hand, getCardsDealer().getPack());
        System.out.println(packList);
        System.out.println("Size of CardList == " + packList.size());
        System.out.println(hand.getCardList());
        updateMap(map, cardsPlayed, hand.getCardList());
        System.out.println(("Map: " + map));

        List<Card> cardsToDiscard = new ArrayList<>(hand.getCardList());
        List <Card> isThirteenCardList = new ArrayList<>();
        int score = calculateMaxScoreForThirteenPlayer(nextPlayer, isThirteenCardList, cardType);
        delay(thinkingTime);
        System.out.println("Score == " + score + " Cards == " + isThirteenCardList + " CardType == " + cardType);
        if (score > 0) {
            if (cardType.size() == 2) {
                if (cardType.contains(0) && cardType.contains(1)) {
                    cardsToDiscard.remove(isThirteenCardList.get(0));
                    cardsToDiscard.remove(isThirteenCardList.get(1));
                }
                else {
                    cardsToDiscard.remove(isThirteenCardList.get(0));
                }
            }
            else if (cardType.size() == 4) {
                cardsToDiscard.remove(isThirteenCardList.get(0));
                cardsToDiscard.remove(isThirteenCardList.get(1));
            }
        }
        else {
            return findLowProbCard(hand.getCardList(), playingArea.getCardList(), map);
        }


        if (cardsToDiscard.size() == 1) {
            return cardsToDiscard.get(0);
        }
        return getLowestScoreCard(cardsToDiscard);
    }
    public void updateMap(HashMap<Integer, Integer> map, List<Card> cardsPlayed, List<Card> handCard) {
        int n = cardsPlayed.size();
        for (int i =0; i<n; i++) {
            Rank cardRank = (Rank)cardsPlayed.get(i).getRank();
            Integer cardPlayedValue = cardRank.getRankCardValue();
            if (map.containsKey(cardPlayedValue)) {
                map.put(cardPlayedValue, map.get(cardPlayedValue)-1);
            }
        }

        int getCard = handCard.size() -1;
        Rank handCardRank = (Rank)handCard.get(getCard).getRank();
        Integer handCardValue = handCardRank.getRankCardValue();
        if (map.containsKey(handCardValue)) {
            map.put(handCardValue, map.get(handCardValue)-1);
        }
    }
}
