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
import code.fortomorrow.parkhere.databinding.ActivityRajshahiPlacesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class RajshahiPlacesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    LinearLayout rajshahiafterRentshow;
    private String rentedHours;
    private int availSpots = 2;
    private String selectedSpots;
    private final String[] places = {"RUET Campus", "RU Campus", "Bagha Mosque"};
    private int rent = 0;
    private int cash2 = 0;
    private int cashnow;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference cash;
    private String cashRunning;
    private int check = 0;
    private Toast toast;
    private Spinner spin;
    ActivityRajshahiPlacesBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRajshahiPlacesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        SharedPref.init(this);
        spin = findViewById(R.id.rajshahiplacesSpinner);
        spin.setOnItemSelectedListener(RajshahiPlacesActivity.this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, places);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        rajshahiafterRentshow = findViewById(R.id.rajshahiafterrentShow);
        View view2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.customtoast, null);
        toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view2);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        binding.cashTextViewRajshahi.setText(SharedPref.read("cash", ""));
        cash = FirebaseDatabase.getInstance().getReference().child("Cash");
        cashRunning = SharedPref.read("cash", "");
        check = Integer.parseInt(cashRunning);
        binding.rajsahhi2hourRent.setOnClickListener(v -> setRajshahi2hourRen());
        binding.rajshahi3hourRent.setOnClickListener(v -> setRajshahi3hourRen());
        binding.rajshahi1hourRelease.setOnClickListener(v -> release1Hour());

        binding.rajshahi1hourRent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (DhakaPlacesActivity.isRentedInDhaka) {
                    Toast.makeText(getApplicationContext(), "You Have Already Rent a slot", Toast.LENGTH_LONG).show();
                } else if (check <= 0) {
                    toast.show();
                    Log.d("Rate", +check + " " + cashnow);
//                    Toast.makeText(getApplicationContext(),"You Don't have enough Cash",Toast.LENGTH_LONG).show();
                } else {
                    availSpots -= 1;
                    rentedHours = "1 Hour";
                    binding.rajshahiplaceAvailablespots.setText(String.valueOf(availSpots));
                    binding.rajshahiselectedhour.setText(" " + rentedHours);
                    DhakaPlacesActivity.isRentedInDhaka = true;
                    rent += 20;
                    String cashBefore = SharedPref.read("cash", "");
                    cashnow = Integer.parseInt(cashBefore);
                    cash2 = cashnow - rent;
                    String uid = firebaseUser.getUid();
                    SharedPref.write("cash", String.valueOf(cash2));
                    HashMap<String, Object> cashammount = new HashMap<>();
                    cashammount.put("amount", cash2);
                    binding.cashTextViewRajshahi.setText(String.valueOf(cash2));
                    rent = 0;
                    cash.child(uid).updateChildren(cashammount);
                    rajshahiafterRentshow.setVisibility(View.VISIBLE);
                    DateFormat dateFormat = new SimpleDateFormat("h:mm a");
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.HOUR_OF_DAY, 1);
                    binding.rajshahifinishTime.setText(dateFormat.format(cal.getTime()));
                    check = cash2;

                }

            }
        });
    }

    public void setRajshahi2hourRen() {
        if (DhakaPlacesActivity.isRentedInDhaka) {

            Toast.makeText(getApplicationContext(), "You Have Already Rent a slot", Toast.LENGTH_LONG).show();
        } else if (check < 40) {
            toast.show();
            Log.d("Rate", +check + " " + cashnow);
//                    Toast.makeText(getApplicationContext(),"You Don't have enough Cash",Toast.LENGTH_LONG).show();
        } else {
            availSpots -= 1;
            rentedHours = "2 Hour";
            binding.rajshahiplaceAvailablespots.setText(String.valueOf(availSpots));
            binding.rajshahiselectedhour.setText(" " + rentedHours);
            DhakaPlacesActivity.isRentedInDhaka = true;
            rent += 40;
            String cashBefore = SharedPref.read("cash", "");
            cashnow = Integer.parseInt(cashBefore);
            cash2 = cashnow - rent;
            String uid = firebaseUser.getUid();
            SharedPref.write("cash", String.valueOf(cash2));
            HashMap<String, Object> cashammount = new HashMap<>();
            cashammount.put("amount", cash2);
            binding.cashTextViewRajshahi.setText(String.valueOf(cash2));
            rent = 0;
            cash.child(uid).updateChildren(cashammount);
            rajshahiafterRentshow.setVisibility(View.VISIBLE);
            DateFormat dateFormat = new SimpleDateFormat("h:mm a");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR_OF_DAY, 2);
            binding.rajshahifinishTime.setText(dateFormat.format(cal.getTime()));
            check = cash2;

        }
    }

    public void setRajshahi3hourRen() {
        if (DhakaPlacesActivity.isRentedInDhaka) {

            Toast.makeText(getApplicationContext(), "You Have Already Rent a slot", Toast.LENGTH_LONG).show();
        } else if (check < 60) {
            toast.show();
            Log.d("Rate", +check + " " + cashnow);
//                    Toast.makeText(getApplicationContext(),"You Don't have enough Cash",Toast.LENGTH_LONG).show();
        } else {
            availSpots -= 1;
            rentedHours = "3 Hour";
            binding.rajshahiplaceAvailablespots.setText(String.valueOf(availSpots));
            binding.rajshahiselectedhour.setText(" " + rentedHours);
            DhakaPlacesActivity.isRentedInDhaka = true;
            rent += 60;
            String cashBefore = SharedPref.read("cash", "");
            cashnow = Integer.parseInt(cashBefore);
            cash2 = cashnow - rent;
            String uid = firebaseUser.getUid();
            SharedPref.write("cash", String.valueOf(cash2));
            HashMap<String, Object> cashammount = new HashMap<>();
            cashammount.put("amount", cash2);
            binding.cashTextViewRajshahi.setText(String.valueOf(cash2));
            rent = 0;
            cash.child(uid).updateChildren(cashammount);
            rajshahiafterRentshow.setVisibility(View.VISIBLE);
            DateFormat dateFormat = new SimpleDateFormat("h:mm a");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR_OF_DAY, 3);
            binding.rajshahifinishTime.setText(dateFormat.format(cal.getTime()));
            check = cash2;

        }
    }


    public void release1Hour() {
        availSpots += 1;
        binding.rajshahiplaceAvailablespots.setText(String.valueOf(availSpots));
        DhakaPlacesActivity.isRentedInDhaka = false;
        rajshahiafterRentshow.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedSpots = places[position];
        binding.selectedspotinRajshahi.setText(selectedSpots.toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}