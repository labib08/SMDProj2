package lucky.scoring;

import ch.aplu.jcardgame.*;

import java.util.*;

public abstract class ScoringSystemStrategy {
    public abstract int calculateScore(int playerIndex, List<Hand> hands, List <Map<String, ArrayList<Card>>> thirteenChecks);
}

