package com.example.schedule_message;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import java.util.List;

public class WhatsappAccessibilityService extends AccessibilityService {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent (AccessibilityEvent event) {
        if (getRootInActiveWindow () == null) {
            return;
        }

        AccessibilityNodeInfoCompat rootInActiveWindow = AccessibilityNodeInfoCompat.wrap (getRootInActiveWindow ());

        // Whatsapp Message EditText id
        List<AccessibilityNodeInfoCompat> messageNodeList = rootInActiveWindow.findAccessibilityNodeInfosByViewId ("com.whatsapp:id/entry");
        if (messageNodeList == null || messageNodeList.isEmpty ()) {
            return;
        }

        // check if the whatsapp message EditText field is filled with text
        AccessibilityNodeInfoCompat messageField = messageNodeList.get (0);
        if (messageField.getText () == null || messageField.getText ().length () == 0 
            || !messageField.getText ().toString ().endsWith (getApplicationContext ().getString (R.string.whatsapp_suffix))) { // So your service doesn't process any message, but the ones ending your apps suffix
            return;
        }

        // Whatsapp send button id
        List<AccessibilityNodeInfoCompat> sendMessageNodeInfoList = rootInActiveWindow.findAccessibilityNodeInfosByViewId ("com.whatsapp:id/send");
        if (sendMessageNodeInfoList == null || sendMessageNodeInfoList.isEmpty ()) {
            Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        AccessibilityNodeInfoCompat sendMessageButton = sendMessageNodeInfoList.get (0);
        if (!sendMessageButton.isVisibleToUser ()) {
            return;
        }

        // Now fire a click on the send button
        sendMessageButton.performAction (AccessibilityNodeInfo.ACTION_CLICK);

        try {
            Thread.sleep (500);
            performGlobalAction (GLOBAL_ACTION_BACK);
            Thread.sleep (500);
        } catch (InterruptedException ignored) {}
        performGlobalAction (GLOBAL_ACTION_BACK);
    }

    @Override
    public void onInterrupt() {

    }
}