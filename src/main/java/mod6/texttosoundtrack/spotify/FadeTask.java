package mod6.texttosoundtrack.spotify;

import jahspotify.services.MediaPlayer;

import java.util.TimerTask;

public class FadeTask extends TimerTask {
    private MediaPlayer mediaPlayer;
    private boolean fadeIn;
    private boolean finished = false;

    public FadeTask(MediaPlayer mediaPlayer, boolean fadeIn) {
        this.mediaPlayer = mediaPlayer;
        this.fadeIn = fadeIn;
    }

    public void fadeOut() {
        mediaPlayer.setVolume(mediaPlayer.getVolume() - 1);
        if (mediaPlayer.getVolume() <= 0) {
            System.out.println("Cancelling fade out because volume is 0: " + mediaPlayer.getVolume());
            finished = true;
            cancel();
        }
    }

    public void fadeIn() {
        mediaPlayer.setVolume(mediaPlayer.getVolume() + 1);
        if (mediaPlayer.getVolume() >= 100) {
            System.out.println("Cancelling fade in because volume is 100: " + mediaPlayer.getVolume());
            finished = true;
            cancel();
        }
    }

    @Override
    public void run() {
        if (fadeIn) {
            fadeIn();
        } else {
            fadeOut();
        }
    }

    public boolean isFinished() {
        return finished;
    }
}
