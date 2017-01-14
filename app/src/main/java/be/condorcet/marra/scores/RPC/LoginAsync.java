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

import be.condorcet.marra.scores.Alert;
import be.condorcet.marra.scores.LoginActivity;
import be.condorcet.marra.scores.R;


public class LoginAsync  extends AsyncTask<String,Void,Integer[]> {

    //Attributs

    private LoginActivity screen;
    private ProgressDialog progDailog;

    //Constructeur

    public LoginAsync(LoginActivity screen){
        this.screen = screen;
    }

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

    //Vérifie si l'utilisateur peut se connecter et récupère son ID
    @Override
    protected Integer[] doInBackground(String... data){
        // Exécution en arrière-plan
        Integer[] response = new Integer[2];
        response[0] = -1;
        response[1] = -1;
        try {
            String pseudo = data[0];
            String mdp = data[1];

            URL url = new URL("http://www.kmarra.be/rpc/se_connecter.php");
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String parametres_post="pseudo=" + pseudo + "&mdp=" + mdp;
            writer.write(parametres_post);
            writer.flush();
            writer.close();
            os.close();

            connection.setConnectTimeout(5000);
            connection.connect();


            //Récupère les infos à partir d'un JSON
            if(connection.getResponseCode()  == 200){
                InputStream inputStream = connection.getInputStream();
                connection.setReadTimeout(5000);
                InputStreamReader inputStreamReader;
                inputStreamReader=new InputStreamReader(inputStream,"UTF-8");

                JsonReader json_reader = new JsonReader(inputStreamReader);
                json_reader.beginObject();
                while (json_reader.hasNext()){
                    String name = json_reader.nextName();
                    if(name.equals("code")){
                        response[0] = json_reader.nextInt();
                    }
                    if(json_reader.nextName().equals("id")){
                        response[1] = json_reader.nextInt();

                    }

                }
                json_reader.endObject();
            }
            else
                response[0] = connection.getResponseCode();

        } catch (Exception e) {
            response[0] = -100;
        }

        return response;
    }

    //Renvoit le code erreur et l'ID.
    @Override
    protected void onPostExecute(Integer[] result) {
        // Callback
        progDailog.dismiss();
        screen.responseAsync(result);
    }
}