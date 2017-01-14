package be.condorcet.marra.scores;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import be.condorcet.marra.scores.RPC.AddScoreAsync;
import be.condorcet.marra.scores.RPC.TopAsync;

public class TopActivity extends AppCompatActivity {

    //Attributs

    private final int GAMES_REQUEST = 3;

    private Button btn_select;
    private Button btn_games;
    private EditText et_nameGame;

    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items;
    private String game;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        //Initialise contenu.

        list = (ListView)findViewById(R.id.topList);
        btn_games = (Button)findViewById(R.id.btn_games);
        btn_games.setOnClickListener(listener_btn_games);
        btn_select = (Button)findViewById(R.id.btn_selectGame);
        btn_select.setOnClickListener(listener_btn_select);
        et_nameGame = (EditText)findViewById(R.id.et_nameGame);

        //Ce lance si la demande a été faite par l'option "afficher jeux".
        intent = getIntent();
        if(intent.hasExtra("game"))
            et_nameGame.setText(intent.getStringExtra("game"));

        //Pour la liste dynamique.
        items = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(TopActivity.this, android.R.layout.simple_list_item_1, items);
        list.setAdapter(adapter);

    }

    //Listener

    //Lance le wizard de selection de jeu.
    private View.OnClickListener listener_btn_games = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Intent intent = new Intent(TopActivity.this, GamesWizard.class);
            startActivityForResult(intent, GAMES_REQUEST );
        }

    };

    private View.OnClickListener listener_btn_select = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            try{
                game = et_nameGame.getText().toString();
                new TopAsync(TopActivity.this).execute(game);
            }
            catch (Exception ex){
                Alert.showSimpleErrorAlert(TopActivity.this, ex.getMessage());
            }
        }

    };

    /**
     * Recovers data from GamesWizard.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == GAMES_REQUEST){
            if(resultCode == RESULT_OK){
                et_nameGame.setText(data.getStringExtra("game"));
            }
        }

    }

    public void responseAsync(ArrayList<String[]> result, int code){
        switch (code){
            case 0:

                items.clear();
                items.add("Jeu : " + game);
                for (int i=0;i< result.size() ; i++) {
                    items.add((i+1) + ") " + result.get(i)[0] + " : " + result.get(i)[1] );
                }

                adapter.notifyDataSetChanged();

                break;
            case 100:
                Alert.showSimpleAlert(TopActivity.this, getString(R.string.errorGameEmpty));
                break;
            case 500:
                Alert.showSimpleAlert(TopActivity.this, getString(R.string.errorNoPlayerFound));
                break;
            case 1000:
                Alert.showSimpleAlert(TopActivity.this, getString(R.string.errorDB));
                break;
            case -100:
                Alert.showSimpleErrorAlert(TopActivity.this, getString(R.string.errorConnection));
                break;
            default:
                Alert.showSimpleAlert(TopActivity.this, getString(R.string.unknownError) + " (code:" + code + ")");
                break;
        }
    }

}
