package be.condorcet.marra.scores;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    String login;
    String hello;
    TextView tv;

    Button btn_add;
    Button btn_top;
    Button btn_game;
    Button btn_users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();

        hello = getString(R.string.hello) + " " + intent.getStringExtra("login") + ". " + getString(R.string.helloQuestion);

        tv = (TextView)findViewById(R.id.textView);
        tv.setText(hello);

        btn_add = (Button)findViewById(R.id.btn_add);
        btn_add.setOnClickListener(listener_btn_add);

        btn_top = (Button)findViewById(R.id.btn_displayTop);
        btn_top.setOnClickListener(listener_btn_top);

        btn_game = (Button)findViewById(R.id.btn_displayGames);
        btn_game.setOnClickListener(listener_btn_game);

        btn_users = (Button)findViewById(R.id.btn_users);
        btn_users.setOnClickListener(listener_btn_users);


    }

    private View.OnClickListener listener_btn_add = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Intent intent = new Intent(HomeActivity.this, AddScoreActivity.class);
            startActivity(intent);
        }

    };
    private View.OnClickListener listener_btn_top = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Intent intent = new Intent(HomeActivity.this, TopActivity.class);
            startActivity(intent);
        }

    };
    private View.OnClickListener listener_btn_game = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Intent intent = new Intent(HomeActivity.this, GamesActivity.class);
            startActivity(intent);
        }

    };
    private View.OnClickListener listener_btn_users = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Intent intent = new Intent(HomeActivity.this, UsersActivity.class);
            startActivity(intent);
        }

    };


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(HomeActivity.this)
                .setMessage(getString(R.string.closeApp))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onDestroy();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }
}
