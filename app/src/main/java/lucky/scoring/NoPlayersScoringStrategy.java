package lucky.scoring;

import ch.aplu.jcardgame.*;
import lucky.scoring.PrivateScoreSumming;

import java.util.*;

public class NoPlayersScoringStrategy extends PrivateScoreSumming {

    @Override
    public int calculateScore(int playerIndex, List<Hand> hands, List<Map<String, ArrayList<Card>>> thirteenChecks) {
        List<Card> privateCards = hands.get(playerIndex).getCardList();
        return getScorePrivateCard(privateCards.get(0)) + getScorePrivateCard(privateCards.get(1));
    }
}
