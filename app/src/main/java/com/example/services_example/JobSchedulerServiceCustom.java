package com.example.services_example;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

@SuppressLint("NewApi")
public class JobSchedulerServiceCustom extends JobService {

    private Context mContext;
    private static final int ASJOBSERVICE_JOB_ID = 999;

    // A pre-built JobInfo we use for scheduling our job.
    private static JobInfo JOB_INFO = null;

    public static int a(Context context) {
        int schedule = ((JobScheduler) context.getSystemService(JobScheduler.class)).schedule(JOB_INFO);
        Log.i("PhotosContentJob", "JOB SCHEDULED!");
        int imgNumber = countImgs(Environment.getExternalStorageDirectory(), 0);
        Toast.makeText(context, "total photo ="+imgNumber, Toast.LENGTH_SHORT).show();
        Log.i("PhotosContentJob", "Photos="+imgNumber);


        return schedule;
    }

    // Schedule this job, replace any existing one.
    public static void scheduleJob(Context context) {
        if (JOB_INFO != null) {
            a(context);
        } else {
            JobScheduler js = context.getSystemService(JobScheduler.class);
            JobInfo.Builder builder = new JobInfo.Builder(ASJOBSERVICE_JOB_ID,
                   new ComponentName(BuildConfig.APPLICATION_ID, JobSchedulerServiceCustom.class.getName()));
           builder.addTriggerContentUri(new JobInfo.TriggerContentUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 1));
            builder.addTriggerContentUri(new JobInfo.TriggerContentUri(MediaStore.Images.Media.INTERNAL_CONTENT_URI, 1));
           builder.setTriggerContentMaxDelay(500);
            JOB_INFO = builder.build();
            js.schedule(JOB_INFO);
        }
    }


    @Override
    public boolean onStartJob(JobParameters params) {
        mContext = this;

        scheduleJob(this);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {


        return true;
    }

    public static int countImgs(File file, int number) {
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
