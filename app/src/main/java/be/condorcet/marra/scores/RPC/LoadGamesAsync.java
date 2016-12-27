package be.condorcet.marra.scores.RPC;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import be.condorcet.marra.scores.GamesWizard;
import be.condorcet.marra.scores.IGames;


public class LoadGamesAsync extends AsyncTask<String,Void,ArrayList<String>> {

    //Attributs

    private IGames screen;
    private int code;

    public LoadGamesAsync(IGames screen){
        this.screen = screen;
    }
    private ProgressDialog progDailog;

    //Initialise la barre de chargement.
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDailog = new ProgressDialog(screen.getContext());
        progDailog.setMessage("Loading...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();
    }

    //Récupère les données du RPC
    @Override
    protected ArrayList<String> doInBackground(String... data){

        // Exécution en arrière-plan
        ArrayList<String> response = new ArrayList<String>();

        try {

            //Lance le script php du serveur

            URL url = new URL("http://www.kmarra.be/rpc/lister_jeux.php");
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setRequestProperty("charset", "utf-8");

            connection.setConnectTimeout(1000);
            connection.connect();


            //Récupère les données du JSON

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

    //Termine la tâche.
    @Override
    protected void onPostExecute(ArrayList<String> result) {
        // Callback
        progDailog.dismiss();
        screen.responseAsync(result, code);
    }
}