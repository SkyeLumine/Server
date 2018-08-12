package lightleaf.netcom.server;

import entities.Constants;
import lightleaf.netcom.common.FileIO;
import lightleaf.netcom.common.Logger;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Map;

public class PlayerDatabase {

    private final Map<String, String> playerCredentials;

    public PlayerDatabase() throws IOException {
        final String workingDirectory = System.getProperty("user.dir") + "/Server/res";
        playerCredentials = FileIO.mapCSV(
                        FileSystems.getDefault().getPath(workingDirectory, "player_database.csv"), Constants.ACCOUNTS);
    }

    public boolean loginDetailsAreValid(final String username, final String password){
        final String mappedPassword = playerCredentials.get(username);
        if(mappedPassword != null){
            if(mappedPassword.equals(password)){
                return true;
            }
        }
        return false;
    }
}
