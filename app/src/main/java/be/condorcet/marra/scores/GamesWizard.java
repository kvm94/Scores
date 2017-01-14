package be.condorcet.marra.scores;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import be.condorcet.marra.scores.RPC.AddScoreAsync;
import be.condorcet.marra.scores.RPC.LoadGamesAsync;

public class GamesWizard extends AppCompatActivity implements IGames{

    private Button btn_select;
    private RadioGroup rdG;
    private Intent intent;
    private Intent intent_return;
    private String selectedRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_wizard);

        intent = getIntent();
        intent_return = new Intent();

        btn_select = (Button)findViewById(R.id.btn_select);
        btn_select.setOnClickListener(listener_btn_select);

        rdG = (RadioGroup)findViewById(R.id.rdG);

        new LoadGamesAsync(this).execute();
    }

    @Override
    public void responseAsync(ArrayList<String> result, int code){
        switch (code){
            case 0:
                for(int i = 0 ; i<result.size();i++){
                    RadioButton  r = new RadioButton(this);
                    r.setText(result.get(i));
                    rdG.addView(r);
                }

                break;
            case 500:
                Alert.showSimpleAlert(GamesWizard.this, getString(R.string.errorNoGameFound));
                break;
            case 1000:
                Alert.showSimpleAlert(GamesWizard.this, getString(R.string.errorDB));
                break;
            case -100:
                Alert.showSimpleErrorAlert(GamesWizard.this, getString(R.string.errorConnection));
                break;
            default:
                Alert.showSimpleAlert(GamesWizard.this, getString(R.string.unknownError) + " (code:" + code + ")");
                break;
        }

    }

    @Override
    public Context getContext(){
        return getApplicationContext();
    }

    private View.OnClickListener listener_btn_select = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            RadioButton radio = (RadioButton)findViewById(rdG.getCheckedRadioButtonId());
            String game = radio.getText().toString();
            intent_return.putExtra("game", game);
            setResult(RESULT_OK, intent_return);
            finish();
        }

    };

}
