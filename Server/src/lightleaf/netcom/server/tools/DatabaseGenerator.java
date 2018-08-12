package lightleaf.netcom.server.tools;

import entities.Constants;
import lightleaf.netcom.common.FileIO;
import lightleaf.netcom.common.Logger;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class DatabaseGenerator {

    public static void main(String[] args) {
        Logger.info("Generating player database...");
        try {
            generateUsernameAndPassword();
        } catch(final IOException e){
            Logger.error("Failed to generate player data base", e);
        }
        Logger.info("Player database generation complete!");
    }

    private static void generateUsernameAndPassword() throws IOException {
        final int numberOfSampleUsernames = 200;

        final List<String> sampleUsernames = FileIO.readCSV(FileSystems.getDefault().getPath(
                Constants.PROJECT_WORKING_DIRECTORY + "/Server/res", "sample_usernames.csv"), numberOfSampleUsernames);


        final List<String> database = new ArrayList<>(Constants.ACCOUNTS);
        for(int i = 0; i < Constants.ACCOUNTS; ++i){
            database.add(sampleUsernames.get(i % (numberOfSampleUsernames - 1)) + i + ", password" + i);
        }

        FileIO.writeCSV(FileSystems.getDefault().getPath(
                Constants.PROJECT_WORKING_DIRECTORY + "/Server/res", "player_database.csv"),
                "USERNAME, PASSWORD", database);
    }
}
