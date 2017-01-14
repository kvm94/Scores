package be.condorcet.marra.scores;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import be.condorcet.marra.scores.RPC.LoadGamesAsync;

import static android.R.attr.width;
import static be.condorcet.marra.scores.R.attr.height;
import static be.condorcet.marra.scores.R.id.top;

public class GamesActivity extends AppCompatActivity implements IGames{

    private LinearLayout content_layout;
    private Button btn_back;
    private Button btn_next;
    private ArrayList<LinearLayout> contents;
    private int pos;
    private int nbrGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        contents = new ArrayList<LinearLayout>();
        content_layout = (LinearLayout)findViewById(R.id.contentLayout);

        btn_back = (Button)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(listener_btn_back);

        btn_next = (Button)findViewById(R.id.btn_next);
        btn_next.setOnClickListener(listener_btn_next);


        new LoadGamesAsync(this).execute();


    }

    private View.OnClickListener listener_btn_back = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            pos -= 5;
            int end = pos + 5;

            if(pos < 0 )
                pos = 0;
            if(end < 5 )
                end = 5;
            if(end > nbrGames)
                end = nbrGames;

            content_layout.removeAllViews();
            displayGames(pos, end);
        }

    };

    private View.OnClickListener listener_btn_next = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            pos += 5;
            int end = pos +5;

            if(pos >= nbrGames ){
                pos = pos-5;
                end = end - 5 ;
            }
            if(end > nbrGames )
                end = nbrGames;

            content_layout.removeAllViews();
            displayGames(pos, end);
        }

    };

    @Override
    public void responseAsync(ArrayList<String> result, int code){
        switch (code){
            case 0:
                for(int i = 0 ; i< result.size(); i++){
                    LinearLayout layout = new LinearLayout(GamesActivity.this);

                    final TextView tvGame = new TextView(GamesActivity.this);
                    tvGame.setText(result.get(i));

                    Button btnTop = new Button(GamesActivity.this);
                    btnTop.setText("Top 10");
                    btnTop.setOnClickListener( new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            Intent intent = new Intent(GamesActivity.this, TopActivity.class);
                            intent.putExtra("game", tvGame.getText().toString());
                            startActivity(intent);
                        }
                    });

                    layout.addView(tvGame);
                    layout.addView(btnTop);
                    contents.add(layout);
                }
                pos = 0;
                nbrGames = contents.size();

                displayGames(pos, (pos+5));

                break;
            case 500:
                Alert.showSimpleAlert(GamesActivity.this, getString(R.string.errorNoGameFound));
                break;
            case 1000:
                Alert.showSimpleAlert(GamesActivity.this, getString(R.string.errorDB));
                break;
            case -100:
                Alert.showSimpleErrorAlert(GamesActivity.this, getString(R.string.errorConnection));
                break;
            default:
                Alert.showSimpleAlert(GamesActivity.this, getString(R.string.unknownError) + " (code:" + code + ")");
                break;
        }
    }

    //Affiche les layout a partir d'un position à une autre.
    private void displayGames(int initalPos, int endPos){
        int i = initalPos;
        while(i < endPos && i< nbrGames){
            content_layout.addView(contents.get(i));
            i++;
        }
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }
}
