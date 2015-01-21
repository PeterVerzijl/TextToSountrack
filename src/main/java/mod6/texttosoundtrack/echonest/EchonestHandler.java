package mod6.texttosoundtrack.echonest;

import mod6.texttosoundtrack.spotify.SpotifyHandler;

public class EchonestHandler {
    private SpotifyHandler spotifyHandler;
    private Thread searchThread;
    private EchonestMood previousMood;

    public static void main(String[] args) {
        EchonestHandler echonestHandler = new EchonestHandler();
        //echonestHandler.searchSong();
        //System.out.println(echonestHandler.searchSong());
        echonestHandler.findTrack(EchonestMood.ACTION);
    }

    public EchonestHandler() {
        spotifyHandler = new SpotifyHandler();
    }

    public void findTrack(EchonestMood mood) {
        if (previousMood == mood) {
            System.out.println("Mood is the same as previous mood, not going to change song.");
            return;
        }
        if (searchThread != null && searchThread.isAlive()) {
            System.out.println("Interrupting thread!");
            searchThread.interrupt();
            try {
                searchThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        previousMood = mood;
        searchThread = new Thread(new FindTrack(spotifyHandler, mood));
        searchThread.start();
    }
}
