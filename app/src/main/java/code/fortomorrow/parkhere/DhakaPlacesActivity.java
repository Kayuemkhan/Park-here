package code.fortomorrow.parkhere;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DhakaPlacesActivity extends AppCompatActivity {
    ListView listView;
    ArrayAdapter arrayAdapter;
    String[] strings = {"Kallanpur, Mirpur"};
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dhaka_places);
        listView = findViewById(R.id.list);
        ArrayAdapter arrayAdapter = new ArrayAdapter<String> (this,R.layout.listivew,R.id.slots,strings);
        listView.setAdapter(arrayAdapter);

    }
}