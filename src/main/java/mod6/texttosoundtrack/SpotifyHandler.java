package mod6.texttosoundtrack;

import jahspotify.JahSpotify;
import jahspotify.media.Link;
import jahspotify.media.Track;
import jahspotify.services.JahSpotifyService;
import jahspotify.services.MediaHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class SpotifyHandler {
    private JahSpotify jahSpotify;

    public static void main(final String[] args) {
        new SpotifyHandler();
    }

    public SpotifyHandler() {
        System.out.println("Initializing spotify handler...");
        // Determine the tempfolder and make sure it exists.
        File temp = new File(System.getProperty("java.io.tmpdir"));
        temp.mkdirs();

        // Start JahSpotify
        JahSpotifyService.initialize(temp);

        jahSpotify = JahSpotifyService.getInstance().getJahSpotify();

        // Ask for the username and password.
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String username = "", password = "";
        try {
            if (username == null || username.isEmpty()) {
                System.out.print("Username: ");
                username = in.readLine();
            }
            if (password == null || password.isEmpty()) {
                System.out.print("Password: ");
                password = in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        ConnectionListener connectionListener = new ConnectionListener(username, password);
        jahSpotify.addConnectionListener(connectionListener);
        MediaHelper.waitFor(connectionListener, 10);
        if (!jahSpotify.isLoggedIn()) {
            System.out.println("Didn't login in time");
            System.exit(1);
        }

        //playTrack("spotify:track:6JEK0CvvjDjjMUBFoXShNZ");
    }

    private void getLogin() {

    }

    public void playTrack(String trackId) {
        // Get a track.
        Track t = JahSpotifyService.getInstance().getJahSpotify().readTrack(Link.create(trackId));
        // Wait for 10 seconds or until the track is loaded.
        MediaHelper.waitFor(t, 10);
        // If the track is loaded, play it.
        if (t.isLoaded()) {
            jahSpotify.play(t.getId());
            System.out.println("Playing track: " + t.getTitle());
        } else {
            System.out.println("Failed to load track");
        }
    }

    public void pauseTrack() {
        jahSpotify.pause();
    }
}