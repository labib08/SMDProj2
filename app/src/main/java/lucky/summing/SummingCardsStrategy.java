package lucky.summing;

import ch.aplu.jcardgame.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface SummingCardsStrategy{

    Map<String, ArrayList<Card>> isThirteen(List<Card> privateCards, List<Card> publicCards);

}