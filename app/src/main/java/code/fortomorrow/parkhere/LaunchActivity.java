package code.fortomorrow.parkhere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.login_buttoninlaunch)
    public void buttonLogin(){
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
    @OnClick(R.id.register_buttonlaunch)
    public void buttonregistern(){
        startActivity(new Intent(getApplicationContext(),SignupActivity.class));
        finish();
    }
}