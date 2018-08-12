package lightleaf.netcom.client;

import lightleaf.netcom.common.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Optional;

public class Client {

    private final InetSocketAddress serverAddress = new InetSocketAddress("localhost", 4242);

    private Optional<SocketChannel> serverChannel = Optional.empty();

    public void connect() throws IOException {
        serverChannel = Optional.of(SocketChannel.open());
        serverChannel.get().connect(serverAddress);
    }

    public boolean isConnected(){
        if(serverChannel.isPresent()){
            if(serverChannel.get().isConnected()) {
                return true;
            }
        }
        return false;
    }

    public void close() throws IOException {
        serverChannel.get().close();
    }

    public void sendToServer(final ByteBuffer msg){
        serverChannel.ifPresent(server -> {
            try {
                server.write(msg);
                Logger.debug("Sent to server: " + new String(msg.array()));
            } catch(final IOException e){
                Logger.error("Failed to send message to server", e);
            }
        });
    }

    public void sendForEcho(final String msg) {
        serverChannel.ifPresent(channel -> {
            try {
                final ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                channel.write(buffer);
                Logger.sent(new String(buffer.array()));
                buffer.clear();
                channel.read(buffer);
                Logger.received(new String(buffer.array()));
            } catch(final IOException e){
                Logger.error("Failed to send message and receive echo", e);
            }
        });
    }
}
