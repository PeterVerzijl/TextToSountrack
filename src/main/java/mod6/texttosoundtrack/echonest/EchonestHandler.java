package mod6.texttosoundtrack.echonest;

import mod6.texttosoundtrack.spotify.SpotifyHandler;

public class EchonestHandler {
    private SpotifyHandler spotifyHandler;
    private Thread searchThread;

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
        if (searchThread != null && searchThread.isAlive()) {
            System.out.println("Interrupting thread!");
            searchThread.interrupt();
            try {
                searchThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        searchThread = new Thread(new FindTrack(spotifyHandler, mood));
        searchThread.start();
    }
}
