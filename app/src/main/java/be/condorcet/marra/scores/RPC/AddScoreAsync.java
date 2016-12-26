package be.condorcet.marra.scores.RPC;

import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import be.condorcet.marra.scores.AddScoreActivity;

/**
 * Created by Kevin on 26-12-16.
 */

public class AddScoreAsync extends AsyncTask<String,Void,Integer> {
    private AddScoreActivity screen;

    public AddScoreAsync(AddScoreActivity screen){

        this.screen = screen;
    }

    @Override
    protected Integer doInBackground(String... data) {
        // Exécution en arrière-plan
        Integer response=-1;
        try {
            String game = data[0];
            int score = Integer.parseInt(data[1]);
            int id = Integer.parseInt(data[2]);

            //Convertit les espace dans la requête GET;
            game = game.replace(" ", "%20");

            URL url = new URL("http://www.kmarra.be/rpc/ajouter_score.php?jeu=" + game + "&score=" + score + "&id=" + id);
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setRequestProperty("charset", "utf-8");

            connection.setConnectTimeout(10000);
            connection.connect();
            response = connection.getResponseCode();


            if(connection.getResponseCode()  == 200){
                InputStream inputStream = connection.getInputStream();
                connection.setReadTimeout(5000);
                InputStreamReader inputStreamReader;
                inputStreamReader=new InputStreamReader(inputStream,"UTF-8");

                Scanner scanner = new Scanner(inputStreamReader);

                response = scanner.nextInt();
            }
            else
                response = connection.getResponseCode();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
    @Override
    protected void onPostExecute(Integer result) {
        // Callback
        screen.responseAsync(result);
    }
}