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
import android.widget.Spinner;
import android.widget.Toast;
import code.fortomorrow.parkhere.databinding.ActivityDhakaPlacesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class DhakaPlacesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
     

  public static boolean isRentedInDhaka= false;
    private String rentedHours;
    private int availSpots = 2;
  private final String [] places = {"Kallanpur","Mirpur","Nikonjo"};
    private int rent = 0;
    private int cash2 =0 ;
    private int cashnow;
  private FirebaseUser firebaseUser;
    private DatabaseReference cash;
    ActivityDhakaPlacesBinding binding;
  private int check = 0;
    private Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      binding =
          ActivityDhakaPlacesBinding.inflate(getLayoutInflater());
      View view = binding.getRoot();

      setContentView(view);
      SharedPref.init(this);
        Spinner spin =  findViewById(R.id.dhakaplacesSpinner);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,places);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.customtoast, null);
         toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
      FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
       binding.cashTextView2.setText(SharedPref.read("cash",""));
        cash = FirebaseDatabase.getInstance().getReference().child("Cash");
      String cashRunning = SharedPref.read("cash", "");
        check = Integer.parseInt(cashRunning);
        binding.dhaka1hourRent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(isRentedInDhaka){
                    toast.show();
                }
                else  if(check <= 0  ){
                    toast.show();
                    Log.d("Rate",+check+" "+cashnow);
                }
                else {

                    availSpots -= 1;
                    rentedHours = "1 Hour";
                    binding.dhakaAvailablespots.setText(String.valueOf(availSpots));
                    binding.dhakaselectedhour.setText(String.format(" %s", rentedHours));
                    isRentedInDhaka = true;
                    rent += 20;
                    String cashBefore = SharedPref.read("cash","");
                    cashnow = Integer.parseInt(cashBefore);
                    cash2= cashnow - rent;
                    String uid = firebaseUser.getUid();
                    SharedPref.write("cash",String.valueOf(cash2));
                    HashMap<String, Object> cashammount = new HashMap<>();
                    cashammount.put("amount",cash2);
                    binding.cashTextView2.setText(String.valueOf(cash2));
                    rent=0;
                    cash.child(uid).updateChildren(cashammount);
                    binding.afterRent.setVisibility(View.VISIBLE);
                    DateFormat dateFormat = new SimpleDateFormat("h:mm a");
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.HOUR_OF_DAY,1);
                    binding.dhakafinishTime.setText(dateFormat.format(cal.getTime()));
                    check = cash2;

                }

            }
        });
        binding.release1hour.setOnClickListener(v -> release1Hour());
        binding.dhaka2hourRent.setOnClickListener(v -> setDhaka2HourRen());
        binding.dhaka3hourRent.setOnClickListener(v -> setDhaka3HourRan());

    }
    public void release1Hour(){
        availSpots +=1;
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
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void setDhaka2HourRen(){
        if(isRentedInDhaka){
            Toast.makeText(getApplicationContext(),"You Have Already Rent a slot",Toast.LENGTH_LONG).show();
        }
        else  if(check < 40  ){
            toast.show();
        }
        else {
            availSpots -= 1;
            rentedHours = "2 Hour";
            binding.dhakaAvailablespots.setText(String.valueOf(availSpots));
            binding.dhakaselectedhour.setText(" "+rentedHours);
            isRentedInDhaka = true;
            rent += 40;
            String cashBefore = SharedPref.read("cash","");
            cashnow = Integer.parseInt(cashBefore);
            cash2= cashnow - rent;
            String uid = firebaseUser.getUid();
            SharedPref.write("cash",String.valueOf(cash2));
            HashMap<String, Object> cashammount = new HashMap<>();
            cashammount.put("amount",cash2);
            binding.cashTextView2.setText(String.valueOf(cash2));
            rent=0;
            cash.child(uid).updateChildren(cashammount);
            binding.afterRent.setVisibility(View.VISIBLE);
            DateFormat dateFormat = new SimpleDateFormat("h:mm a");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR_OF_DAY,2);
          binding.dhakafinishTime.setText(dateFormat.format(cal.getTime()));
            check = cash2;
        }
    }
    public void setDhaka3HourRan(){
        if(isRentedInDhaka){
            Toast.makeText(getApplicationContext(),"You Have Already Rent a slot",Toast.LENGTH_LONG).show();
        }
        else  if(check < 60 ){
            toast.show();
//            Toast.makeText(getApplicationContext(),"You Don't have enough Cash",Toast.LENGTH_LONG).show();
        }
        else {
            availSpots -= 1;
            rentedHours = "6 Hour";
          binding. dhakaAvailablespots.setText(String.valueOf(availSpots));
          binding. dhakaselectedhour.setText(" "+rentedHours);
            isRentedInDhaka = true;
            rent += 40;
            String cashBefore = SharedPref.read("cash","");
            cashnow = Integer.parseInt(cashBefore);
            cash2= cashnow - rent;
            String uid = firebaseUser.getUid();
            SharedPref.write("cash",String.valueOf(cash2));
            HashMap<String, Object> cashammount = new HashMap<>();
            cashammount.put("amount",cash2);
            binding.cashTextView2.setText(String.valueOf(cash2));
            rent=0;
            cash.child(uid).updateChildren(cashammount);
          binding. afterRent.setVisibility(View.VISIBLE);
            DateFormat dateFormat = new SimpleDateFormat("h:mm a");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR_OF_DAY,3);
          binding.dhakafinishTime.setText(dateFormat.format(cal.getTime()));
            check = cash2;
        }
    }
}