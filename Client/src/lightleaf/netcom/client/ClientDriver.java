package lightleaf.netcom.client;

import lightleaf.netcom.common.Logger;

import java.io.IOException;
import java.util.Optional;

public class ClientDriver {

    public static void main(String[] args) {
        final int numClients = 10;
        final String[] msgs = {"Hi!", "Hello!", "Greetings!"};
        final ClientPool clientPool = new ClientPool();

        for(int i = 0; i < numClients; ++i){
            clientPool.add(new Client("Client #" + Integer.toString(i)));
        }

        try {
            clientPool.connect();

            for(int i = 0; i < numClients * 5; ++i){
                final int randomClientIndex = (int) (numClients * (Math.random() - 0.1));
                final int randomMsgIndex = (int) (msgs.length * (Math.random() - 0.1));
                final Optional<Client> client = clientPool.get(randomClientIndex);
                client.ifPresent(c -> c.sendForEcho(msgs[randomMsgIndex]));

                //try{Thread.sleep(1000);}catch(final InterruptedException e){}
            }

        } catch(final IOException e){
            Logger.error("Client could not connect to server", e);
        } finally {
            try {
                clientPool.close();
            } catch(final IOException e){
                Logger.error("Failed to close ClientPool");
            }
        }

        try {
            clientPool.close();
        } catch(final IOException e){
            Logger.error("Client could not close from server", e);
        }
    }
}
