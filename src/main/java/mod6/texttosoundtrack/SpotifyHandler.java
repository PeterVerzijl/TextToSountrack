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
    private SpotifyPlaybackListener playbackListener;

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

        playbackListener = new SpotifyPlaybackListener(jahSpotify);
        jahSpotify.addPlaybackListener(playbackListener);

        //playTrack("spotify:track:6JEK0CvvjDjjMUBFoXShNZ");
    }

    private void getLogin() {

    }

    /**
     * Plays a track.
     * This method may take up to 20 seconds to complete.
     * @param trackId Spotify track id
     * @return success Returns false if track could not be loaded (for example when track is not on spotify)
     */
    public boolean playTrack(String trackId) {
        //trackId = "spotify:track:6JEK0CvvjDjjMUBFoXShNZ";

        Link link = Link.create(trackId);

        // Get a track.
        Track track = jahSpotify.readTrack(Link.create(trackId));
        // Wait for 10 seconds or until the track is loaded.
        MediaHelper.waitFor(track, 10);
        // If the track is loaded, play it.
        if (track.isLoaded()) {
            if (track.getTitle() != null && !track.getTitle().isEmpty()) {
                playbackListener.setLoaded(false);

                jahSpotify.play(track.getId());
                // Wait until track starts playing to be sure it can actually be played.
                MediaHelper.waitFor(playbackListener, 2);
                if (playbackListener.isLoaded()) {
                    System.out.println("Playing track: " + track + " with id " + track.getId());
                    return true;
                } else {
                    System.out.println("Could not play that track");
                }
            } else {
                System.out.println("Track is not on spotify");
            }
        } else {
            System.out.println("Failed to load track");
        }
        return false;
    }

    /**
     * Pause playing track.
     * Does nothing if no track is playing.
     */
    public void pauseTrack() {
        jahSpotify.pause();
    }

    /*public boolean trackExists(String trackId) {
        return trackExists(Link.create(trackId));
    }

    public boolean trackExists(Link link) {
        Track track = jahSpotify.readTrack(link);
        MediaHelper.waitFor(track, 5);
        return track.getTitle() != null && !track.getTitle().isEmpty();
    }*/
}