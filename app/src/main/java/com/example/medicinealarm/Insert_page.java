package com.example.medicinealarm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Insert_page extends AppCompatActivity {

    EditText med_name,date,expiry;
    Spinner time;
    Button btn;

    //private String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private String[] timesOfDay = {"Morning", "Afternoon", "Evening", "Night"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_page);

        med_name = findViewById(R.id.medicine_name);
        date = findViewById(R.id.date);
        expiry=findViewById(R.id.expiry_date);
        time = findViewById(R.id.time);
        btn = findViewById(R.id.insert_btn);

        // Create and set the adapter for days of the week spinner
        /*ArrayAdapter<String> daysAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, daysOfWeek);
        daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        date.setAdapter(daysAdapter);*/

        // Create and set the adapter for times of day spinner
        ArrayAdapter<String> timesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timesOfDay);
        timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time.setAdapter(timesAdapter);


        Database_file db_file=new Database_file(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String regex="\\d{2}/\\d{2}/\\d{4}";
                boolean bool1=(date.getText().toString().matches(regex));
                boolean bool2=(expiry.getText().toString().matches(regex));
                if (TextUtils.isEmpty(med_name.getText().toString()))
                    med_name.setError("Enter medicine name");
                if(TextUtils.isEmpty(date.getText().toString()))
                    date.setError("Enter date");
                if(TextUtils.isEmpty(expiry.getText().toString()))
                    expiry.setError("Enter date");
                if(!bool1 || !bool2){
                    Toast.makeText(Insert_page.this,"Invalid date format",Toast.LENGTH_SHORT).show();
                }
                else {
                    String name = med_name.getText().toString();
                    String dateValue = date.getText().toString();
                    String expiryValue = expiry.getText().toString();
                    String timeValue = time.getSelectedItem().toString();
                    boolean bool=db_file.insertData(name, dateValue, expiryValue, timeValue);
                    if(bool)
                        finish();
                }
            }
        });
    }
}