package code.fortomorrow.parkhere;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DhakaPlacesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.cashTextView2)
     TextView cashTextViewDhaka;
    @BindView(R.id.dhakaAvailablespots)
    TextView dhakaAvailablespots;
    @BindView(R.id.dhaka1hourRent)
    TextView dhaka1hourRent;
    @BindView(R.id.dhaka2hourRent)
    TextView dhaka2HourRent;
    @BindView(R.id.dhaka3hourRent)
    TextView dhaka3HourRant;
    LinearLayout afterRent;
    @BindView(R.id.dhakaselectedhour)
    TextView dhakaselectedhour;
    @BindView(R.id.release1hour)
    TextView release1hour;
    @BindView(R.id.dhakafinishTime)
    TextView dhakafinishTime;
    @BindView(R.id.selectedSpots)
    TextView selectedSpot;

    public static boolean isRentedInDhaka= false;
    private String rentedHours;
    private int availSpots = 2;
    private String selectedSpots ;
    private String [] places = {"Kallanpur","Mirpur","Nikonjo"};
    private int rent = 0;
    private int cash2 =0 ;
    private int cashnow;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference cash;
    private String cashRunning;
    private int check = 0;
    private Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dhaka_places);
        SharedPref.init(this);
        ButterKnife.bind(this);
        Spinner spin =  findViewById(R.id.dhakaplacesSpinner);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,places);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        afterRent = findViewById(R.id.afterRent);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.customtoast, null);
         toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        cashTextViewDhaka = findViewById(R.id.cashTextView2);
        cashTextViewDhaka.setText(SharedPref.read("cash",""));
        cash = FirebaseDatabase.getInstance().getReference().child("Cash");
        cashRunning = SharedPref.read("cash","");
        check = Integer.parseInt(cashRunning);
        dhaka1hourRent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(isRentedInDhaka == true){
                    toast.show();
//                    Toast.makeText(getApplicationContext(),"You Have Already Rent a slot",Toast.LENGTH_LONG).show();
                }
                else  if(check <= 0  ){
                    toast.show();
                    Log.d("Rate",+check+" "+cashnow);
//                    Toast.makeText(getApplicationContext(),"You Don't have enough Cash",Toast.LENGTH_LONG).show();
                }
                else {

                    availSpots -= 1;
                    rentedHours = "1 Hour";
                    dhakaAvailablespots.setText(String.valueOf(availSpots));
                    dhakaselectedhour.setText(" "+rentedHours);
                    isRentedInDhaka = true;
                    rent += 20;
                    String cashBefore = SharedPref.read("cash","");
                    cashnow = Integer.parseInt(cashBefore);
                    cash2= cashnow - rent;
                    String uid = firebaseUser.getUid();
                    SharedPref.write("cash",String.valueOf(cash2));
                    HashMap<String, Object> cashammount = new HashMap<>();
                    cashammount.put("amount",cash2);
                    cashTextViewDhaka.setText(String.valueOf(cash2));
                    rent=0;
                    cash.child(uid).updateChildren(cashammount);
                    afterRent.setVisibility(View.VISIBLE);
                    DateFormat dateFormat = new SimpleDateFormat("h:mm a");
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.HOUR_OF_DAY,1);
                    dhakafinishTime.setText(dateFormat.format(cal.getTime()));
                    check = cash2;

                }

            }
        });

    }
    @OnClick(R.id.release1hour)
    public void release1Hour(){
        availSpots +=1;
        dhakaAvailablespots.setText(String.valueOf(availSpots));
        isRentedInDhaka = false;
        afterRent.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedSpots = places[position];
            selectedSpot.setText(selectedSpots);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick(R.id.dhaka2hourRent)
    public void setDhaka2HourRen(){
        if(isRentedInDhaka == true){
            Toast.makeText(getApplicationContext(),"You Have Already Rent a slot",Toast.LENGTH_LONG).show();
        }
        else  if(check < 40  ){
            toast.show();
//            Toast.makeText(getApplicationContext(),"You Don't have enough Cash",Toast.LENGTH_LONG).show();
        }
        else {
            availSpots -= 1;
            rentedHours = "2 Hour";
            dhakaAvailablespots.setText(String.valueOf(availSpots));
            dhakaselectedhour.setText(" "+rentedHours);
            isRentedInDhaka = true;
            rent += 40;
            String cashBefore = SharedPref.read("cash","");
            cashnow = Integer.parseInt(cashBefore);
            cash2= cashnow - rent;
            String uid = firebaseUser.getUid();
            SharedPref.write("cash",String.valueOf(cash2));
            HashMap<String, Object> cashammount = new HashMap<>();
            cashammount.put("amount",cash2);
            cashTextViewDhaka.setText(String.valueOf(cash2));
            rent=0;
            cash.child(uid).updateChildren(cashammount);
            afterRent.setVisibility(View.VISIBLE);
            DateFormat dateFormat = new SimpleDateFormat("h:mm a");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR_OF_DAY,1);
            dhakafinishTime.setText(dateFormat.format(cal.getTime()));
            check = cash2;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick(R.id.dhaka3hourRent)
    public void setDhaka3HourRan(){
        if(isRentedInDhaka == true){
            Toast.makeText(getApplicationContext(),"You Have Already Rent a slot",Toast.LENGTH_LONG).show();
        }
        else  if(check < 60 ){
            toast.show();
//            Toast.makeText(getApplicationContext(),"You Don't have enough Cash",Toast.LENGTH_LONG).show();
        }
        else {
            availSpots -= 1;
            rentedHours = "6 Hour";
            dhakaAvailablespots.setText(String.valueOf(availSpots));
            dhakaselectedhour.setText(" "+rentedHours);
            isRentedInDhaka = true;
            rent += 40;
            String cashBefore = SharedPref.read("cash","");
            cashnow = Integer.parseInt(cashBefore);
            cash2= cashnow - rent;
            String uid = firebaseUser.getUid();
            SharedPref.write("cash",String.valueOf(cash2));
            HashMap<String, Object> cashammount = new HashMap<>();
            cashammount.put("amount",cash2);
            cashTextViewDhaka.setText(String.valueOf(cash2));
            rent=0;
            cash.child(uid).updateChildren(cashammount);
            afterRent.setVisibility(View.VISIBLE);
            DateFormat dateFormat = new SimpleDateFormat("h:mm a");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR_OF_DAY,1);
            dhakafinishTime.setText(dateFormat.format(cal.getTime()));
            check = cash2;
        }
    }
}