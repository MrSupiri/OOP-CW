package Views;

public class ConsoleApp {
    private static String sessionToken;

    public static void main(String[] args) {
        // TODO: Initiate the Manager CLI
    }

    private boolean login(String username, String password){
        // TODO: Match the username and password with database, update the sessionToken and return true if authorized
        return false;
    }

    private void launchGUI(){
        // TODO: Start the Electron Binary
    }

    private void logOut(){
        sessionToken = null;
    }
}
