package com.example.services_example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MediaScannerBroadcastReceiverCustom extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        android.util.Log.d("Applog","pic added to gallery");

    }
}
