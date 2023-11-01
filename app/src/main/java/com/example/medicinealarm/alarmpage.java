package com.example.medicinealarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class alarmpage extends AppCompatActivity {

    private String[] timesOfDay = {"Morning", "Afternoon", "Evening", "Night"};
    private Spinner input_time;
    private Button btnEnableDisable;
    private EditText editTextTime,input_date;
    private EditText editTextTime2;
    private Button changeBtn;
    private Spinner select_time;
    private AlarmManager alarmManager;
    //private int date,month,year;
    Time morningTime = new Time(9, 0, 0);
    Time afternoonTime = new Time(13, 0, 0);
    Time eveningTime = new Time(17, 0, 0);
    Time nightTime = new Time(21, 0, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmpage);
        input_time = findViewById(R.id.input_time);
        input_date = findViewById(R.id.input_date);
        btnEnableDisable = findViewById(R.id.btnEnableDisable);
        editTextTime = findViewById(R.id.editTextTime);
        editTextTime2 = findViewById(R.id.editTextTime2);
        changeBtn = findViewById(R.id.change_btn);
        select_time = findViewById(R.id.select_time);


        // Create and set the adapter for times of day spinner
        ArrayAdapter<String> timesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timesOfDay);
        timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        input_time.setAdapter(timesAdapter);

        // Create and set the adapter for change times of day
        ArrayAdapter<String> changeTimesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timesOfDay);
        changeTimesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_time.setAdapter(changeTimesAdapter);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Initially, set the components below btnEnableDisable as disabled
        setComponentsEnabled(false);

        btnEnableDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEnabled = !editTextTime.isEnabled();
                setComponentsEnabled(isEnabled);
                btnEnableDisable.setText(isEnabled ? "Disable" : "Enable");
            }
        });

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editTextTime2.getText().toString()))
                    editTextTime2.setError("Enter hour");
                if (TextUtils.isEmpty(editTextTime.getText().toString()))
                    editTextTime.setError("Enter minute");
                else {
                    String selectedTimeOfDay = select_time.getSelectedItem().toString();
                    int hour = Integer.parseInt(editTextTime2.getText().toString());
                    int minute = Integer.parseInt(editTextTime.getText().toString());
                    if (selectedTimeOfDay.equals("Morning")) {
                        morningTime.hour = hour;
                        morningTime.minute = minute;
                    } else if (selectedTimeOfDay.equals("Afternoon")) {
                        afternoonTime.hour = hour;
                        afternoonTime.minute = minute;
                    } else if (selectedTimeOfDay.equals("Evening")) {
                        eveningTime.hour = hour;
                        eveningTime.minute = minute;
                    } else if (selectedTimeOfDay.equals("Night")) {
                        nightTime.hour = hour;
                        nightTime.minute = minute;
                    }

                    Toast.makeText(alarmpage.this, "Time changed successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setComponentsEnabled(boolean isEnabled) {
        editTextTime.setEnabled(isEnabled);
        editTextTime2.setEnabled(isEnabled);
        changeBtn.setEnabled(isEnabled);
        select_time.setEnabled(isEnabled);
    }

    public void page_ring(View view) {
        // Fetch data from the database and store it in an ArrayList
        Database_file database_file = new Database_file(this);
        ArrayList<MedicineModel> medicineList = database_file.fetchMedicine();

        // TODO: Implement further logic using the fetched data
        String inputDate = input_date.getText().toString();

        // Split the input date by '/'
        String[] dateParts = inputDate.split("/");

        // Ensure that the input date has three parts (day, month, year)
        if (dateParts.length == 3) {
            // Extract day, month, and year values
            int date, month, year;
            try {
                date = Integer.parseInt(dateParts[0]);
                month = Integer.parseInt(dateParts[1]);
                year = Integer.parseInt(dateParts[2]);

                ArrayList<String> matchMedicine = new ArrayList<>();
                String selectedTimeOfDay = input_time.getSelectedItem().toString();
                Time time = new Time(0, 0, 0);

                for (MedicineModel medicine : medicineList) {
                    String detail = "Name: " + medicine.name + " Date: " + medicine.date + " Time: " + medicine.time;
                    if (inputDate.equals(medicine.date) && selectedTimeOfDay.equalsIgnoreCase(medicine.time)) {
                        matchMedicine.add(detail);
                    }
                }

                switch (selectedTimeOfDay) {
                    case "Morning":
                        time = morningTime;
                        break;
                    case "Afternoon":
                        time = afternoonTime;
                        break;
                    case "Evening":
                        time = eveningTime;
                        break;
                    case "Night":
                        time = nightTime;
                        break;
                }

                if (matchMedicine.isEmpty()) {
                    Toast.makeText(this, "No medicine at this time", Toast.LENGTH_SHORT).show();
                } else {
                    setAlarm(time, matchMedicine, date, month, year);
                }
            } catch (NumberFormatException e) {
                // Handle the case where the input date cannot be parsed as integers
                Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where the input date does not have the expected format
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
        }
    }

    protected void setAlarm(Time time, ArrayList<String> medicines, int date, int month, int year) {
        if (alarmManager != null) {
            Calendar calendar = Calendar.getInstance();
            //months in Calendar class start from 0(i.e. January==0) so month-1
            calendar.set(year, month - 1, date, time.hour, time.minute, 0);

            Intent alarmIntent = new Intent(alarmpage.this, AlarmReceiver.class);
            alarmIntent.putStringArrayListExtra("medicineDetails", medicines);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(alarmpage.this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);

            if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
                //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                //alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), pendingIntent), pendingIntent);
                Toast.makeText(alarmpage.this, "Alarm set", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(alarmpage.this, "Invalid time selected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
