package com.example.medicinealarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.util.ArrayList;

public class AlarmReceiver extends BroadcastReceiver {
    private MediaPlayer mediaPlayer;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the medicine details from the intent
        ArrayList<String> medicineDetails = intent.getStringArrayListExtra("medicineDetails");
        Toast.makeText(context, "Time to take medicine", Toast.LENGTH_LONG).show();

        // Play music here
        mediaPlayer = MediaPlayer.create(context, R.raw.digital);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        // Start the Ringalarm activity
        Intent ringAlarmIntent = new Intent(context, Ringalarm.class);
        ringAlarmIntent.putExtra("medicineDetails", medicineDetails);
        ringAlarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(ringAlarmIntent);
    }
    public void stopAlarm() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        // Cancel the alarm by canceling the PendingIntent
        if (alarmManager != null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}