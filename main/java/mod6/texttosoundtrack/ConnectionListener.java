package mod6.texttosoundtrack;

import jahspotify.AbstractConnectionListener;
import jahspotify.media.Loadable;
import jahspotify.services.JahSpotifyService;

public class ConnectionListener extends AbstractConnectionListener implements Loadable {
    private boolean loaded;
    private String username;
    private String password;

    public ConnectionListener(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void initialized(final boolean initialized) {
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
