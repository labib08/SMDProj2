package lucky;

import ch.aplu.jcardgame.Card;
import lucky.players.PlayerObserver;


public interface Subject {
    void registerObserver(PlayerObserver observer);
    void removeObserver(PlayerObserver observer);
    void notifyObservers(Card card);
}
