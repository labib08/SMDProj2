package lucky;// LuckyThirteen.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import lucky.gameFunctions.CardGraphics;
import lucky.gameFunctions.ResultLogger;
import lucky.gameFunctions.ScoreGraphics;
import lucky.players.CleverPlayer;
import lucky.players.HumanPlayer;
import lucky.players.Player;
import lucky.players.PlayerFactory;
import lucky.scoring.ScoringOperations;
import lucky.summing.SummingOperations;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class LuckyThirdteen extends CardGame {
    final String trumpImage[] = {"bigspade.gif", "bigheart.gif", "bigdiamond.gif", "bigclub.gif"};

    static public final int seed = 30008;
    static final Random random = new Random(seed);
    private Properties properties;

    public boolean rankGreater(Card card1, Card card2) {
        return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
    }
    private final String version = "1.0";
    public final int nbPlayers = 4;
    public final int nbStartCards = 2;
    public final int nbFaceUpCards = 2;

    private static final int THIRTEEN_GOAL = 13;
    private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");

    private final Location textLocation = new Location(350, 450);
    private int thinkingTime = 2000;
    private int delayTime = 600;
    public void setStatus(String string) {
        setStatusText(string);
    }

    private int[] autoIndexHands = new int [nbPlayers];
    private boolean isAuto = false;
    private Hand playingArea;
    private Hand pack;
    private SummingOperations summingOperations;
    private ScoringOperations scoringOperations;
    private final CardsDealer cardsDealer;
    Rank[] ranks = Rank.values();
    Suit[] suits = Suit.values();
    private Player[] players = new Player[nbPlayers];
    ResultLogger resultLogger = new ResultLogger(players);
    CardGraphics cardGraphics = new CardGraphics(nbPlayers);
    ScoreGraphics scoreGraphics = new ScoreGraphics(nbPlayers);


    private void calculateScoreEndOfRound() {
        List<List<Map<String, ArrayList<Card>>>> isThirteenChecks = new ArrayList<>();

        for (int i = 0; i < players.length; i++) {
            List<Map<String, ArrayList<Card>>> result = summingOperations.isThirteen(players[i].getHand().getCardList(), playingArea.getCardList());
            isThirteenChecks.add(result);
        }

        List<Integer> indexesWithThirteen = new ArrayList<>(); // Players that have cards that add up to 13
        for (int i = 0; i < isThirteenChecks.size(); i++) {
            if (!isThirteenChecks.get(i).isEmpty()) {
                indexesWithThirteen.add(i);
            }
        }

        Hand[] hands = new Hand[nbPlayers];
        for (int i = 0; i < nbPlayers; i++){
            hands[i] = players[i].getHand();
        }

        scoringOperations.scoring(players, hands, isThirteenChecks, indexesWithThirteen);
    }

    private Card selected;

    private void initGame() {
        pack = deck.toHand(false);
        cardsDealer.setPack(pack);
        for (int i = 0; i < nbPlayers; i++) {
            players[i].setHand(new Hand(deck)); // sets up a hand for each player

        }
        playingArea = new Hand(deck);
        cardsDealer.dealingOut(nbPlayers, nbStartCards, nbFaceUpCards, pack, properties, players, playingArea);
        cardGraphics.setUpPublicCardGraphics(this, playingArea);

        for (int i = 0; i < nbPlayers; i++) {
            players[i].getHand().sort(Hand.SortType.SUITPRIORITY, false);
            if (players[i] instanceof HumanPlayer){
                HumanPlayer current = (HumanPlayer) players[i];
                current.setUpForInteraction();
            }
        }
        // graphics
        cardGraphics.setUpPlayerGraphics(this, players);
    }

    private void playGame() {
        // End trump suit
        int winner = 0;
        int roundNumber = 1;
        scoreGraphics.updateScore(this, players);

        List<Card>cardsPlayed = new ArrayList<>();
        resultLogger.addRoundInfoToLog(roundNumber);

        int currentPlayer = 0;
        while(roundNumber <= 4) {
            selected = null;
            boolean finishedAuto = false;

            if (isAuto) {
                int currentPlayerAutoIndex = autoIndexHands[currentPlayer];
                List<String> currentPlayerMovement = players[currentPlayer].getMovements();
                String nextMovement = "";

                if (currentPlayerMovement.size() > currentPlayerAutoIndex) {
                    nextMovement = currentPlayerMovement.get(currentPlayerAutoIndex);
                    currentPlayerAutoIndex++;

                    autoIndexHands[currentPlayer] = currentPlayerAutoIndex;
                    Hand nextHand = players[currentPlayer].getHand();

                    // Apply movement for player
                    selected = cardsDealer.applyAutoMovement(nextHand, nextMovement, pack);
                    delay(delayTime);
                    if (selected != null) {
                        selected.removeFromHand(true);
                    } else { // for when player runs out of auto movement sequence
                        selected = players[currentPlayer].selectCardToDiscard(thinkingTime, playingArea);
                        selected.removeFromHand(true);
                    }
                } else {
                    finishedAuto = true;
                }
            }

            if (!isAuto || finishedAuto) {
                if (players[currentPlayer] instanceof HumanPlayer) { // human player picking
                    HumanPlayer current = (HumanPlayer) players[currentPlayer];
                    setStatus("Player 0 is playing. Please double click on a card to discard");
                    cardsDealer.dealACardToHand(current.getHand(), pack);
                    selected = current.selectCardToDiscard(delayTime, playingArea);
                    selected.removeFromHand(true);

//                } else if (players[currentPlayer] instanceof CleverPlayer) {
//                    CleverPlayer current = (CleverPlayer) players[currentPlayer];
//
//
//                }

                }   else { // non-human player picking
                    setStatusText("Player " + currentPlayer + " thinking...");
                    selected = players[currentPlayer].selectCardToDiscard(thinkingTime, playingArea);
                    selected.removeFromHand(true);
                }
            }

            resultLogger.addCardPlayedToLog(currentPlayer, players[currentPlayer].getHand().getCardList(), ranks, suits);
            if (selected != null) {
                cardsPlayed.add(selected);
                selected.setVerso(false);  // In case it is upside down
                delay(delayTime);
                // End Follow
            }

            currentPlayer = (currentPlayer + 1) % nbPlayers;

            if (currentPlayer == 0) {
                roundNumber ++;
                resultLogger.addEndOfRoundToLog();

                if (roundNumber <= 4) {
                    resultLogger.addRoundInfoToLog(roundNumber);
                }
            }

            if (roundNumber > 4) {
                calculateScoreEndOfRound();
            }
            delay(delayTime);
        }
    }

    public void initPlayers(){
        for (int i = 0; i < nbPlayers; i++){
            String playerType = properties.getProperty("players."+i);
            String autoMovements = properties.getProperty("players."+i+".cardsPlayed");
            players[i] = PlayerFactory.getInstance().getPlayer(playerType, i, cardsDealer);

            if (autoMovements != null) {
                players[i].setUpAutoMovement(autoMovements);
            }

        }
    }

    public String runApp() {
        setTitle("LuckyThirteen (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");
        initPlayers();
        scoreGraphics.initScore(this, players);
        initGame();
        playGame();

        scoreGraphics.updateScore(this, players);
        int maxScore = 0;
        for (int i = 0; i < nbPlayers; i++) if (players[i].getScore() > maxScore) maxScore = players[i].getScore();
        List<Integer> winners = new ArrayList<Integer>();
        for (int i = 0; i < nbPlayers; i++) if (players[i].getScore() == maxScore) winners.add(i);
        String winText;
        if (winners.size() == 1) {
            winText = "Game over. Winner is player: " +
                    winners.iterator().next();
        } else {
            winText = "Game Over. Drawn winners are players: " +
                    String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList()));
        }
        addActor(new Actor("sprites/gameover.gif"), textLocation);
        setStatusText(winText);
        refresh();
        resultLogger.addEndOfGameToLog(winners);

        return resultLogger.getLogResult().toString();
    }

    public LuckyThirdteen(Properties properties) {
        super(700, 700, 30);
        this.properties = properties;
        isAuto = Boolean.parseBoolean(properties.getProperty("isAuto"));
        thinkingTime = Integer.parseInt(properties.getProperty("thinkingTime", "200"));
        delayTime = Integer.parseInt(properties.getProperty("delayTime", "50"));
        this.summingOperations = new SummingOperations();
        this.scoringOperations = new ScoringOperations();
        this.cardsDealer = new CardsDealer(pack);
    }
}