package be.condorcet.marra.scores.RPC;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import be.condorcet.marra.scores.R;
import be.condorcet.marra.scores.TopActivity;


public class TopAsync extends AsyncTask<String, Void, ArrayList<String[]>> {

//Attributs

    private TopActivity screen;
    private int code;

    public TopAsync(TopActivity screen) {
        this.screen = screen;
    }

    private ProgressDialog progDailog;

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

    //Récupère les données du RPC
    @Override
    protected ArrayList<String[]> doInBackground(String... data) {

        // Exécution en arrière-plan
        ArrayList<String[]> responses = new ArrayList<String[]>();
        String[] response;


        try {

            String game = data[0];

            //Convertit les espace dans la requête GET;
            game = game.replace(" ", "%20");

            //Lance le script php du serveur

            URL url = new URL("http://www.kmarra.be/rpc/afficher_top.php?jeu=" + game);
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setRequestProperty("charset", "utf-8");

            connection.setConnectTimeout(1000);
            connection.connect();


            //Récupère les données du JSON

            if (connection.getResponseCode() == 200) {

                InputStream inputStream = connection.getInputStream();
                connection.setReadTimeout(5000);
                InputStreamReader inputStreamReader;
                inputStreamReader = new InputStreamReader(inputStream, "UTF-8");

                JsonReader json_reader = new JsonReader(inputStreamReader);
                json_reader.beginObject();
                while (json_reader.hasNext()) {

                    if (json_reader.nextName().equals("code")) {
                        code = json_reader.nextInt();
                    }

                    if (json_reader.nextName().equals("joueurs")) {
                        json_reader.beginArray();

                        while (json_reader.hasNext()) {
                            json_reader.beginObject();
                            response = new String[2];

                            if (json_reader.nextName().equals("pseudo")) {
                                response[0] = json_reader.nextString();
                            }
                            if (json_reader.nextName().equals("score")) {
                                response[1] = json_reader.nextString();
                            }
                            responses.add(response);
                            json_reader.endObject();
                        }
                    }
                    json_reader.endArray();
                }
                json_reader.endObject();
                json_reader.close();
            }

        } catch (Exception e) {
            code =-100;
        }

        return responses;
    }

    //Termine la tâche.
    @Override
    protected void onPostExecute(ArrayList<String[]> result) {
        // Callback
        progDailog.dismiss();
        screen.responseAsync(result, code);
    }
}
