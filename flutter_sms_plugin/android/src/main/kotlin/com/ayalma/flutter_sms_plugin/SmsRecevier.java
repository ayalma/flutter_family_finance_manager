package com.ayalma.flutter_sms_plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsRecevier extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = SmsRecevier.class.getSimpleName();

    /**
     * Invoked by the OS when a timer goes off.
     *
     * <p>The associated timer was registered in {@link SmsService}.
     *
     * <p>In Android, timer notifications require a {@link BroadcastReceiver} as the artifact that is
     * notified when the timer goes off. As a result, this method is kept simple, immediately
     * offloading any work to {@link SmsService#enqueueSmsProcessing(Context, Intent)}.
     *
     * <p>This method is the beginning of an execution path that will eventually execute a desired
     * Dart callback function, as registed by the Dart side of the android_alarm_manager plugin.
     * However, there may be asynchronous gaps between {@code onReceive()} and the eventual invocation
     * of the Dart callback because {@link SmsService} may need to spin up a Flutter execution
     * context before the callback can be invoked.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Intent recieved: " + intent.getAction());
        if (intent.getAction() == SMS_RECEIVED) {
            SmsService.enqueueSmsProcessing(context,intent);

        }
    }
}
