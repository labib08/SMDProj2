package lucky.scoring;

public class ScoringSystemStrategyFactory {
    private static ScoringSystemStrategyFactory instance = null;

    private ScoringSystemStrategyFactory() {
    }

    public static synchronized ScoringSystemStrategyFactory getInstance() {
        if (instance == null) {
            instance = new ScoringSystemStrategyFactory();
        }
        return instance;
    }

    public ScoringSystemStrategy getScoringStrategy(String strategyType) {
        switch (strategyType) {
            case "Case1":
                return new OnePlayerScoringStrategy();
            case "Case2":
                return new NoPlayersScoringStrategy();
            case "Case3":
                return new MultiplePlayersScoringStrategy();
            default:
                throw new IllegalArgumentException("Unknown strategy type: " + strategyType);
        }
    }
}