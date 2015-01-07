package mod6.texttosoundtrack;

import jahspotify.JahSpotify;
import jahspotify.media.Link;
import jahspotify.media.Track;
import jahspotify.services.JahSpotifyService;
import jahspotify.services.MediaHelper;

import java.io.File;

public class SpotifyHandler {
    private JahSpotify jahSpotify;

    public static void main(final String[] args) {
        new SpotifyHandler();
    }

    public SpotifyHandler() {
        // Determine the tempfolder and make sure it exists.
        //File temp = new File(new File(Main.class.getResource("TextToSoundtrack.class").getFile()).getParentFile(), "temp");
        File temp = new File("C:\\Development", "temp");
        temp.mkdirs();

        // Start JahSpotify
        JahSpotifyService.initialize(temp);

        jahSpotify = JahSpotifyService.getInstance().getJahSpotify();
        ConnectionListener connectionListener = new ConnectionListener();
        jahSpotify.addConnectionListener(connectionListener);
        MediaHelper.waitFor(connectionListener, 10);
        if (!jahSpotify.isLoggedIn()) {
            System.out.println("Didn't login in time");
            System.exit(1);
        }

        playTrack("6JEK0CvvjDjjMUBFoXShNZ");
    }

    public void playTrack(String trackId) {
        // Get a track.
        Track t = JahSpotifyService.getInstance().getJahSpotify().readTrack(Link.create("spotify:track:" + trackId));
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