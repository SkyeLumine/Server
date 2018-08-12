package lightleaf.netcom.client;

import lightleaf.netcom.common.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Optional;

public class Client {

    private final String name;
    private final InetSocketAddress serverAddress = new InetSocketAddress("localhost", 4242);

    private Optional<SocketChannel> serverChannel = Optional.empty();

    public Client(final String name){
        this.name = name;
    }

    public void connect() throws IOException {
        serverChannel = Optional.of(SocketChannel.open());
        serverChannel.get().connect(serverAddress);
    }

    public void close() throws IOException {
        serverChannel.get().close();
    }

    public void sendForEcho(final String msg) {
        serverChannel.ifPresent(channel -> {
            try {
                final String msgWithHeader = name + " " + msg;
                final ByteBuffer buffer = ByteBuffer.wrap(msgWithHeader.getBytes());
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
