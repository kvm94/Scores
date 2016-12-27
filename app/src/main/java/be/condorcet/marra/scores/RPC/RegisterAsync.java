package be.condorcet.marra.scores.RPC;

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

import be.condorcet.marra.scores.RegisterActivity;


public class RegisterAsync extends AsyncTask<String,Void,Integer[]> {

    //Attributs

    private RegisterActivity screen;

    //Constructeur
    public RegisterAsync(RegisterActivity screen){
        this.screen = screen;
    }

    @Override
    protected Integer[] doInBackground(String... data) {
        // Exécution en arrière-plan
        Integer[] response = new Integer[2];
        response[0] = -1;
        response[1] = -1;
        try {
            String pseudo = data[0];
            String mdp = data[1];

            URL url = new URL("http://www.kmarra.be/rpc/creer_compte.php");
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

            connection.setConnectTimeout(10000);
            connection.connect();
            response[0] = connection.getResponseCode();


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
                    else if(response[0] == 0){
                        if(json_reader.nextName().equals("id")){
                            response[1] = json_reader.nextInt();
                        }
                    }

                }
                json_reader.endObject();
            }
            else
                response[0] = connection.getResponseCode();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
    @Override
    protected void onPostExecute(Integer[] result) {
        // Callback
        screen.responseAsync(result);
    }
}
