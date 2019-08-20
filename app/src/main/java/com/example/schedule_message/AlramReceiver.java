package com.example.schedule_message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import java.net.URLEncoder;

public  class AlramReceiver extends BroadcastReceiver {
    private String phone;
    private String message;
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Details", 0);

        phone = sharedPreferences.getString("phone", null);
        message = sharedPreferences.getString("message", null);

        Toast.makeText(context, "recieve", Toast.LENGTH_SHORT).show();
        PackageManager packageManager = context.getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);
        try {
            String url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + URLEncoder.encode(message, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                context.startActivity(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
