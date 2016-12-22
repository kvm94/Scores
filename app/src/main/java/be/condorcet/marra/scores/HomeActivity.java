package be.condorcet.marra.scores;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    String login;
    String hello;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();

        hello = getString(R.string.hello) + " " + intent.getStringExtra("login") + ". " + getString(R.string.helloQuestion);

        tv = (TextView)findViewById(R.id.textView);
        tv.setText(hello);
    }

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
