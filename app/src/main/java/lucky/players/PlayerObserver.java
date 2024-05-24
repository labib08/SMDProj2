package lucky.players;

import ch.aplu.jcardgame.*;
import java.util.List;

public interface PlayerObserver {
    void notify(int playerIndex, List<Card> cards);
}
