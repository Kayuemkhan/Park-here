package code.fortomorrow.parkhere;

import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import code.fortomorrow.parkhere.databinding.ActivityKhulnaPlacesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class KhulnaPlacesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

  LinearLayout khulnaafterRentshow;
  private String rentedHours;
  private int availSpots = 2;
  private final String[] places = {"Bagherhat", "Lalon Shah's Mazaar", "Sat Gumbad Mosque"};
  private int rent = 0;
  private int cash2 = 0;
  private int cashnow;
  private FirebaseUser firebaseUser;
  private DatabaseReference cash;
  private int check = 0;
  private Toast toast;
  ActivityKhulnaPlacesBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityKhulnaPlacesBinding.inflate(getLayoutInflater());
    View view = binding.getRoot();
    setContentView(view);

    SharedPref.init(this);
    Spinner spin = findViewById(R.id.khulnaplacesSpinner);
    spin.setOnItemSelectedListener(KhulnaPlacesActivity.this);
    ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, places);
    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spin.setAdapter(aa);

    khulnaafterRentshow = findViewById(R.id.khulnaafterRentshow);
    View view2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.customtoast, null);
    toast = new Toast(getApplicationContext());
    toast.setDuration(Toast.LENGTH_LONG);
    toast.setView(view2);

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    firebaseUser = firebaseAuth.getCurrentUser();
    cash = FirebaseDatabase.getInstance().getReference().child("Cash");

    // Fetch cash asynchronously from Firebase
    String uid = firebaseUser.getUid();
    cash.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        String cashRunning = SharedPref.read("cash", "");
        if (dataSnapshot.exists()) {
          cashnow = ((Long) dataSnapshot.child("amount").getValue()).intValue();
          SharedPref.write("cash", String.valueOf(cashnow));
          check = cashnow;
          binding.cashTextViewForKhulna.setText(String.valueOf(cashnow));
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        Log.e("FirebaseError", "Error fetching cash: " + databaseError.getMessage());
      }
    });

    binding.khulna1hourRent.setOnClickListener(new View.OnClickListener() {
      @RequiresApi(api = Build.VERSION_CODES.O)
      @Override
      public void onClick(View v) {
        if (DhakaPlacesActivity.isRentedInDhaka) {
          Toast.makeText(getApplicationContext(), "You have already rented a spot.", Toast.LENGTH_LONG).show();
        } else if (check <= 0) {
          toast.show();
          Log.d("Rate", +check + " " + cashnow);
        } else {
          handleRent(1, 20);
        }
      }
    });

    binding.khulna2hourRent.setOnClickListener(v -> handleRent(2, 40));
    binding.khulna3hourRent.setOnClickListener(v -> handleRent(3, 60));
    binding.khulna1hourRelease.setOnClickListener(v -> release1Hour());
  }

  private void handleRent(int hours, int rentCost) {
    if (DhakaPlacesActivity.isRentedInDhaka) {
      Toast.makeText(getApplicationContext(), "You have already rented a spot.", Toast.LENGTH_LONG).show();
    } else if (check < rentCost) {
      toast.show();
      Log.d("Rate", "Insufficient cash: " + check + " " + cashnow);
    } else {
      availSpots -= 1;
      rentedHours = hours + " Hour" + (hours > 1 ? "s" : "");
      binding.khulnaplaceAvailablespots.setText(String.valueOf(availSpots));
      binding.khulnaselectedhour.setText(" " + rentedHours);
      DhakaPlacesActivity.isRentedInDhaka = true;

      rent += rentCost;
      cash2 = cashnow - rent;
      String uid = firebaseUser.getUid();
      SharedPref.write("cash", String.valueOf(cash2));

      HashMap<String, Object> cashAmount = new HashMap<>();
      cashAmount.put("amount", cash2);
      cash.child(uid).updateChildren(cashAmount);

      binding.cashTextViewForKhulna.setText(String.valueOf(cash2));
      rent = 0;
      khulnaafterRentshow.setVisibility(View.VISIBLE);

      DateFormat dateFormat = new SimpleDateFormat("h:mm a");
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.HOUR_OF_DAY, hours);
      binding.khulnafinishTime.setText(dateFormat.format(cal.getTime()));
      check = cash2;
    }
  }

  public void release1Hour() {
    availSpots += 1;
    binding.khulnaplaceAvailablespots.setText(String.valueOf(availSpots));
    DhakaPlacesActivity.isRentedInDhaka = false;
    khulnaafterRentshow.setVisibility(View.INVISIBLE);
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    String selectedSpot = places[position];
    binding.selectedspotinkhulna.setText(selectedSpot);
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {
    // No-op
  }
}
