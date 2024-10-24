package code.fortomorrow.parkhere;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import code.fortomorrow.parkhere.databinding.ActivityLaunchBinding;

public class LaunchActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    code.fortomorrow.parkhere.databinding.ActivityLaunchBinding binding =
        ActivityLaunchBinding.inflate(getLayoutInflater());
    View view = binding.getRoot();

    setContentView(view);
    binding.loginButtoninlaunch.setOnClickListener(v -> buttonLogin());
    binding.registerButtonlaunch.setOnClickListener(v -> buttonRegister());
  }

  public void buttonLogin() {
    startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
    finish();
  }

  public void buttonRegister() {
    startActivity(new Intent(LaunchActivity.this, SignupActivity.class));
    finish();
  }
}