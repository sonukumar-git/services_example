package com.example.services_example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnStart, btnStop, btnNext;
    public static final String CHANNEL_ID = "exampleServiceChannel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = findViewById(R.id.btn1);
        btnNext = findViewById(R.id.btn3);
        btnStop = findViewById(R.id.btn2);

        btnStart.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnStop.setOnClickListener(this);

        createNotificationChannel();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //  Log.v(TAG, "Permission is granted");

            } else {

                //  Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }
        }


    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btn1:
                Intent i = new Intent(this, MyService.class);
                i.putExtra("inputExtra", "Enjoy Music");
                ContextCompat.startForegroundService(this, i);
                break;
            case R.id.btn3:
                Intent intent = new Intent(this, next.class);
                startActivity(intent);
                break;
            case R.id.btn2:
                stopService(new Intent(this, MyService.class));

                break;

        }

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
