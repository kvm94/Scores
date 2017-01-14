package be.condorcet.marra.scores.RPC;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import be.condorcet.marra.scores.AddScoreActivity;
import be.condorcet.marra.scores.R;

public class AddScoreAsync extends AsyncTask<String,Void,Integer> {

    //Attributs

    private AddScoreActivity screen;
    private ProgressDialog progDailog;


    //Constructeur

    public AddScoreAsync(AddScoreActivity screen){

        this.screen = screen;
    }

    //Méthodes surchargées


    //Initialise la barre de chargement.
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDailog = new ProgressDialog(screen);
        progDailog.setMessage(screen.getString(R.string.loading));
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();
    }

    /**
     * Ajoute un score à la base de données.
     * @param data
     * @return Code de retour.
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

        } catch (Exception e) {
            response = -100;
        }

        return response;
    }

    //Transmet le code retour à l'activité.
    @Override
    protected void onPostExecute(Integer result) {

        progDailog.dismiss();
        screen.responseAsync(result);
    }
}