package code.fortomorrow.parkhere;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import code.fortomorrow.parkhere.databinding.ActivityChittagongPlacesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Objects;

public class ChittagongPlacesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
  private ActivityChittagongPlacesBinding binding;
  private LinearLayout chittagongafterRentshow;
  private String rentedHours;
  private int availSpots = 2; // Default available spots
  private final String[] places = { "Foyes lake", "Patenga beach", "Chittagong circuit house" };
  private int rent = 0;
  private int cash2 = 0;
  private int cashnow;
  private FirebaseUser firebaseUser;
  private DatabaseReference cash;
  private int check = 0;
  private Toast toast;
  private Spinner spin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityChittagongPlacesBinding.inflate(getLayoutInflater());
    View view = binding.getRoot();
    setContentView(view);
    SharedPref.init(this);

    spin = findViewById(R.id.chittagongplacesSpinner);
    spin.setOnItemSelectedListener(this);
    ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, places);
    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spin.setAdapter(aa);

    chittagongafterRentshow = findViewById(R.id.chittagongafterRentshow);
    View view2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.customtoast, null);
    toast = new Toast(getApplicationContext());
    toast.setDuration(Toast.LENGTH_LONG);
    toast.setView(view2);

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    firebaseUser = firebaseAuth.getCurrentUser();
    cash = FirebaseDatabase.getInstance().getReference().child("Cash");

    // Async read cash value
    String uid = firebaseUser.getUid();
    cash.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        //String cashValue = snapshot.child("amount").getValue(String.class);
        cashnow = ((Long) Objects.requireNonNull(snapshot.child("amount").getValue())).intValue();
        SharedPref.write("cash", String.valueOf(cashnow));

        check =cashnow ;
        binding.cashTextViewForChittagong.setText(String.valueOf(check));
      }

      @Override
      public void onCancelled(DatabaseError error) {
        Log.e("FirebaseError", "Failed to read cash data.", error.toException());
      }
    });

    binding.chittagong1hourRent.setOnClickListener(v -> rentSlot(1, 20));
    binding.chittagong2hourRent.setOnClickListener(v -> rentSlot(2, 40));
    binding.chittagong3hourRent.setOnClickListener(v -> rentSlot(3, 60));
    binding.chittagongrelease1hour.setOnClickListener(v -> release1Hour());
  }

  private void rentSlot(int hours, int cost) {
    if (DhakaPlacesActivity.isRentedInDhaka) {
      toast.show();
    } else if (check < cost) {
      toast.show();
    } else {
      availSpots -= 1;
      rentedHours = hours + " Hour";
      binding.chittagongAvailablespots.setText(String.valueOf(availSpots));
      binding.chittagongselectedhour.setText(rentedHours);
      DhakaPlacesActivity.isRentedInDhaka = true;
      rent += cost;
      cashnow = check;
      cash2 = cashnow - rent;
      String uid = firebaseUser.getUid();
      SharedPref.write("cash", String.valueOf(cash2));
      HashMap<String, Object> cashAmount = new HashMap<>();
      cashAmount.put("amount", cash2);
      cash.child(uid).updateChildren(cashAmount);

      binding.cashTextViewForChittagong.setText(String.valueOf(cash2));
      rent = 0;
      chittagongafterRentshow.setVisibility(View.VISIBLE);

      DateFormat dateFormat = new SimpleDateFormat("h:mm a");
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.HOUR_OF_DAY, hours);
      binding.chittagongfinishTime.setText(dateFormat.format(cal.getTime()));
      check = cash2; // Update check with the new cash value
    }
  }

  public void release1Hour() {
    availSpots += 1;
    binding.chittagongAvailablespots.setText(String.valueOf(availSpots));
    DhakaPlacesActivity.isRentedInDhaka = false;
    chittagongafterRentshow.setVisibility(View.INVISIBLE);
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    String selectedSpot = places[position];
    binding.selectedspotinchittagong.setText(selectedSpot);
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {
    // No action needed
  }
}
