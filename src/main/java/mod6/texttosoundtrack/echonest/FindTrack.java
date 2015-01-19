package mod6.texttosoundtrack.echonest;

import mod6.texttosoundtrack.spotify.SpotifyHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class FindTrack implements Runnable {
    private SpotifyHandler spotifyHandler;
    private EchonestMood mood;

    public FindTrack(SpotifyHandler spotifyHandler, EchonestMood mood) {
        this.spotifyHandler = spotifyHandler;
        this.mood = mood;
    }

    @Override
    public void run() {
        if (mood != null) {
            try {
                URL url = new URL("http://developer.echonest.com/api/v4/song/search?api_key=CGV11LMHK97XRE10T&format=json&" +
                        "mood=" + mood.getEchonestMood() + "&min_instrumentalness=0.95&bucket=id:spotify&bucket=tracks");
                URLConnection urlConnection = url.openConnection();
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);
                //new JSONObject(new JSONParser().parse(responseStrBuilder.toString()));
                JSONObject json = (JSONObject) JSONValue.parse(responseStrBuilder.toString());
                JSONArray songArray = (JSONArray) ((JSONObject) json.get("response")).get("songs");
                for (int i = 0; i < songArray.size(); i++) {
                    JSONObject song = (JSONObject) songArray.get(i);
                    JSONArray tracks = (JSONArray) song.get("tracks");
                    if (!tracks.isEmpty()) {
                        String trackId = (String) ((JSONObject) tracks.get(0)).get("foreign_id");
                        //Try to play track
                        if (spotifyHandler.playTrack(trackId)) {
                            break;
                        }
                    }
                }
                streamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
