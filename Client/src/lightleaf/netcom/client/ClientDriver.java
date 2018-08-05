package lightleaf.netcom.client;

import lightleaf.netcom.common.Logger;

import java.io.IOException;

public class ClientDriver {

    public static void main(String[] args) {

        final String[] msgs = {"Hi!", "Hello!", "Greetings!"};

        final Client client = new Client();
        try {
            client.connect();
        } catch(final IOException e){
            Logger.error("Client could not connect to server", e);
        }

        for(int i = 0; i < 5; ++i){
            client.sendForEcho(msgs[(int) (Math.random() * (msgs.length - 0.1))]);
            try{Thread.sleep(1000);}catch(final InterruptedException e){}
        }

        try {
            client.close();
        } catch(final IOException e){
            Logger.error("Client could not disconnect from server", e);
        }
    }
}
