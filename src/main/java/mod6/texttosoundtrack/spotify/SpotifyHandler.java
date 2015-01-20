package mod6.texttosoundtrack.spotify;

import jahspotify.JahSpotify;
import jahspotify.media.Link;
import jahspotify.media.Track;
import jahspotify.services.JahSpotifyService;
import jahspotify.services.MediaHelper;
import jahspotify.services.MediaPlayer;
import mod6.texttosoundtrack.CustomMediaHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class SpotifyHandler {
    private JahSpotify jahSpotify;
    private SpotifyPlaybackListener playbackListener;
    private Fade fade;
    private String previousTrack;

    //------------ Hack for predefined tracks --------------//
    private String[] predefinedTracks = {"spotify:track:6JEK0CvvjDjjMUBFoXShNZ"};
    private int currentSong = 0;
    //---------- End hack for predefined tracks ------------//


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

        fade = new Fade();
        //playTrack("spotify:track:6JEK0CvvjDjjMUBFoXShNZ");
    }

    private void getLogin() {

    }

    /**
     * Plays a track, track does not start playing immediately.
     *
     * @param trackId Spotify track id
     */
    public boolean playTrack(String trackId) throws InterruptedException {
        //trackId = predefinedTracks[currentSong];
        //currentSong++;

        if (previousTrack != null) {
            if (previousTrack.equals(trackId)){
                System.out.println("That song was already playing, not changing song");
                return true;
            }
        }
        //TODO instantly return true if song is already playing

        //Fade out previous track
        System.out.println("Fading out");

        // Get a track.
        Track track = jahSpotify.readTrack(Link.create(trackId));
        // Wait for 10 seconds or until the track is loaded.
        CustomMediaHelper.waitFor(track, 10);

        //------------ Hack for predefined tracks --------------//
        if (track.getTitle().contains("Yorkshire") || track.getTitle().contains("Sunset Finale")) {
            System.out.println("Ignored non existant action songs because of test");
            return false;
        }
        //---------- End hack for predefined tracks ------------//

        //This probably shouldn't be done using mediahelper (wait until timer completes)
        FadeTask fadeTask = fade.fadeOut();
        CustomMediaHelper.waitFor(fadeTask, 10);

        System.out.println("Done fading out: " + MediaPlayer.getInstance().getVolume());

        // If the track is loaded, play it.
        if (track.isLoaded()) {
            if (track.getTitle() != null && !track.getTitle().isEmpty()) {
                playbackListener.setLoaded(false);

                jahSpotify.play(track.getId());
                // Wait until track starts playing to be sure it can actually be played.
                CustomMediaHelper.waitFor(playbackListener, 2);
                if (playbackListener.isLoaded()) {
                    System.out.println("Playing track: " + track + " with id " + track.getId());
                    //Fade in new track
                    fade.fadeIn();
                    previousTrack = trackId;
                    return true;
                } else {
                    System.out.println("Could not play that track");
                    previousTrack = null;
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
}