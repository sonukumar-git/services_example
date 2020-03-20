package com.example.services_example;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.File;
import java.security.PublicKey;

public class MyService extends Service {
    public static final String SERVICE_ID = "Foreground_Service";
    int NOTIFICATION_ID=1;
    private RemoteViews notificationView = null;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager = null;

    MediaPlayer myPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
     //   myPlayer = MediaPlayer.create(this, R.raw.sun);
       // myPlayer.setLooping(false);

        Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();



    }

//    @Override
//    public void onStart(Intent intent, int startId) {
//       Toast.makeText(this, "Start service", Toast.LENGTH_SHORT).show();
//            myPlayer.start();
//      int imgNumber = countImgs(Environment.getExternalStorageDirectory(), 0);
//       Toast.makeText(this, "total photo ="+imgNumber, Toast.LENGTH_SHORT).show();
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, flags);

        notificationView = new RemoteViews(this.getPackageName(), R.layout.layout_bobble_notification);

        notificationView.setImageViewResource(R.id.ivBobbleLogo,R.drawable.ic_launcher);
        notificationView.setImageViewResource(R.id.ivCancel,R.drawable.cancel);

//        Notification notification = new NotificationCompat.Builder(this, SERVICE_ID)
//                .setContentTitle("Service Started")
//                .setContentText(input)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentIntent(pendingIntent)
//                .build();

        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);



        // The id of the channel.
        String id = "Bobble_enable";
        // Finally... Actually creating the Notification

        notificationBuilder = new NotificationCompat.Builder(this, id);
        // The user-visible name of the channel.
        CharSequence name = "bop";

        // The user-visible description of the channel.
        String description = "music-player";

        //createNotificationChannel();


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = null;
            mChannel = new NotificationChannel(id, name, importance);

            // Configure the notification channel.
            mChannel.setDescription(description);
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationBuilder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setOngoing(true)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContent(notificationView)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCustomContentView(notificationView)
                .setChannelId(id)
                .setCustomBigContentView(notificationView);
        // Sets the notification to run on the foreground.
        // (why not the former commented line?)
        Notification notification = notificationBuilder.build();
        //this, SERVICE_ID)

        notificationManager.notify(NOTIFICATION_ID, notification);
        startForeground(NOTIFICATION_ID, notification);


     //   startForeground(1, notification);

        //start music
      //  myPlayer.start();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            JobSchedulerServiceCustom.scheduleJob(this);
//
//        }

        Toast.makeText(this, " MyService Started", Toast.LENGTH_LONG).show();

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
      //  myPlayer.stop();

    }

    public int countImgs(File file, int number) {
        File[] dirs = file.listFiles();
        String name = "";
        if (dirs != null) { // Sanity check
            for (File dir : dirs) {
                if (dir.isFile()) { // Check file or directory
                    name = dir.getName().toLowerCase();
                    // Add or delete extensions as needed
                    if (name.endsWith(".png") || name.endsWith(".jpg")
                            || name.endsWith(".jpeg")) {
                        number++;
                    }
                } else number = countImgs(dir, number);
            }
        }

        return number;
    }

    private void createNotificationChannel() {



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    SERVICE_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }


    }
}
