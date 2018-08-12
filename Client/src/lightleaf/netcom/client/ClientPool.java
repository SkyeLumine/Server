package lightleaf.netcom.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientPool {

    private List<Client> clients = new ArrayList<>();

    public void add(final Client client){
        clients.add(client);
    }

    public Optional<Client> get(final int index){
        if(index < clients.size()){
            return Optional.of(clients.get(index));
        }
        return Optional.empty();
    }

    public Optional<Client> get(final Client targetClient){
        int targetIndex = clients.indexOf(targetClient);
        if(targetIndex != -1){
            return Optional.of(clients.get(targetIndex));
        }
        return Optional.empty();
    }

    public void connect() throws IOException {
        for(final Client client : clients){
            client.connect();
        }
    }

    public void close() throws IOException {
        for(final Client client : clients){
            client.close();
        }
    }

}
