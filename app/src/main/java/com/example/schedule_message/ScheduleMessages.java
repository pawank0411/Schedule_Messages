package com.example.schedule_message;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schedule_message.model.MessageData;

import java.util.ArrayList;
import java.util.Calendar;

public class ScheduleMessages extends AppCompatActivity {

    private TimePicker timePicker1;
    private Calendar calendar;
    private int hour, min;
    private EditText phone, message;
    private ArrayList<MessageData> messageArrayList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_schedule_message);


        Button send = findViewById(R.id.send);
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

                    String main_mes = message.getText().toString() + " " + getApplicationContext().getString(R.string.whatsapp_suffix);
                    editor.putString("message", main_mes);
                    editor.apply();

                    Intent intent = new Intent(ScheduleMessages.this, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(ScheduleMessages.this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) ScheduleMessages.this.getSystemService(ALARM_SERVICE);
                    if (alarmManager != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            hour = timePicker1.getCurrentHour();
                            min = timePicker1.getCurrentMinute();
                            calendar.set(Calendar.HOUR_OF_DAY, hour);
                            calendar.set(Calendar.MINUTE, min);
                            Toast.makeText(ScheduleMessages.this, "The message is being schedule for " + hour + ":" + min, Toast.LENGTH_SHORT).show();
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                            messageArrayList.add(new MessageData(phone.getText().toString(), message.getText().toString(), hour + ":" + min));
                        }
                    }
                } else {
                    Toast.makeText(ScheduleMessages.this, "Enter details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void showTime(int hour, int min) {
        String format = "";
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
