package mod6.texttosoundtrack.echonest;

import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Song;
import com.echonest.api.v4.SongParams;

import java.util.List;

public class EchonestHandler {
    private EchoNestAPI echoNest;

    public static void main(String[] args) throws EchoNestException {
        EchonestHandler echonestHandler = new EchonestHandler();
        //SpotifyHandler spotifyHandler = new SpotifyHandler();
        String trackId = echonestHandler.searchSong();
        System.out.println("Chosen track: " + trackId);
        //spotifyHandler.playTrack(trackId);
        //System.out.println(echonestHandler.searchSong());
    }

    public EchonestHandler(){
        echoNest = new EchoNestAPI("CGV11LMHK97XRE10T");
    }

    public String searchSong() {
        SongParams params = new SongParams();
        params.add("mood", "happy");
        params.set("max_speechiness", "0.5");
        params.add("bucket", "id:spotify");
        params.add("bucket", "tracks");
        try {
            List<Song> songs = echoNest.searchSongs(params);
            System.out.println("Songs found: " + songs);

            for (Song song : songs) {
                return song.getID();
            }
        } catch (EchoNestException e) {
            e.printStackTrace();
        }
        return null;
    }
}
