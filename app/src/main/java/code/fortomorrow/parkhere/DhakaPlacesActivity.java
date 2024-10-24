package code.fortomorrow.parkhere;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import code.fortomorrow.parkhere.databinding.ActivityDhakaPlacesBinding;
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

public class DhakaPlacesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

  public static boolean isRentedInDhaka = false;
  private String rentedHours;
  private int availSpots = 2;  // Available parking spots
  private final String[] places = { "Kallanpur", "Mirpur", "Nikonjo" };
  private int rent = 0;
  private int cash2 = 0;
  private int cashnow;
  private FirebaseUser firebaseUser;
  private DatabaseReference cash;
  ActivityDhakaPlacesBinding binding;
  private int check = 0;
  private Toast toast;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityDhakaPlacesBinding.inflate(getLayoutInflater());
    View view = binding.getRoot();
    setContentView(view);

    SharedPref.init(this);
    Spinner spin = findViewById(R.id.dhakaplacesSpinner);
    spin.setOnItemSelectedListener(this);
    ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, places);
    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spin.setAdapter(aa);

    View view2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.customtoast, null);
    toast = new Toast(getApplicationContext());
    toast.setDuration(Toast.LENGTH_LONG);
    toast.setView(view2);

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    firebaseUser = firebaseAuth.getCurrentUser();
    binding.cashTextView2.setText(SharedPref.read("cash", ""));
    loadCashAsync();

    check = 0;

    binding.dhaka1hourRent.setOnClickListener(v -> handleRentClick(1, 20));
    binding.dhaka2hourRent.setOnClickListener(v -> handleRentClick(2, 40));
    binding.dhaka3hourRent.setOnClickListener(v -> handleRentClick(3, 60));
    binding.release1hour.setOnClickListener(v -> release1Hour());
  }

  private void handleRentClick(int hours, int rentAmount) {
    if (isRentedInDhaka) {
      toast.show();
    } else if (check < rentAmount) {
      toast.show();
      Log.d("Rate", +check + " " + cashnow);
    } else {
      availSpots -= 1;
      rentedHours = hours + " Hour";
      binding.dhakaAvailablespots.setText(String.valueOf(availSpots));
      binding.dhakaselectedhour.setText(String.format(" %s", rentedHours));
      isRentedInDhaka = true;
      rent += rentAmount;

      String cashBefore = SharedPref.read("cash", "");
      cashnow = Integer.parseInt(cashBefore);
      cash2 = cashnow - rent;

      updateCashAsync(cash2);
      binding.cashTextView2.setText(String.valueOf(cash2));
      rent = 0;

      DateFormat dateFormat = new SimpleDateFormat("h:mm a");
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.HOUR_OF_DAY, hours);
      binding.dhakafinishTime.setText(dateFormat.format(cal.getTime()));
      check = cash2;
    }
  }

  private void loadCashAsync() {
    String cashRunning = SharedPref.read("cash", "");
    check = cashRunning.isEmpty() ? 0 : Integer.parseInt(cashRunning);
    binding.cashTextView2.setText(cashRunning);

    String uid = firebaseUser.getUid();
    cash = FirebaseDatabase.getInstance().getReference().child("Cash");
    cash.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.exists() && snapshot.hasChild("amount")) {
          int cashValue = snapshot.child("amount").getValue(Integer.class);
          SharedPref.write("cash", String.valueOf(cashValue));
          binding.cashTextView2.setText(String.valueOf(cashValue));
          check = cashValue;
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {
        Log.e("Firebase", "Error fetching cash amount", error.toException());
      }
    });
  }

  private void updateCashAsync(int cash2) {
    String uid = firebaseUser.getUid();
    HashMap<String, Object> cashAmount = new HashMap<>();
    cashAmount.put("amount", cash2);

    cash.child(uid).updateChildren(cashAmount).addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        SharedPref.write("cash", String.valueOf(cash2));
        Log.d("CashUpdate", "Cash updated successfully");
      } else {
        Log.e("CashUpdate", "Error updating cash", task.getException());
      }
    });
  }

  public void release1Hour() {
    availSpots += 1;
    binding.dhakaAvailablespots.setText(String.valueOf(availSpots));
    isRentedInDhaka = false;
    binding.afterRent.setVisibility(View.INVISIBLE);
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    String selectedSpots = places[position];
    binding.selectedSpots.setText(selectedSpots);
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {}

}
