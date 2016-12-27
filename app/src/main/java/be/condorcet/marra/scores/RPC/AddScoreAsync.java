package be.condorcet.marra.scores.RPC;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import be.condorcet.marra.scores.AddScoreActivity;

public class AddScoreAsync extends AsyncTask<String,Void,Integer> {

    //Attributs

    private AddScoreActivity screen;

    //Constructeur

    public AddScoreAsync(AddScoreActivity screen){

        this.screen = screen;
    }

    //Méthodes surchargées

    /**
     * Exécute la tâche en arrière plan.
     * @param data
     * @return Integer
     */
    @Override
    protected Integer doInBackground(String... data) {

        Integer response=-1;

        try {
            //Récupère les données.

            String game = data[0];
            int score = Integer.parseInt(data[1]);
            int id = Integer.parseInt(data[2]);

            //Convertit les espace dans la requête GET;
            game = game.replace(" ", "%20");

            //COnnexion au script php et envoie les paramètre en GET

            URL url = new URL("http://www.kmarra.be/rpc/ajouter_score.php?jeu=" + game + "&score=" + score + "&id=" + id);
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setRequestProperty("charset", "utf-8");

            connection.setConnectTimeout(10000);
            connection.connect();
            response = connection.getResponseCode();


            //Récupère les données renvoyées par le script php

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

    //Exécute après le doInBackground
    @Override
    protected void onPostExecute(Integer result) {
        // Callback
        screen.responseAsync(result);
    }
}