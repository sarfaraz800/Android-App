package com.example.medicinealarm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class display_page extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_page);

        //RecyclerView
        listView = findViewById(R.id.medicine_list);
        Database_file database_file=new Database_file(this);

        ArrayList<MedicineModel> arrMedicine = database_file.fetchMedicine();
        ArrayList<String> medicineDetails = new ArrayList<>();

        //Displays database in LogCat
        for(int i=0;i<arrMedicine.size();i++)
            Log.d("Medicine Detail","Name: "+arrMedicine.get(i).name + " Date: "+arrMedicine.get(i).date +" Expiry: "+arrMedicine.get(i).expiry + " Time: "+arrMedicine.get(i).time);

        for (MedicineModel medicine : arrMedicine) {
            String detail = "Name: " + medicine.name + " Date: " + medicine.date +"\n" +"Expiry: "+medicine.expiry + " Time: " + medicine.time;
            medicineDetails.add(detail);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicineDetails);
        listView.setAdapter(adapter);
    }
}