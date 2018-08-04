import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {

    private static final InetSocketAddress serverAddress = new InetSocketAddress("localhost", 4242);
    private static Optional<Server> server = Optional.empty();

    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private Selector selector;
    private ServerSocketChannel serverChannel;

    private Server(){}

    public static Server getServer(){
        if(!server.isPresent()) {
            server = Optional.of(new Server());
        }
        return server.get();
    }

    public void start() throws Exception {
        if (isRunning.get()) {
            throw new Exception("A Server has already been started.");
        }

        final Runnable clientAcceptor = (() -> {
            try {
                selector = Selector.open();
                serverChannel = ServerSocketChannel.open();
                serverChannel.bind(serverAddress);
                serverChannel.configureBlocking(false);
                serverChannel.register(selector, SelectionKey.OP_ACCEPT);

                isRunning.set(true);
                Logger.info("Server has started.");
                while (isRunning.get()) {
                    selector.select();
                    final Set<SelectionKey> keys = selector.selectedKeys();
                    final Iterator<SelectionKey> keyIterator = keys.iterator();

                    while(keyIterator.hasNext()){
                        final SelectionKey key = keyIterator.next();
                        if(key.isAcceptable()){
                            registerClient(key, selector);
                        }
                    }
                }
            } catch(final IOException e){
                e.printStackTrace();
            }
        });

        new Thread(clientAcceptor).start();
    }

    private void registerClient(final SelectionKey key, final Selector selector){
        try {
            final SocketChannel clientChannel = (SocketChannel) key.channel();
            clientChannel.configureBlocking(false);
            clientChannel.register(selector, SelectionKey.OP_READ);
        } catch(final IOException e){
            Logger.error("Failed to register client", e);
        }
    }

    public void stop() throws Exception {
        serverChannel.close();
        selector.close();
        isRunning.set(false);
    }
}
