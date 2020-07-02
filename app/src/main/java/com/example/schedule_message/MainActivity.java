package com.example.schedule_message;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule_message.adapter.ListAdapter;
import com.example.schedule_message.model.MessageData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<MessageData> messageDataArrayList = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("PREF", MODE_PRIVATE);

        FloatingActionButton add_task = findViewById(R.id.add_task);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
//        Intent intent = getIntent();
//        Bundle bundle = intent.getBundleExtra("BUNDLE");
//        if (bundle != null) {
//            messageDataArrayList = (ArrayList<MessageData>) bundle.getSerializable("TASKS");
//        }
//        Toast.makeText(this, String.valueOf(messageDataArrayList.size()), Toast.LENGTH_SHORT).show();
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
        Gson gson = new Gson();
        String json = sharedPreferences.getString("MessageTask", null);
        Type type = new TypeToken<ArrayList<MessageData>>() {
        }.getType();
        messageDataArrayList = gson.fromJson(json, type);

        if (messageDataArrayList == null) {
            messageDataArrayList = new ArrayList<>();
        }
        
        Toast.makeText(this, String.valueOf(messageDataArrayList.size()), Toast.LENGTH_SHORT).show();
        ListAdapter listAdapter = new ListAdapter(messageDataArrayList, this);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
        add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScheduleMessages.class);
                startActivity(intent);
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

    public void getSavedAttendance() {

    }
}