package be.condorcet.marra.scores;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import be.condorcet.marra.scores.RPC.AddScoreAsync;

public class AddScoreActivity extends AppCompatActivity {

    //Attributs

    private final int GAMES_REQUEST = 2;

    private EditText et_nameGame;
    private EditText et_score;
    private Button   btn_games;
    private Button btn_addScore;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_score);

        //Crée l'intent
        Intent intent = getIntent();
        userId = intent.getIntExtra("id", -1);

        //Initialise le contenues de l'activité.
        et_nameGame = (EditText)findViewById(R.id.et_jeux);
        et_score = (EditText)findViewById(R.id.et_score);
        btn_addScore = (Button)findViewById(R.id.btn_addScore);
        btn_addScore.setOnClickListener(listener_btn_addScore);
        btn_games = (Button)findViewById(R.id.btn_games);
        btn_games.setOnClickListener(listener_btn_games);
    }

    private View.OnClickListener listener_btn_addScore = new View.OnClickListener(){
        @Override
        public void onClick(View v){

            try{
                String game = et_nameGame.getText().toString();
                String score = et_score.getText().toString();
                String idS = Integer.toString(userId);

                String[] params = {game, score, idS};
                new AddScoreAsync(AddScoreActivity.this).execute(params);
            }
            catch (Exception ex){
                Alert.showSimpleErrorAlert(AddScoreActivity.this, ex.getMessage());
            }

        }

    };

    private View.OnClickListener listener_btn_games = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Intent intent = new Intent(AddScoreActivity.this, GamesWizard.class);
            startActivityForResult(intent, GAMES_REQUEST );
        }

    };
    /**
     *
     * *Recovers data from GamesWizard.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == GAMES_REQUEST){
            if(resultCode == RESULT_OK){
                et_nameGame.setText(data.getStringExtra("game"));
            }
        }

    }


    public void responseAsync(Integer code){
        switch (code){
            case 0:
                Alert.showConfirmationMessage(AddScoreActivity.this, getString(R.string.scoreAdded), this);
                break;
            case 100:
                Alert.showSimpleAlert(AddScoreActivity.this, getString(R.string.errorNoScore));
                break;
            case 110:
                Alert.showSimpleAlert(AddScoreActivity.this, getString(R.string.errorGameEmpty));
                break;
            case 1000:
                Alert.showSimpleAlert(AddScoreActivity.this, getString(R.string.errorDB));
                break;
            default:
                Alert.showSimpleAlert(AddScoreActivity.this, getString(R.string.unknownError) + " (code:" + code + ")");
                break;
        }
    }
}
