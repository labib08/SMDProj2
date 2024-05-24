package lucky.scoring;

import ch.aplu.jcardgame.*;
import lucky.players.Player;

import java.util.ArrayList;
import java.util.*;

public class ScoringOperations {
    public void scoring (Player[] players, Hand[] hands, List<List<Map<String, ArrayList<Card>>>> isThirteenChecks, List<Integer> indexesWithThirteen) {

        List<Hand> handsList = Arrays.asList(hands);
        ScoringSystemStrategyFactory factory = ScoringSystemStrategyFactory.getInstance();
        ScoringSystemStrategy scoringStrategy;
        int score;
        long playersWithThirteen = indexesWithThirteen.size();

        if (playersWithThirteen == 1) {
            scoringStrategy = factory.getScoringStrategy("Case1");
            int winnerIndex = indexesWithThirteen.get(0);
            score = scoringStrategy.calculateScore(winnerIndex, handsList, isThirteenChecks.get(0));
            players[winnerIndex].setScore(score);

        } else if (playersWithThirteen == 0){
            scoringStrategy = factory.getScoringStrategy("Case2");
            for (int i = 0; i < players.length; i++) {
                score = scoringStrategy.calculateScore(i, handsList, isThirteenChecks.get(0));
                players[i].setScore(score);
            }

        } else {
            scoringStrategy = factory.getScoringStrategy("Case3");
            for (Integer thirteenIndex : indexesWithThirteen) {
                score= scoringStrategy.calculateScore(thirteenIndex, handsList, isThirteenChecks.get(thirteenIndex));
                players[thirteenIndex].setScore(score);
            }
        }
    }
}
