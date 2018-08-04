public class Driver {

    public static void main(String[] args){
        final Server server = Server.getServer();

        try {
            server.start();
        } catch(final Exception e){
            Logger.error("Failed to start the server", e);
        }
    }
}
