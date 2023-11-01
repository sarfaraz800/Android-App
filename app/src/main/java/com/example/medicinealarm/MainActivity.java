package com.example.medicinealarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import kotlin.jvm.internal.MagicApiIntrinsics;

public class MainActivity extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void page_insert(View view) {
        // Start a new activity here
        Intent intent = new Intent(MainActivity.this, Insert_page.class);
        startActivity(intent);
    }
    public void page_display(View view) {
        Intent intent = new Intent(MainActivity.this, display_page.class);
        startActivity(intent);
    }
    public void page_alarm(View view){
        Intent intent=new Intent(MainActivity.this,alarmpage.class);
        startActivity(intent);
    }
    public void page_delete(View view){
        Intent intent=new Intent(MainActivity.this,delete_page.class);
        startActivity(intent);
    }

}
