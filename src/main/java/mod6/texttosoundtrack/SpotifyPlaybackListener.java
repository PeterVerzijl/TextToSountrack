package mod6.texttosoundtrack;

import jahspotify.AbstractPlaybackListener;
import jahspotify.JahSpotify;
import jahspotify.media.Link;
import jahspotify.media.Loadable;

public class SpotifyPlaybackListener extends AbstractPlaybackListener implements Loadable {
    private boolean loaded;
    private JahSpotify jahSpotify;

    public SpotifyPlaybackListener(JahSpotify jahSpotify) {
        this.jahSpotify = jahSpotify;
    }


    @Override
    public void trackStarted(Link link) {
        loaded = false;
    }

    @Override
    public int addToBuffer(byte[] buffer) {
        loaded = true;
        return 0;
    }

    @Override
    public void trackEnded(Link link, boolean forcedEnd) {
        System.out.println("Track ended! " + link.getType() + " , " + forcedEnd);
    }

    public boolean isPlaying() {
        return jahSpotify.getStatus() == JahSpotify.PlayerStatus.PLAYING;
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
