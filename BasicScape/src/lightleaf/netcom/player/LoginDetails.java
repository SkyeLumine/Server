package lightleaf.netcom.player;

public class LoginDetails {

    private String username;
    private String password;

    public LoginDetails(final String username, final String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }
}
