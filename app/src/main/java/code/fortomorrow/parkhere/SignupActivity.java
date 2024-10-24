package code.fortomorrow.parkhere;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import code.fortomorrow.parkhere.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

  public Button signupButton;
  private FirebaseAuth mAuth;
  private ProgressDialog loadingBar1;
  private DatabaseReference usersData;
  private DatabaseReference cash;
  int amount = 200;
  ActivitySignupBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    View view = binding.getRoot();

    setContentView(view);
    loadingBar1 = new ProgressDialog(this);
    mAuth = FirebaseAuth.getInstance();
    binding = ActivitySignupBinding.inflate(getLayoutInflater());

    usersData = FirebaseDatabase.getInstance().getReference().child("Users");
    cash = FirebaseDatabase.getInstance().getReference().child("Cash");
    binding.signUpButton.setOnClickListener(v -> {
      signUp();
    });
    binding.signInBack.setOnClickListener(v -> {
      signup();
    });
  }

  public void signUp() {
    String email = binding.emailETSignUp.getText().toString();
    String pass = binding.passwordETSignUp.getText().toString();
    if (TextUtils.isEmpty(email)) {
      binding.emailETSignUp.setError("Phone Field can't be Blank");
      return;
    } else if (TextUtils.isEmpty(pass)) {
      binding.passwordETSignUp.setError("Password Field can't be Blank");
      return;
    } else {
      loadingBar1.setTitle("Login Account");
      loadingBar1.setMessage("Please Wait, While we are checking the credentials");
      loadingBar1.setCanceledOnTouchOutside(false);
      loadingBar1.show();

      AllowAccssAccount(email, pass);
    }
  }

  private void AllowAccssAccount(String email, String pass) {
    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        FirebaseUser user = mAuth.getCurrentUser();
        HashMap<String, Object> usersdata = new HashMap<>();
        usersdata.put("email", email);
        usersdata.put("pass", pass);
        String user2 = user.getUid();
        usersData.child(user2).updateChildren(usersdata);
        HashMap<String, Object> cashammount = new HashMap<>();
        cashammount.put("amount", amount);
        cash.child(user2).updateChildren(cashammount);
        //cash.child(user2).setValue("200");
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        loadingBar1.dismiss();
        finish();
      } else {
        Toast.makeText(SignupActivity.this, "Authentication failed.",
            Toast.LENGTH_SHORT).show();
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Log.e("error", e.getMessage());
        Toast.makeText(getApplicationContext(), "error" + e.getMessage(), Toast.LENGTH_LONG).show();
        loadingBar1.dismiss();
      }
    });
  }

  public void signup() {
    startActivity(new Intent(getApplicationContext(), LaunchActivity.class));
  }
}