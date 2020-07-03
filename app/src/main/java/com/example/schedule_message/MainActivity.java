package com.example.schedule_message;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule_message.adapter.ListAdapter;
import com.example.schedule_message.model.MessageData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private ArrayList<MessageData> messageDataArrayList = new ArrayList<>();
    private TimePicker timePicker1;
    private Calendar calendar;
    private int hour, min;
    private EditText phone, message;
    private Button send;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton add_task = findViewById(R.id.add_task);
        recyclerView = findViewById(R.id.recycler_view);

        if (!isAccessibilityOn(this)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("Please open settings to allow accessibility permission")
                    .setCancelable(false)
                    .setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).create().show();
        }

        add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater layoutInflater = getLayoutInflater();
                View dialogview = layoutInflater.inflate(R.layout.activity_schedule_message, null);
                alert.setView(dialogview);
                AlertDialog alertDialog = alert.create();
                alertDialog.show();

                phone = dialogview.findViewById(R.id.phn);
                message = dialogview.findViewById(R.id.message);
                send = dialogview.findViewById(R.id.send);

                SharedPreferences sharedPreferences = getSharedPreferences("Details", 0);
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                timePicker1 = dialogview.findViewById(R.id.timePicker1);
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

                            Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager alarmManager = (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);
                            if (alarmManager != null) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    hour = timePicker1.getCurrentHour();
                                    min = timePicker1.getCurrentMinute();
                                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                                    calendar.set(Calendar.MINUTE, min);
                                    Toast.makeText(MainActivity.this, "The message is being schedule for " + hour + ":" + min, Toast.LENGTH_SHORT).show();
                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                                    MessageData messageData = new MessageData(phone.getText().toString(),
                                            message.getText().toString(), hour + ":" + min);
                                    messageDataArrayList.add(messageData);
                                    ListAdapter listAdapter = new ListAdapter(messageDataArrayList, MainActivity.this);
                                    recyclerView.hasFixedSize();
                                    recyclerView.setAdapter(listAdapter);
                                    listAdapter.notifyDataSetChanged();
                                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                }
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Enter details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }

    private boolean isAccessibilityOn(Context context) {
        int accessibilityEnabled = 0;
        final String service = context.getPackageName() + "/" + WhatsappAccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException ignored) {
        }

        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                colonSplitter.setString(settingValue);
                while (colonSplitter.hasNext()) {
                    String accessibilityService = colonSplitter.next();

                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
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