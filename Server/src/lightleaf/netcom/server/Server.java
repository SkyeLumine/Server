package lightleaf.netcom.server;

import entities.Constants;
import lightleaf.netcom.common.Logger;
import lightleaf.netcom.common.MessageHeaderIndex;
import lightleaf.netcom.common.OpCode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    private static final InetSocketAddress SERVER_ADDRESS = new InetSocketAddress("localhost", 4242);
    private static final int CLIENT_CLOSED_CONNECTION = -1;

    private static Optional<Server> server = Optional.empty();

    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private PlayerDatabase playerDatabase;
    private int count = 0;

    private Server(){
        try {
            playerDatabase = new PlayerDatabase();
        } catch(final IOException e){
            Logger.error("Failed to load player database", e);
            Logger.error("Server is shutting down");
            System.exit(1);
        }
    }

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

        final Runnable selectorTask = (() -> {
            try {
                selector = Selector.open();
                serverChannel = ServerSocketChannel.open();
                serverChannel.bind(SERVER_ADDRESS);
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
                            registerClient();
                        }

                        if(key.isReadable()){
                            try{
                                 processReadableKey(key);
                            } catch(final IOException e){
                                Logger.error("Failed to read from client", e);
                                key.channel().close();
                                Logger.info("Closed connection to client");
                            }
                        }

                        keyIterator.remove();
                    }
                }
            } catch(final IOException e){
                e.printStackTrace();
            }
        });

        new Thread(selectorTask).start();
    }

    private void processReadableKey(final SelectionKey key) throws IOException {
        final SocketChannel clientChannel = (SocketChannel) key.channel();
        final ByteBuffer buffer = ByteBuffer.allocate(256);
        final int readBytes = clientChannel.read(buffer);
        if(readBytes == CLIENT_CLOSED_CONNECTION){
            clientChannel.close();
            Logger.info("Client closed connection");
        } else {
            processMessage(clientChannel, buffer);
        }
    }

    private void processMessage(final SocketChannel clientChannel, final ByteBuffer buffer) throws IOException {

        if(buffer.get(MessageHeaderIndex.OPCODE) == OpCode.LOGIN){
            final String[] loginDetails = new String(buffer.array()).substring(MessageHeaderIndex.OPCODE).trim().split("\0");
            final String username = loginDetails[0];
            final String password = loginDetails[1];

            if(playerDatabase.loginDetailsAreValid(username, password)){
                Logger.info(username + " successfully logged in");
                Logger.info(++count + " of " + Constants.ACCOUNTS + " connected");
            } else {
                Logger.info(username + " failed to log in");
            }
        }
    }

    private void registerClient(){
        try {
            final SocketChannel clientChannel = serverChannel.accept();
            clientChannel.configureBlocking(false);
            clientChannel.register(selector, SelectionKey.OP_READ);
            Logger.info("Registered a new client");
        } catch(final IOException e){
            Logger.error("Failed to register client", e);
        }
    }

    public void stop() throws Exception {
        try {
            serverChannel.close();
        } catch(final IOException e){
            Logger.error("Failed to close server channel");
        } finally {
            try {
                selector.close();
            } catch(final IOException e){
                Logger.error("Failed to close server selector");
            } finally {
                isRunning.set(false);
            }
        }
    }
}
