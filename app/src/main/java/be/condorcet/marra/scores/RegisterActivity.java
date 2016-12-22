package be.condorcet.marra.scores;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    //Attributes

    private Button btn_save;
    private EditText et_Login;
    private EditText et_Passwd;
    private EditText et_Confirm;
    private Intent intent;
    private Intent intent_return;
    private String login;
    private String passwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initComponents();
        initIntent();

        btn_save.setOnClickListener(listener_btn_save);

    }

    private void initComponents(){
        et_Login    =   (EditText)findViewById(R.id.et_Login);
        et_Passwd   =   (EditText)findViewById(R.id.et_Passwd);
        et_Confirm  =   (EditText)findViewById(R.id.et_Confirm);
        btn_save    =   (Button)findViewById(R.id.btn_Save);
    }

    private void initIntent(){
        intent = getIntent();
        intent_return = new Intent();
    }

    private void getValue() throws Exception {
        try{
            String confirm  = et_Confirm.getText().toString();
            login    = et_Login.getText().toString();
            passwd   = et_Passwd.getText().toString();

            if(!login.equals("") && !passwd.equals("") && !confirm.equals("")){
                if(passwd.equals(confirm)){
                    intent_return.putExtra("login", login);
                    intent_return.putExtra("passwd", passwd);
                }
                else{
                    throw new Exception(getString(R.string.errorConfirmPasswd));
                }
            }
            else{
                throw new Exception(getString(R.string.errorMissingField));
            }

        }
        catch(Exception ex){
            throw ex;
        }
    }

    private View.OnClickListener listener_btn_save = new android.view.View.OnClickListener(){
        @Override
        public void onClick(View v){
            try{
                getValue();
                /**
                 * TODO Register into Database.
                 */



                setResult(RESULT_OK, intent_return);
                finish();
            }
            catch (Exception ex){
                Alert.showSimpleErrorAlert(RegisterActivity.this, ex.getMessage());
            }

        }
    };
}
