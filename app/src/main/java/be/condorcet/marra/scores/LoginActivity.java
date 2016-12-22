package be.condorcet.marra.scores;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    public final static int REGISTER_REQUEST = 1;
    private EditText et_login;
    private EditText et_passwd;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_register = (Button)findViewById(R.id.btn_Register);
        btn_register.setOnClickListener(listener_btn_register);

        et_login = (EditText)findViewById(R.id.et_Login);
        et_passwd = (EditText)findViewById(R.id.et_Passwd);

    }

    private View.OnClickListener listener_btn_register = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivityForResult(intent, REGISTER_REQUEST );
        }

    };

    /**
     * Recovers data from RegisterActivity.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REGISTER_REQUEST){
            if(resultCode == RESULT_OK){
                Alert.showSimpleAlert(LoginActivity.this, getString(R.string.registered));
                String login = data.getStringExtra("login");
                String passwd = data.getStringExtra("passwd");
                et_login.setText(login);
                et_passwd.setText(passwd);
            }
        }

    }
}
