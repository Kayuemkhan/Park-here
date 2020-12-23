package code.fortomorrow.parkhere;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChittagongPlacesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chittagong_places);
        ButterKnife.bind(this);
    }
}