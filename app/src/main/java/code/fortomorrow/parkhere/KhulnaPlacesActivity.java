package code.fortomorrow.parkhere;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KhulnaPlacesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.cashTextViewForKhulna)
    TextView cashTextviewForkhulna;
    @BindView(R.id.khulnaplaceAvailablespots)
    TextView khulnaplaceAvailablespots;
    @BindView(R.id.khulna1hourRent)
    TextView khulna1hourRent;
    @BindView(R.id.khulna2hourRent)
    TextView khulna2hourRent;
    @BindView(R.id.khulna3hourRent)
    TextView khulna3hourRent;
    @BindView(R.id.khulnaselectedhour)
    TextView khulnaselectedhour;
    @BindView(R.id.khulnafinishTime)
    TextView khulnafinishTime;
    @BindView(R.id.khulna1hourRelease)
    TextView khulna1hourRelease;
    @BindView(R.id.selectedspotinkhulna)
    TextView selectedspotinkhulna;
    LinearLayout khulnaafterRentshow;
    private String rentedHours;
    private int availSpots = 2;
    private String selectedSpots;
    private String[] places = {"Bagherhat", "Lalon Shah's Mazaar", "Sat Gumbad Mosque"};
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khulna_places);
        ButterKnife.bind(this);
        SharedPref.init(this);
        spin = findViewById(R.id.khulnaplacesSpinner);
        spin.setOnItemSelectedListener(KhulnaPlacesActivity.this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, places);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        khulnaafterRentshow = findViewById(R.id.khulnaafterRentshow);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.customtoast, null);
        toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        cashTextviewForkhulna = findViewById(R.id.cashTextViewForKhulna);
        cashTextviewForkhulna.setText(SharedPref.read("cash", ""));
        cash = FirebaseDatabase.getInstance().getReference().child("Cash");
        cashRunning = SharedPref.read("cash", "");
        check = Integer.parseInt(cashRunning);
        khulna1hourRent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (DhakaPlacesActivity.isRentedInDhaka == true) {
                    Toast.makeText(getApplicationContext(), "You Have Already Rent a slot", Toast.LENGTH_LONG).show();
                } else if (check <= 0) {
                    toast.show();
                    Log.d("Rate", +check + " " + cashnow);
//                    Toast.makeText(getApplicationContext(),"You Don't have enough Cash",Toast.LENGTH_LONG).show();
                } else {
                    availSpots -= 1;
                    rentedHours = "1 Hour";
                    khulnaplaceAvailablespots.setText(String.valueOf(availSpots));
                    khulnaselectedhour.setText(" " + rentedHours);
                    DhakaPlacesActivity.isRentedInDhaka = true;
                    rent += 20;
                    String cashBefore = SharedPref.read("cash", "");
                    cashnow = Integer.parseInt(cashBefore);
                    cash2 = cashnow - rent;
                    String uid = firebaseUser.getUid();
                    SharedPref.write("cash", String.valueOf(cash2));
                    HashMap<String, Object> cashammount = new HashMap<>();
                    cashammount.put("amount", cash2);
                    cashTextviewForkhulna.setText(String.valueOf(cash2));
                    rent = 0;
                    cash.child(uid).updateChildren(cashammount);
                    khulnaafterRentshow.setVisibility(View.VISIBLE);
                    DateFormat dateFormat = new SimpleDateFormat("h:mm a");
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.HOUR_OF_DAY, 1);
                    khulnafinishTime.setText(dateFormat.format(cal.getTime()));
                    check = cash2;

                }

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick(R.id.khulna2hourRent)
    public void setKhulna2hourRent() {
        if (DhakaPlacesActivity.isRentedInDhaka == true) {

            Toast.makeText(getApplicationContext(), "You Have Already Rent a slot", Toast.LENGTH_LONG).show();
        } else if (check < 40) {
            toast.show();
            Log.d("Rate", +check + " " + cashnow);
//                    Toast.makeText(getApplicationContext(),"You Don't have enough Cash",Toast.LENGTH_LONG).show();
        } else {
            availSpots -= 1;
            rentedHours = "2 Hour";
            khulnaplaceAvailablespots.setText(String.valueOf(availSpots));
            khulnaselectedhour.setText(" " + rentedHours);
            DhakaPlacesActivity.isRentedInDhaka = true;
            rent += 40;
            String cashBefore = SharedPref.read("cash", "");
            cashnow = Integer.parseInt(cashBefore);
            cash2 = cashnow - rent;
            String uid = firebaseUser.getUid();
            SharedPref.write("cash", String.valueOf(cash2));
            HashMap<String, Object> cashammount = new HashMap<>();
            cashammount.put("amount", cash2);
            cashTextviewForkhulna.setText(String.valueOf(cash2));
            rent = 0;
            cash.child(uid).updateChildren(cashammount);
            khulnaafterRentshow.setVisibility(View.VISIBLE);
            DateFormat dateFormat = new SimpleDateFormat("h:mm a");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR_OF_DAY, 2);
            khulnafinishTime.setText(dateFormat.format(cal.getTime()));
            check = cash2;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick(R.id.khulna3hourRent)
    public void setKhulna3hourRent() {
        if (DhakaPlacesActivity.isRentedInDhaka == true) {

            Toast.makeText(getApplicationContext(), "You Have Already Rent a slot", Toast.LENGTH_LONG).show();
        } else if (check < 60) {
            toast.show();
            Log.d("Rate", +check + " " + cashnow);
//                    Toast.makeText(getApplicationContext(),"You Don't have enough Cash",Toast.LENGTH_LONG).show();
        } else {
            availSpots -= 1;
            rentedHours = "3 Hour";
            khulnaplaceAvailablespots.setText(String.valueOf(availSpots));
            khulnaselectedhour.setText(" " + rentedHours);
            DhakaPlacesActivity.isRentedInDhaka = true;
            rent += 60;
            String cashBefore = SharedPref.read("cash", "");
            cashnow = Integer.parseInt(cashBefore);
            cash2 = cashnow - rent;
            String uid = firebaseUser.getUid();
            SharedPref.write("cash", String.valueOf(cash2));
            HashMap<String, Object> cashammount = new HashMap<>();
            cashammount.put("amount", cash2);
            cashTextviewForkhulna.setText(String.valueOf(cash2));
            rent = 0;
            cash.child(uid).updateChildren(cashammount);
            khulnaafterRentshow.setVisibility(View.VISIBLE);
            DateFormat dateFormat = new SimpleDateFormat("h:mm a");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR_OF_DAY, 3);
            khulnafinishTime.setText(dateFormat.format(cal.getTime()));
            check = cash2;

        }
    }

    @OnClick(R.id.khulna1hourRelease)
    public void release1Hour() {
        availSpots += 1;
        khulnaplaceAvailablespots.setText(String.valueOf(availSpots));
        DhakaPlacesActivity.isRentedInDhaka = false;
        khulnaafterRentshow.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}