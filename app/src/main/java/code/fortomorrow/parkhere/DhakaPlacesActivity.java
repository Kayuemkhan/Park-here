package code.fortomorrow.parkhere;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    private int rent = 0;
    private int cash2 ;
    int cashnow;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference cash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dhaka_places);
        SharedPref.init(this);
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        cashTextViewDhaka = findViewById(R.id.cashTextView2);
        cashTextViewDhaka.setText(SharedPref.read("cash",""));
        cash = FirebaseDatabase.getInstance().getReference().child("Cash");
        dhaka1hourRent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                rent += 20;
                String cashBefore = SharedPref.read("cash","");
                cashnow = Integer.parseInt(cashBefore);
                cash2= cashnow - rent;
                String uid = firebaseUser.getUid();
                SharedPref.write("cash",String.valueOf(cash2));
                HashMap<String, Object> cashammount = new HashMap<>();
                cashammount.put("amount",cash2);
                cashTextViewDhaka.setText(cash2);
                cash.child(uid).updateChildren(cashammount);
            }
        });
    }
}