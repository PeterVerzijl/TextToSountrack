package mod6.texttosoundtrack.echonest;

import mod6.texttosoundtrack.SpotifyHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class EchonestHandler {
    private SpotifyHandler spotifyHandler;

    public static void main(String[] args) {
        EchonestHandler echonestHandler = new EchonestHandler();
        //echonestHandler.searchSong();
        //System.out.println(echonestHandler.searchSong());
        echonestHandler.findTrack();
    }

    public EchonestHandler() {
        spotifyHandler = new SpotifyHandler();
    }

    public boolean findTrack() {
        try {
            URL url = new URL(
                    "http://developer.echonest.com/api/v4/song/search?api_key=CGV11LMHK97XRE10T&format=json&" +
                            "mood=happy&min_instrumentalness=0.95&bucket=id:spotify&bucket=tracks");
            URLConnection urlConnection = url.openConnection();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            //new JSONObject(new JSONParser().parse(responseStrBuilder.toString()));
            JSONObject json = (JSONObject) JSONValue.parse(responseStrBuilder.toString());
            System.out.println("Input json: " + json);
            JSONArray songArray = (JSONArray) ((JSONObject) json.get("response")).get("songs");
            for (int i = 0; i < songArray.size(); i++) {
                JSONObject song = (JSONObject) songArray.get(i);
                String trackId = (String) ((JSONObject) ((JSONArray) song.get("tracks")).get(0)).get("foreign_id");
                System.out.println("Track id: " + trackId);
                //Try to play track
                if (spotifyHandler.playTrack(trackId)) {
                    return true;
                }
            }
            streamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
