package lucky.gameFunctions;

import ch.aplu.jgamegrid.*;
import lucky.LuckyThirdteen;
import lucky.gameFunctions.Graphics;
import lucky.players.Player;

import java.awt.*;

public class ScoreGraphics extends Graphics {
    private Actor[] scoreActors = {null, null, null, null};
    Font bigFont = new Font("Arial", Font.BOLD, 36);
    private final Location[] scoreLocations = {
            new Location(575, 675),
            new Location(25, 575),
            new Location(575, 25),
            new Location(575, 575)
    };

    public ScoreGraphics(int nbPlayers){
        super(nbPlayers);
    }

    public void initScore(LuckyThirdteen game, Player[] players) {
        for (int i = 0; i < nbPlayers; i++) {
            String text = "[" + String.valueOf(players[i].getScore()) + "]";
            scoreActors[i] = new TextActor(text, Color.WHITE, game.bgColor, bigFont);
            game.addActor(scoreActors[i], scoreLocations[i]);
        }
    }

    public void updateScore(LuckyThirdteen game, Player[] player) {
        for (int i = 0; i < nbPlayers; i++) {
            game.removeActor(scoreActors[i]);
            int displayScore = Math.max(player[i].getScore(), 0);
            String text = "P" + i + "[" + String.valueOf(displayScore) + "]";
            scoreActors[i] = new TextActor(text, Color.WHITE, game.bgColor, bigFont);
            game.addActor(scoreActors[i], scoreLocations[i]);
        }
    }
}
