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
import code.fortomorrow.parkhere.databinding.ActivityChittagongPlacesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class ChittagongPlacesActivity extends AppCompatActivity
    implements AdapterView.OnItemSelectedListener {
  private ActivityChittagongPlacesBinding binding;
  private LinearLayout chittagongafterRentshow;
  private String rentedHours;
  private int availSpots = 2;
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

    binding.chittagong2hourRent.setOnClickListener(v -> {
      setChittagong2hourRen();
    });

    binding.chittagong3hourRent.setOnClickListener(v -> {
      setChittagong3hourRen();
    });
    binding.chittagongrelease1hour.setOnClickListener(v -> {
      release1Hour();
    });

    spin = findViewById(R.id.chittagongplacesSpinner);
    spin.setOnItemSelectedListener(ChittagongPlacesActivity.this);
    ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, places);
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
    String cashRunning = SharedPref.read("cash", "");
    check = Integer.parseInt(cashRunning);
    binding.chittagong1hourRent.setOnClickListener(new View.OnClickListener() {
      @RequiresApi(api = Build.VERSION_CODES.O)
      @Override
      public void onClick(View v) {
        if (DhakaPlacesActivity.isRentedInDhaka) {
          Toast.makeText(getApplicationContext(), "You Have Already Rent a slot", Toast.LENGTH_LONG)
              .show();
        } else if (check <= 0) {
          toast.show();
          Log.d("Rate", +check + " " + cashnow);
          //                    Toast.makeText(getApplicationContext(),"You Don't have enough Cash",Toast.LENGTH_LONG).show();
        } else {
          availSpots -= 1;
          rentedHours = "1 Hour";
          binding.chittagongAvailablespots.setText(String.valueOf(availSpots));
          binding.chittagongselectedhour.setText(String.format(" %s", rentedHours));
          DhakaPlacesActivity.isRentedInDhaka = true;
          rent += 20;
          String cashBefore = SharedPref.read("cash", "");
          cashnow = Integer.parseInt(cashBefore);
          cash2 = cashnow - rent;
          String uid = firebaseUser.getUid();
          SharedPref.write("cash", String.valueOf(cash2));
          HashMap<String, Object> cashammount = new HashMap<>();
          cashammount.put("amount", cash2);
          binding.cashTextViewForChittagong.setText(String.valueOf(cash2));
          rent = 0;
          cash.child(uid).updateChildren(cashammount);
          chittagongafterRentshow.setVisibility(View.VISIBLE);
          DateFormat dateFormat = new SimpleDateFormat("h:mm a");
          Calendar cal = Calendar.getInstance();
          cal.add(Calendar.HOUR_OF_DAY, 1);
          binding.chittagongfinishTime.setText(dateFormat.format(cal.getTime()));
          check = cash2;
        }
      }
    });
  }

  public void setChittagong2hourRen() {
    if (DhakaPlacesActivity.isRentedInDhaka) {
      toast.show();
      //                    Toast.makeText(getApplicationContext(),"You Have Already Rent a slot",Toast.LENGTH_LONG).show();
    } else if (check < 40) {
      toast.show();
      Log.d("Rate", +check + " " + cashnow);
      //                    Toast.makeText(getApplicationContext(),"You Don't have enough Cash",Toast.LENGTH_LONG).show();
    } else {
      availSpots -= 1;
      rentedHours = "2 Hour";
      binding.chittagongAvailablespots.setText(String.valueOf(availSpots));
      binding.chittagongselectedhour.setText(" " + rentedHours);
      DhakaPlacesActivity.isRentedInDhaka = true;
      rent += 40;
      String cashBefore = SharedPref.read("cash", "");
      cashnow = Integer.parseInt(cashBefore);
      cash2 = cashnow - rent;
      String uid = firebaseUser.getUid();
      SharedPref.write("cash", String.valueOf(cash2));
      HashMap<String, Object> cashammount = new HashMap<>();
      cashammount.put("amount", cash2);
      binding.cashTextViewForChittagong.setText(String.valueOf(cash2));
      rent = 0;
      cash.child(uid).updateChildren(cashammount);
      chittagongafterRentshow.setVisibility(View.VISIBLE);
      DateFormat dateFormat = new SimpleDateFormat("h:mm a");
      Calendar cal = null;
      cal = Calendar.getInstance();
      cal.add(Calendar.HOUR_OF_DAY, 2);
      binding.chittagongfinishTime.setText(dateFormat.format(cal.getTime()));
      check = cash2;
    }
  }

  public void setChittagong3hourRen() {
    if (DhakaPlacesActivity.isRentedInDhaka) {
      toast.show();
      //                    Toast.makeText(getApplicationContext(),"You Have Already Rent a slot",Toast.LENGTH_LONG).show();
    } else if (check < 60) {
      toast.show();
      Log.d("Rate", +check + " " + cashnow);
      //                    Toast.makeText(getApplicationContext(),"You Don't have enough Cash",Toast.LENGTH_LONG).show();
    } else {
      availSpots -= 1;
      rentedHours = "3 Hour";
      binding.chittagongAvailablespots.setText(String.valueOf(availSpots));
      binding.chittagongselectedhour.setText(" " + rentedHours);
      DhakaPlacesActivity.isRentedInDhaka = true;
      rent += 60;
      String cashBefore = SharedPref.read("cash", "");
      cashnow = Integer.parseInt(cashBefore);
      cash2 = cashnow - rent;
      String uid = firebaseUser.getUid();
      SharedPref.write("cash", String.valueOf(cash2));
      HashMap<String, Object> cashammount = new HashMap<>();
      cashammount.put("amount", cash2);
      binding.cashTextViewForChittagong.setText(String.valueOf(cash2));
      rent = 0;
      cash.child(uid).updateChildren(cashammount);
      chittagongafterRentshow.setVisibility(View.VISIBLE);
      DateFormat dateFormat = new SimpleDateFormat("h:mm a");
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.HOUR_OF_DAY, 3);
      binding.chittagongfinishTime.setText(dateFormat.format(cal.getTime()));
      check = cash2;
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
    String selectedSpots = places[position];
    binding.selectedspotinchittagong.setText(selectedSpots);
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }
}
