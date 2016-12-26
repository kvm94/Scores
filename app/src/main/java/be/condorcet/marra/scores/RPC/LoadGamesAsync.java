package be.condorcet.marra.scores.RPC;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.JsonReader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import be.condorcet.marra.scores.GamesWizard;
import be.condorcet.marra.scores.LoginActivity;

/**
 * Created by Kevin on 26-12-16.
 */

public class LoadGamesAsync extends AsyncTask<String,Void,ArrayList<String>> {

    private GamesWizard screen;
    private int code;

    public LoadGamesAsync(GamesWizard screen){
        this.screen = screen;
    }
    private ProgressDialog progDailog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDailog = new ProgressDialog(screen);
        progDailog.setMessage("Loading...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();
    }

    @Override
    protected ArrayList<String> doInBackground(String... data){
        // Exécution en arrière-plan
        ArrayList<String> response = new ArrayList<String>();

        try {
            URL url = new URL("http://www.kmarra.be/rpc/lister_jeux.php");
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setRequestProperty("charset", "utf-8");

            connection.setConnectTimeout(1000);
            connection.connect();


            if(connection.getResponseCode()  == 200){

                InputStream inputStream = connection.getInputStream();
                connection.setReadTimeout(5000);
                InputStreamReader inputStreamReader;
                inputStreamReader=new InputStreamReader(inputStream,"UTF-8");

                JsonReader json_reader = new JsonReader(inputStreamReader);
                json_reader.beginObject();
                while (json_reader.hasNext()){

                    if(json_reader.nextName().equals("code")){
                        code = json_reader.nextInt();
                    }

                    if(json_reader.nextName().equals("jeux")){
                        json_reader.beginArray();

                        while (json_reader.hasNext()) {
                            json_reader.beginObject();
                            if (json_reader.nextName().equals("nom_jeu")) {
                                response.add(json_reader.nextString());
                            }
                            json_reader.endObject();
                        }
                    }
                    json_reader.endArray();
                }
                json_reader.endObject();
                json_reader.close();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return response;
    }
    @Override
    protected void onPostExecute(ArrayList<String> result) {
        // Callback
        progDailog.dismiss();
        screen.responseAsync(result);
    }
}