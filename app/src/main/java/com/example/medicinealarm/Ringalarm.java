package com.example.medicinealarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class Ringalarm extends AppCompatActivity {

    ListView medicineListView;
    Button stop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringalarm);

        medicineListView = findViewById(R.id.m_list);
        stop=findViewById(R.id.stop_btn);
        // Retrieve the medicine details from the intent extras
        Intent intent = getIntent();
        ArrayList<String> matchMedicine = intent.getStringArrayListExtra("medicineDetails");

        // Create an ArrayAdapter to display the medicine details in the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, matchMedicine);
        medicineListView.setAdapter(adapter);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmReceiver alarmReceiver = new AlarmReceiver();
                alarmReceiver.stopAlarm();
                finish();
                //System.exit(0);
            }
        });
    }
}