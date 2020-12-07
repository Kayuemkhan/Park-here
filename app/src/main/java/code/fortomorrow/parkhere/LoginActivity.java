package code.fortomorrow.parkhere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.sign_up)
    TextView signUp;
    @BindView(R.id.emailET)
    TextInputEditText emailET;
    @BindView(R.id.passwordET)
    TextInputEditText passwordET;
    private ProgressDialog loadingBar1;
    private FirebaseAuth mAuth;
    @BindView(R.id.login_button)
    Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loadingBar1 = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

    }
    @OnClick(R.id.login_button)
    public void checkFields() {
        String email = emailET.getText().toString();
        String pass = passwordET.getText().toString();
        if(TextUtils.isEmpty(email)){
            emailET.setError("Phone Field can't be Blank");
            return;
        }
        else if(TextUtils.isEmpty(pass)){
            passwordET.setError("Password Field can't be Blank");
            return;
        }
        else {
            loadingBar1.setTitle("Login Account");
            loadingBar1.setMessage("Please Wait, While we are checking the credentials");
            loadingBar1.setCanceledOnTouchOutside(false);
            loadingBar1.show();
            AllowAccssAccount(email,pass);
    }
    }

    private void AllowAccssAccount(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser user = mAuth.getCurrentUser();

                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                    loadingBar1.dismiss();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Email & Password is not Correct",Toast.LENGTH_LONG).show();
                    loadingBar1.dismiss();
                }
            }
        });
    }

    @OnClick(R.id.sign_up)
    public void signup(){
        startActivity(new Intent(getApplicationContext(),SignupActivity.class));
    }

}