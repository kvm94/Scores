package be.condorcet.marra.scores;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import be.condorcet.marra.scores.RPC.LoginAsync;

public class LoginActivity extends AppCompatActivity {

    public final static int REGISTER_REQUEST = 1;

    private EditText et_login;
    private EditText et_passwd;
    private Button btn_register;
    private Button btn_login;

    private String login;
    private String passwd;
    private int    userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_register = (Button)findViewById(R.id.btn_Register);
        btn_register.setOnClickListener(listener_btn_register);

        btn_login = (Button)findViewById(R.id.btn_Login);
        btn_login.setOnClickListener(listener_btn_login);

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

    private View.OnClickListener listener_btn_login = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            try{
                getValues();
                String[] params = {login, passwd};
                new LoginAsync(LoginActivity.this).execute(params);

            }
            catch (Exception ex){
                Alert.showSimpleErrorAlert(LoginActivity.this, ex.getMessage());
            }
        }

    };

    private void getValues() throws Exception {
        try{
            login    = et_login.getText().toString();
            passwd   = et_passwd.getText().toString();

            if(login.equals("") || passwd.equals("")){
                throw new Exception(getString(R.string.errorMissingField));
            }
        }
        catch(Exception ex){
            throw ex;
        }
    }

    /**
     * Recovers data from RegisterActivity.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REGISTER_REQUEST){
            if(resultCode == RESULT_OK){
                login = data.getStringExtra("login");
                passwd = data.getStringExtra("passwd");
                userId = data.getIntExtra("id", -1);
                et_login.setText(login);
                et_passwd.setText(passwd);
            }
        }

    }

    public void responseAsync(Integer[] response){
        userId = response[1];
        switch(response[0]){
            case 0:
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("login", login);
                intent.putExtra("id", userId);
                startActivity(intent);
                finish();
                break;
            case 200:
                Alert.showSimpleErrorAlert(LoginActivity.this, getString(R.string.errorLoginPasswd));
                break;
            case 1000:
                Alert.showSimpleErrorAlert(LoginActivity.this, getString(R.string.errorDB));
                break;
            case -100:
                Alert.showSimpleErrorAlert(LoginActivity.this, getString(R.string.errorConnection));
                break;
            default :
                Alert.showSimpleAlert(LoginActivity.this, getString(R.string.unknownError) + " (code:" + response + ")");
                break;
        }
    }
}
