package code.fortomorrow.parkhere;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import code.fortomorrow.parkhere.adapter.PlaceAdapter;
import code.fortomorrow.parkhere.databinding.ActivityMainBinding;
import code.fortomorrow.parkhere.model.Cash;
import code.fortomorrow.parkhere.model.PlaceModel;
import code.fortomorrow.parkhere.onclick.ItemClick;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemClick {

    private DatabaseReference cashtext;
    private FirebaseAuth auth= FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    private final String currentId = user.getUid();
    private RadioGroup radioGroup;
    private String text;
    private ActivityMainBinding binding;

    private RecyclerView recyclerView;
    private PlaceAdapter placeAdapter;
    private List<PlaceModel> placeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);
        cashtext = FirebaseDatabase.getInstance().getReference().child("Cash").child(currentId);
        dataset();
        SharedPref.init(this);
        //binding.dhakaplace.setOnClickListener(v -> dhakaplaceclick());
        //binding.chittagongplace.setOnClickListener(v -> chittagongplaceclick());
        //binding.khulnaplace.setOnClickListener(v -> khulnaplaceclick());
        //binding.rajshahiid.setOnClickListener(v -> rajshahiPlaceclick());
        recyclerView = findViewById(R.id.rvDashboard);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data
        addData();
        // Adapter setup
        placeAdapter = new PlaceAdapter(this, placeList,this);
        recyclerView.setAdapter(placeAdapter);

    }

    private void addData() {
        placeList = new ArrayList<>();
        placeList.add(new PlaceModel("Dhaka", "Places: 6", "5.27%", "Rate Increase from normal days."));
        placeList.add(new PlaceModel("Chittagong", "Places: 4", "3.12%", "Slight increase during weekends."));
        placeList.add(new PlaceModel("Khulna", "Places: 3", "1.57%", "Standard rates apply."));
        placeList.add(new PlaceModel("Rajshahi", "Places: 3", "1.57%", "Standard rates apply."));

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

    @Override public void onClick(String itemName) {
        if(itemName.contains("Dhaka")){
            dhakaplaceclick();
        }
        else if(itemName.contains("Chittagong")){
            chittagongplaceclick();
        }
        else if(itemName.contains("Khulna")){
            khulnaplaceclick();
        }
        else if(itemName.contains("Rajshahi")){
            rajshahiPlaceclick();
        }
    }


}