package code.fortomorrow.parkhere;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DhakaPlacesActivity extends AppCompatActivity {
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
    private int rent = 0;
    private int cash2 ;
    int cashnow;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference cash;
    @BindView(R.id.dhakaselectedhour)
    TextView dhakaselectedhour;
    @BindView(R.id.release1hour)
    TextView release1hour;
    public static boolean isRentedInDhaka= false;
    private String rentedHours;
    private int availSpots =2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dhaka_places);
        SharedPref.init(this);
        ButterKnife.bind(this);
        afterRent = findViewById(R.id.afterRent);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        cashTextViewDhaka = findViewById(R.id.cashTextView2);
        cashTextViewDhaka.setText(SharedPref.read("cash",""));
        cash = FirebaseDatabase.getInstance().getReference().child("Cash");
        dhaka1hourRent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(isRentedInDhaka == true){
                    Toast.makeText(getApplicationContext(),"You Have Already Rent a slot",Toast.LENGTH_LONG).show();
                }
                else {
                    availSpots -= 1;
                    rentedHours = "1 Hour";
                    dhakaAvailablespots.setText(String.valueOf(availSpots));
                    dhakaselectedhour.setText(rentedHours);
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
}