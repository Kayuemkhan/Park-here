package code.fortomorrow.parkhere;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import code.fortomorrow.parkhere.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private ProgressDialog loadingBar1;
    private FirebaseAuth mAuth;

    private ActivityLoginBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);
        loadingBar1 = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        binding.loginButton.setOnClickListener(v -> checkFields());
        binding.signUp.setOnClickListener(v -> signup());
        binding.backfromlogin.setOnClickListener(v -> backFromLogin());

    }
    public void checkFields() {
        String email = binding.emailET.getText().toString();
        String pass = binding.passwordET.getText().toString();
        if(TextUtils.isEmpty(email)){
            binding.emailET.setError("Phone Field can't be Blank");
        }
        else if(TextUtils.isEmpty(pass)){
            binding.passwordET.setError("Password Field can't be Blank");
        }
        else {
            loadingBar1.setTitle("Login Account");
            loadingBar1.setMessage("Please Wait, While we are checking the credentials");
            loadingBar1.setCanceledOnTouchOutside(false);
            loadingBar1.show();
            AllowAccessAccount(email,pass);
    }
    }

    private void AllowAccessAccount(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    System.out.println("taskkk");
                    System.out.println(task);

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

    public void signup(){
        startActivity(new Intent(getApplicationContext(),SignupActivity.class));
    }
    public void backFromLogin(){
        startActivity(new Intent(getApplicationContext(),LaunchActivity.class));
    }

}