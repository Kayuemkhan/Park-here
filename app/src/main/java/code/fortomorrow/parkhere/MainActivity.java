package code.fortomorrow.parkhere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import code.fortomorrow.parkhere.model.Cash;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.cashTextView)
    public TextView cashTextview;
    DatabaseReference cashtext;
    FirebaseAuth auth= FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    private String currentId = user.getUid();
    private RadioGroup radioGroup;
    String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cashtext = FirebaseDatabase.getInstance().getReference().child("Cash").child(currentId);
        dataset();
        ButterKnife.bind(this);


    }

    private void dataset() {
        cashtext.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Cash cash = snapshot.getValue(Cash.class);
                    //Log.d("error",cash.getAmount());
                    cashTextview.setText(cash.getAmount().toString());
                }
                else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @OnClick(R.id.dhakaplace)
    void dhakaplaceclick(){
        startActivity(new Intent(getApplicationContext(),DhakaPlacesActivity.class));
        finish();
    }
    @OnClick(R.id.chittagongplace)
    void chittagongplaceclick(){
        startActivity(new Intent(getApplicationContext(),ChittagongPlacesActivity.class));
    }
    @OnClick(R.id.khulnaplace)
    void khulnaplaceclick(){
        startActivity(new Intent(getApplicationContext(),KhulnaPlacesActivity.class));
    }
    @OnClick(R.id.rajshahiid)
    void rajshahiPlaceclick(){
        startActivity(new Intent(getApplicationContext(),RajshahiPlacesActivity.class));
    }

}