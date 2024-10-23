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

public class ChittagongPlacesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.cashTextViewForChittagong)
    TextView cashTextviewForChittagong;
    @BindView(R.id.chittagongAvailablespots)
    TextView chittagongAvailablespots;
    @BindView(R.id.chittagong1hourRent)
    TextView chittagong1hourRent;
    @BindView(R.id.chittagong2hourRent)
    TextView chittagong2hourRent;
    @BindView(R.id.chittagong3hourRent)
    TextView chittagong3hourRent;
    @BindView(R.id.chittagongselectedhour)
    TextView chittagongselectedhour;
    @BindView(R.id.chittagongfinishTime)
    TextView chittagongfinishTime;
    @BindView(R.id.chittagongrelease1hour)
    TextView chittagongrelease1hour;
    @BindView(R.id.selectedspotinchittagong)
    TextView selectedspotinchittagong;
    LinearLayout chittagongafterRentshow;
    private String rentedHours;
    private int availSpots = 2;
    private String selectedSpots;
    private String[] places = {"Foyes lake", "Patenga beach", "Chittagong circuit house"};
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
        setContentView(R.layout.activity_chittagong_places);
        ButterKnife.bind(this);
        SharedPref.init(this);
        spin = findViewById(R.id.chittagongplacesSpinner);
        spin.setOnItemSelectedListener(ChittagongPlacesActivity.this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, places);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        chittagongafterRentshow = findViewById(R.id.chittagongafterRentshow);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.customtoast, null);
        toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        cashTextviewForChittagong = findViewById(R.id.cashTextViewForChittagong);
        cashTextviewForChittagong.setText(SharedPref.read("cash", ""));
        cash = FirebaseDatabase.getInstance().getReference().child("Cash");
        cashRunning = SharedPref.read("cash", "");
        check = Integer.parseInt(cashRunning);
        chittagong1hourRent.setOnClickListener(new View.OnClickListener() {
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
                    chittagongAvailablespots.setText(String.valueOf(availSpots));
                    chittagongselectedhour.setText(" " + rentedHours);
                    DhakaPlacesActivity.isRentedInDhaka = true;
                    rent += 20;
                    String cashBefore = SharedPref.read("cash", "");
                    cashnow = Integer.parseInt(cashBefore);
                    cash2 = cashnow - rent;
                    String uid = firebaseUser.getUid();
                    SharedPref.write("cash", String.valueOf(cash2));
                    HashMap<String, Object> cashammount = new HashMap<>();
                    cashammount.put("amount", cash2);
                    cashTextviewForChittagong.setText(String.valueOf(cash2));
                    rent = 0;
                    cash.child(uid).updateChildren(cashammount);
                    chittagongafterRentshow.setVisibility(View.VISIBLE);
                    DateFormat dateFormat = new SimpleDateFormat("h:mm a");
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.HOUR_OF_DAY, 1);
                    chittagongfinishTime.setText(dateFormat.format(cal.getTime()));
                    check = cash2;

                }

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick(R.id.chittagong2hourRent)
    public void setChittagong2hourRen() {
        if (DhakaPlacesActivity.isRentedInDhaka == true) {
            toast.show();
//                    Toast.makeText(getApplicationContext(),"You Have Already Rent a slot",Toast.LENGTH_LONG).show();
        } else if (check < 40) {
            toast.show();
            Log.d("Rate", +check + " " + cashnow);
//                    Toast.makeText(getApplicationContext(),"You Don't have enough Cash",Toast.LENGTH_LONG).show();
        } else {
            availSpots -= 1;
            rentedHours = "2 Hour";
            chittagongAvailablespots.setText(String.valueOf(availSpots));
            chittagongselectedhour.setText(" " + rentedHours);
            DhakaPlacesActivity.isRentedInDhaka = true;
            rent += 40;
            String cashBefore = SharedPref.read("cash", "");
            cashnow = Integer.parseInt(cashBefore);
            cash2 = cashnow - rent;
            String uid = firebaseUser.getUid();
            SharedPref.write("cash", String.valueOf(cash2));
            HashMap<String, Object> cashammount = new HashMap<>();
            cashammount.put("amount", cash2);
            cashTextviewForChittagong.setText(String.valueOf(cash2));
            rent = 0;
            cash.child(uid).updateChildren(cashammount);
            chittagongafterRentshow.setVisibility(View.VISIBLE);
            DateFormat dateFormat = new SimpleDateFormat("h:mm a");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR_OF_DAY, 2);
            chittagongfinishTime.setText(dateFormat.format(cal.getTime()));
            check = cash2;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick(R.id.chittagong3hourRent)
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
            chittagongAvailablespots.setText(String.valueOf(availSpots));
            chittagongselectedhour.setText(" " + rentedHours);
            DhakaPlacesActivity.isRentedInDhaka = true;
            rent += 60;
            String cashBefore = SharedPref.read("cash", "");
            cashnow = Integer.parseInt(cashBefore);
            cash2 = cashnow - rent;
            String uid = firebaseUser.getUid();
            SharedPref.write("cash", String.valueOf(cash2));
            HashMap<String, Object> cashammount = new HashMap<>();
            cashammount.put("amount", cash2);
            cashTextviewForChittagong.setText(String.valueOf(cash2));
            rent = 0;
            cash.child(uid).updateChildren(cashammount);
            chittagongafterRentshow.setVisibility(View.VISIBLE);
            DateFormat dateFormat = new SimpleDateFormat("h:mm a");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR_OF_DAY, 3);
            chittagongfinishTime.setText(dateFormat.format(cal.getTime()));
            check = cash2;
        }
    }

    @OnClick(R.id.chittagongrelease1hour)
    public void release1Hour() {
        availSpots += 1;
        chittagongAvailablespots.setText(String.valueOf(availSpots));
        DhakaPlacesActivity.isRentedInDhaka = false;
        chittagongafterRentshow.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedSpots = places[position];
        selectedspotinchittagong.setText(selectedSpots.toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
