package com.example.medicinealarm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class delete_page extends AppCompatActivity {

    private EditText med_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_page);
        med_name=findViewById(R.id.med_delete);
    }
    public void delete(View view){
        Database_file database_file = new Database_file(this);
        boolean bool=database_file.deleteMedicine(med_name.getText().toString());
        if(bool)
            finish();
    }
}