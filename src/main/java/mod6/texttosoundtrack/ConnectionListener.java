package mod6.texttosoundtrack;

import jahspotify.AbstractConnectionListener;
import jahspotify.media.Loadable;
import jahspotify.services.JahSpotifyService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConnectionListener extends AbstractConnectionListener implements Loadable {
    private boolean loaded;

    @Override
    public void initialized(final boolean initialized) {
        // Ask for the username and password.
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String username = null, password = null;
        try {
            System.out.print("Username: ");
            username = in.readLine();
            System.out.print("Password: ");
            password = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // When JahSpotify is initialized, we can attempt to login.
        if (initialized) {
            JahSpotifyService.getInstance().getJahSpotify().login(username, password, null, false);
        }
    }

    @Override
    public void loggedIn(final boolean success) {
        if (!success) {
            System.err.println("Unable to login.");
            System.exit(1);
        }
        System.out.println("Logged in succesfully.");

        setLoaded(true);
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
