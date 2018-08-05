package lightleaf.netcom.common;

public class Logger {

    private static boolean lastLogWasRelog = false;

    public static void info(final String msg){
        log("INFO: " + msg);
    }

    public static void received(final String msg){
        log("RECEIVED: " + msg);
    }

    public static void sent(final String msg){
        log("SENT: " + msg);
    }

    public static void debug(final String msg){
        log("DEBUG: " + msg);
    }

    public static void error(final String msg){
        log("ERROR: " + msg);
    }

    public static void error(final String msg, final Throwable exception){
        log("ERROR: " + msg);
        exception.printStackTrace();
    }

    public static void relogInfo(final String msg){
        relog("INFO: " + msg);
    }

    public static void relogDebug(final String msg){
        relog("DEBUG: " + msg);
    }

    public static void relogError(final String msg){
        relog("ERROR: " + msg);
    }

    private static void log(final String msg){
        if(lastLogWasRelog)
            System.out.println();

        System.out.println(msg);
        lastLogWasRelog = false;
    }

    public static void relog(final String msg){
        System.out.print("\r");
        System.out.print(msg);
        lastLogWasRelog = true;
    }
}
