package lightleaf.netcom.player;

import lightleaf.netcom.client.Client;
import lightleaf.netcom.common.Logger;
import lightleaf.netcom.common.MessageHeaderIndex;
import lightleaf.netcom.common.OpCode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class PlayerPool {

    private final int size;
    private final List<String> usernames;
    private final List<String> passwords;
    private final List<Client> clients;

    public PlayerPool(final int size){
        this.size = size;
        usernames = new ArrayList<>(size);
        passwords = new ArrayList<>(size);
        clients = new ArrayList<>(size);
    }

    public void addPlayer(final LoginDetails loginDetails, final Client client){
        usernames.add(loginDetails.getUsername());
        passwords.add(loginDetails.getPassword());
        clients.add(client);
    }

    public void doAction(final int playerIndex){
        if(!clients.get(playerIndex).isConnected()){
            login(playerIndex);
        } else{
            Logger.debug("No action available");
        }
    }

    private void login(final int playerIndex){
        try {
            final Client client = clients.get(playerIndex);
            client.connect();

            ByteBuffer byteBuffer = ByteBuffer.allocate(256);
            byteBuffer.put(MessageHeaderIndex.OPCODE, OpCode.LOGIN);
            byteBuffer.position(1);
            byteBuffer.put(usernames.get(playerIndex).getBytes());
            byteBuffer.put((byte) '\0');
            byteBuffer.put(passwords.get(playerIndex).getBytes());
            byteBuffer.clear();

            clients.get(playerIndex).sendToServer(byteBuffer);
            //clients.get(playerIndex).sendForEcho(new String(byteBuffer.array()));
        } catch(final IOException e){
            Logger.error("Failed to connect client", e);
        }
    }
}

