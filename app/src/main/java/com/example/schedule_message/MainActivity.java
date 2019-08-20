package com.example.schedule_message;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Button send;
    private TimePicker timePicker1;
    private Calendar calendar;
    private String format = "";
    private int hour, min;
    private EditText phone, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send = findViewById(R.id.send);
        phone = findViewById(R.id.phn);
        message = findViewById(R.id.message);

        SharedPreferences sharedPreferences = getSharedPreferences("Details", 0);
        final SharedPreferences.Editor editor = sharedPreferences.edit();



        timePicker1 = findViewById(R.id.timePicker1);
        calendar = Calendar.getInstance();

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);
        showTime(hour, min);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!phone.getText().toString().isEmpty() && !message.getText().toString().isEmpty()) {
                    editor.putString("phone", phone.getText().toString());
                    String main_mes = message.getText().toString() + "_SM";
                    editor.putString("message", main_mes);
                    editor.apply();

                    Intent intent = new Intent(MainActivity.this, AlramReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);
                    if (alarmManager != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            hour = timePicker1.getCurrentHour();
                            min = timePicker1.getCurrentMinute();
                            calendar.set(Calendar.HOUR_OF_DAY, hour);
                            calendar.set(Calendar.MINUTE, min);
                            Toast.makeText(MainActivity.this, "The message is being schedule for "+ hour +":"+min, Toast.LENGTH_SHORT).show();
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Enter details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void showTime(int hour, int min) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
    }
}