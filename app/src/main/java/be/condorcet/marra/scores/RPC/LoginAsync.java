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
import java.util.Scanner;

import be.condorcet.marra.scores.LoginActivity;
import be.condorcet.marra.scores.RegisterActivity;

/**
 * Created by Kevin on 24-12-16.
 */

public class LoginAsync  extends AsyncTask<String,Void,Integer> {
    private LoginActivity screen;

    public LoginAsync(LoginActivity screen){
        this.screen = screen;
    }

    @Override
    protected Integer doInBackground(String... data) {
        // Exécution en arrière-plan
        Integer response=-1;
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