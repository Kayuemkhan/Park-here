package code.fortomorrow.parkhere;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import code.fortomorrow.parkhere.databinding.ActivityMainBinding;
import code.fortomorrow.parkhere.model.Cash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference cashtext;
    private FirebaseAuth auth= FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    private String currentId = user.getUid();
    private RadioGroup radioGroup;
    private String text;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);
        cashtext = FirebaseDatabase.getInstance().getReference().child("Cash").child(currentId);
        dataset();
        SharedPref.init(this);
        binding.dhakaplace.setOnClickListener(v -> dhakaplaceclick());
        binding.chittagongplace.setOnClickListener(v -> chittagongplaceclick());
        binding.khulnaplace.setOnClickListener(v -> khulnaplaceclick());
        binding.rajshahiid.setOnClickListener(v -> rajshahiPlaceclick());
    }

    private void dataset() {
        cashtext.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Cash cash = snapshot.getValue(Cash.class);
                    String cashvalue= cash.getAmount().toString();
                    SharedPref.write("cash",cashvalue);
                    binding.cashTextView.setText(cashvalue);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    void dhakaplaceclick(){

        startActivity(new Intent(getApplicationContext(),DhakaPlacesActivity.class));

    }
    @SuppressLint("ResourceAsColor")
    void chittagongplaceclick(){

        startActivity(new Intent(getApplicationContext(),ChittagongPlacesActivity.class));

    }
    @SuppressLint("ResourceAsColor")
    void khulnaplaceclick(){

        startActivity(new Intent(getApplicationContext(),KhulnaPlacesActivity.class));

    }
    @SuppressLint("ResourceAsColor")
    void rajshahiPlaceclick(){

        startActivity(new Intent(getApplicationContext(),RajshahiPlacesActivity.class));

    }

}