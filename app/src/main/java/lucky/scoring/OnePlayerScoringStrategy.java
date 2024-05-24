package lucky.scoring;

import ch.aplu.jcardgame.*;
import lucky.scoring.ScoringSystemStrategy;

import java.util.*;

public class OnePlayerScoringStrategy extends ScoringSystemStrategy {
    @Override
    public int calculateScore(int playerIndex, List<Hand> hands, List<Map<String, ArrayList<Card>>> thirteenChecks) {
        return 100;
    }
}
