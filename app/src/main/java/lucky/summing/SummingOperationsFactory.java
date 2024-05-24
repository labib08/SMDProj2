package lucky.summing;

public class SummingOperationsFactory {
    private static SummingOperationsFactory instance = null;

    private SummingOperationsFactory() {
    }

    public static synchronized SummingOperationsFactory getInstance() {
        if (instance == null) {
            instance = new SummingOperationsFactory();
        }
        return instance;
    }

    public SummingCardsStrategy getSummingStrategy(int n) {
        switch (n) {
            case 0:
                return new TwoPrivateStrategy();
            case 1:
                return new OnePrivateOnePublicStrategy();
            case 2:
                return new FourCardsStrategy();
            default:
                throw new IllegalArgumentException("Unknown strategy type: " + n);
        }
    }
}