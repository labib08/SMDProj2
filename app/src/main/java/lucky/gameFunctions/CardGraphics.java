package lucky.gameFunctions;

import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jcardgame.TargetArea;
import ch.aplu.jgamegrid.Location;
import lucky.LuckyThirdteen;
import lucky.players.Player;

public class CardGraphics extends Graphics {
    private RowLayout[] layouts;
    private final int handWidth = 400;
    private final Location trickLocation = new Location(350, 350);
    private final int trickWidth = 40;
    private final Location[] handLocations = {
            new Location(350, 625),
            new Location(75, 350),
            new Location(350, 75),
            new Location(625, 350)
    };

    public CardGraphics(int nbPlayers){
        super(nbPlayers);
    }

    public void setUpPublicCardGraphics(LuckyThirdteen game, Hand playingArea){
        playingArea.setView(game, new RowLayout(trickLocation, (playingArea.getNumberOfCards() + 2) * trickWidth));
        playingArea.draw();
    }

    public void setUpPlayerGraphics(LuckyThirdteen game, Player[] players){
        layouts = new RowLayout[nbPlayers];
        for (int i = 0; i < nbPlayers; i++) {
            layouts[i] = new RowLayout(handLocations[i], handWidth);
            layouts[i].setRotationAngle(90 * i);
            players[i].getHand().setView(game, layouts[i]);
            players[i].getHand().setTargetArea(new TargetArea(trickLocation));
            players[i].getHand().draw();
        }
    }
}
