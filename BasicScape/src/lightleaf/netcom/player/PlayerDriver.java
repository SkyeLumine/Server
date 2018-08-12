package lightleaf.netcom.player;

import lightleaf.netcom.client.Client;
import lightleaf.netcom.common.Logger;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerDriver {

    public static void main(String[] args) throws IOException {

        final int numberOfPlayers = 2000;

        final PlayerPool players = new PlayerPool(numberOfPlayers);

        final LoginDetailsGenerator loginDetailsGenerator = new LoginDetailsGenerator();
        for(int i = 0; i < numberOfPlayers; ++i) {
            players.addPlayer(loginDetailsGenerator.next(), new Client());
        }

        while(true) {
            final int randomIndex = ThreadLocalRandom.current().nextInt(numberOfPlayers);
            players.doAction(randomIndex);
            try{Thread.sleep(1);}catch(final Exception e){Logger.error("Error", e);}
        }
    }
}
