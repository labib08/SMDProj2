package lucky.summing;

import ch.aplu.jcardgame.*;

import java.util.ArrayList;
import java.util.*;

public class SummingOperations {

    public List<Map<String, ArrayList<Card>>> isThirteen(List<Card> privateCards, List<Card> publicCards) {
        List<Map<String, ArrayList<Card>>> cardsAndStrategy = new ArrayList<>();

        int numberOfSummingStrategies = 3;

        SummingOperationsFactory summingOperationsFactory = SummingOperationsFactory.getInstance();
        SummingCardsStrategy summingStrategy;

        for (int i = 0; i < numberOfSummingStrategies; i++){
            summingStrategy = summingOperationsFactory.getSummingStrategy(i);

            Map<String, ArrayList<Card>> result = summingStrategy.isThirteen(privateCards, publicCards);

            if (!result.isEmpty()) {  // If the strategy resulted in getting to sum up the cards to 13
                cardsAndStrategy.add(result);
            }

        }
        return cardsAndStrategy;
    }
}