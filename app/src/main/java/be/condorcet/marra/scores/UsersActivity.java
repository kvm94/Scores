package be.condorcet.marra.scores;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import be.condorcet.marra.scores.RPC.UserAsync;

public class UsersActivity extends AppCompatActivity {

    private LinearLayout userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        userList = (LinearLayout)findViewById(R.id.userList);

        new UserAsync(UsersActivity.this).execute();
    }

    public void responseAsync(ArrayList<String> result , int code){
        switch (code){
            case 0:
                for(int i = 0 ; i< result.size(); i++){
                    TextView tvUser = new TextView(UsersActivity.this);
                    tvUser.setText(result.get(i));

                    userList.addView(tvUser);
                }

                break;
            case 500:
                Alert.showSimpleAlert(UsersActivity.this, getString(R.string.errorNoPlayerFound));
                break;
            case 1000:
                Alert.showSimpleAlert(UsersActivity.this, getString(R.string.errorDB));
                break;
            default:
                Alert.showSimpleAlert(UsersActivity.this, getString(R.string.unknownError) + " (code:" + code + ")");
                break;
        }
    }
}
