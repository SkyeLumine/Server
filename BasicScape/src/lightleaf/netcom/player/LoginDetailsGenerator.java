package lightleaf.netcom.player;

import entities.Constants;
import lightleaf.netcom.common.FileIO;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LoginDetailsGenerator {

    private List<String> loginDetailsList;

    public LoginDetailsGenerator() throws IOException {
        final String workingDirectory = System.getProperty("user.dir") + "/BasicScape/res";
        loginDetailsList = FileIO.readCSV(
                FileSystems.getDefault().getPath(workingDirectory, "playerlogindetails.csv"), Constants.ACCOUNTS);
    }

    public LoginDetails next(){
        final String[] loginDetails = this.loginDetailsList.remove(ThreadLocalRandom.current().nextInt(loginDetailsList.size())).split(",");
        return new LoginDetails(loginDetails[0].trim(), loginDetails[1].trim());
    }
}

