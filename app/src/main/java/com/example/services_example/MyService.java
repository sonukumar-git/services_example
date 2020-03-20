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
import android.graphics.Color;
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

import static com.example.services_example.MainActivity.CHANNEL_ID;

public class MyService extends Service {
    public static final String SERVICE_ID = "Foreground_Service";
    int NOTIFICATION_ID = 1;
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
        //  notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("Enable \uD83C\uDF1FBobble AI Keyboard\uD83C\uDF1F")
//                .setContentText("Don’t miss the fun")
//                .setSmallIcon(R.drawable.ic_launcher)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(false)
//                .build();
//
//        startForeground(1, notification);

        //do heavy work on a background thread
        //stopSelf();
        notificationBuilder = new NotificationCompat.Builder(this);
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            notificationBuilder.setSmallIcon(R.drawable.ic_launcher);
            notificationBuilder.setContentTitle("ohhh")
                    .setStyle(
                            new NotificationCompat.BigTextStyle()
                                    .bigText("hello"))
                    .setAutoCancel(true).setDefaults(Notification.DEFAULT_SOUND)
                    .setLights(Color.WHITE, 500, 500)
                    .setContentText("ram ram");
        } else {


            RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.layout_bobble_notification);
            notificationView.setImageViewResource(R.id.ivBobbleLogo, R.drawable.ic_launcher);
            notificationView.setTextViewText(R.id.tvBobbleKeyboard, "bbbb");
            notificationView.setTextViewText(R.id.tvEnableBobbleKeyboard, "Enable \uD83C\uDF1FBobble AI Keyboard\uD83C\uDF1F");


            // The id of the channel.
            String id = "Bop-MusicPlayer";
            // Finally... Actually creating the Notification
            // The user-visible name of the channel.
            CharSequence name = "bop";

            // The user-visible description of the channel.
            String description = "music-player";


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel mChannel = null;
                mChannel = new NotificationChannel(id, name, importance);

                // Configure the notification channel.
                mChannel.setDescription(description);
                notificationManager.createNotificationChannel(mChannel);
            }

            notificationBuilder.setContentIntent(pendingIntent)
                    .setContentTitle("Enable \uD83C\uDF1FBobble AI Keyboard\uD83C\uDF1F")
                    .setContentText("Don’t miss the fun")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigPictureStyle())
                    .setCustomContentView(notificationView)
                    .setChannelId(id)
                    .setCustomBigContentView(notificationView);
        }

        notificationBuilder.setContentIntent(pendingIntent);
        Notification notification = notificationBuilder.build();
        notificationManager.notify(NOTIFICATION_ID, notification);
        startForeground(NOTIFICATION_ID, notification);


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


}
