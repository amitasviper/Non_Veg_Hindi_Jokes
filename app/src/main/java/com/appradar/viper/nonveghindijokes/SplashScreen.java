package com.appradar.viper.nonveghindijokes;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;


public class SplashScreen extends Activity {

    public static final String PREFS_NAME = "MyPrefsFile";
    public static boolean isRunning = true;
    boolean firstStart;
    SetupApp setupApp;
    SharedPreferences settings;
    TextView initialising, percent;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        settings = getSharedPreferences(PREFS_NAME, 0);
        firstStart = settings.getBoolean("firstStart", true);

        initialising = (TextView) findViewById(R.id.initialising);
        percent = (TextView) findViewById(R.id.percent);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        AppRater.onStart(this);

        if(firstStart) {
            initialising.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            percent.setVisibility(View.VISIBLE);
        }

        setupProgressBar();
        prepareAndStart();

        StartAnimation();

        setNotifications(this);
    }

    private void setupProgressBar(){
        progressBar.setProgress(0);
        progressBar.setMax(100);
    }

    private void StartMainOptionActivity(){
        Intent intent = new Intent(this, MainOptions.class);
        startActivity(intent);
        finish();
    }
    private void StartAnimation(){
        //Log.d("SplashScreen","Animation Started");
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.alpha);
        anim.reset();
        LinearLayout lin_lay = (LinearLayout) findViewById(R.id.lin_lay);
        lin_lay.clearAnimation();
        lin_lay.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView image_logo = (ImageView) findViewById(R.id.logo);
        image_logo.clearAnimation();
        image_logo.startAnimation(anim);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        //image_logo.clearAnimation();
        image_logo.startAnimation(fadeIn);
        image_logo.startAnimation(fadeOut);

        HandleAnimationEffects handleAnimationEffects = new HandleAnimationEffects();
        fadeOut.setAnimationListener(handleAnimationEffects);

        //Log.d("SplashScreen", "Animation Ended");
    }

    //Animation Handler class that will act at different stages of Animation
    //This class calls the next activity when the current animation ends.
    private class HandleAnimationEffects implements Animation.AnimationListener{
        @Override
        public void onAnimationStart(Animation animation) {
            //Log.d("HandleAnimationEffects", "onAnimationStart called");
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            //Log.d("HandleAnimationEffects", "onAnimationEnd called");
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            //Log.d("HandleAnimationEffects", "onAnimationRepeat called");

        }
    }

    public void prepareAndStart(){
        if(firstStart) {
            setupApp = new SetupApp(SplashScreen.this);
            setupApp.start();
        }

        else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(4000);
                    }
                    catch (Exception e){

                    }
                    StartMainOptionActivity();
                }
            }).start();
        }
    }

    private class SetupApp extends Thread{
        private Context context;
        private JokesDatabase jDatabase;

        public void run() {
            int count = 0;
            jDatabase.open();


            processFilesAndAddToDatabase("double_meaning");
            progressBar.setProgress(count += 100 / 9);
            final int finalCount1 = count;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    percent.setText("" + finalCount1 + "%");
                }
            });
            Log.e("Progess is: ", "10");

            processFilesAndAddToDatabase("hindi");
            progressBar.setProgress(count += 100 / 9);
            final int finalCount2 = count;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    percent.setText("" + finalCount2 +"%");
                }
            });
            Log.e("Progess is: ", "20");

            processFilesAndAddToDatabase("english");
            progressBar.setProgress(count += 100 / 9);
            final int finalCount3 = count;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    percent.setText("" + finalCount3 +"%");
                }
            });
            Log.e("Progess is: ", "30");
            /*
            processFilesAndAddToDatabase("hindi");
            progressBar.setProgress(count += 100 / 9);
            final int finalCount4 = count;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    percent.setText("" + finalCount4 +"%");
                }
            });
            Log.e("Progess is: ", "40");
            processFilesAndAddToDatabase("pappu");
            progressBar.setProgress(count += 100 / 9);
            final int finalCount5 = count;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    percent.setText("" + finalCount5 +"%");
                }
            });
            Log.e("Progess is: ", "50");
            processFilesAndAddToDatabase("pathan");
            progressBar.setProgress(count += 100 / 9);
            final int finalCount6 = count;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    percent.setText("" + finalCount6 +"%");
                }
            });
            Log.e("Progess is: ", "60");
            processFilesAndAddToDatabase("santa_banta");
            progressBar.setProgress(count += 100 / 9);
            final int finalCount7 = count;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    percent.setText("" + finalCount7 +"%");
                }
            });
            Log.e("Progess is: ", "80");
            processFilesAndAddToDatabase("sharabi");
            progressBar.setProgress(count += 100 / 9);
            final int finalCount8 = count;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    percent.setText("" + finalCount8 +"%");
                }
            });
            Log.e("Progess is: ", "80");
            processFilesAndAddToDatabase("vivahit");
            progressBar.setProgress(count += 100 / 9);
            final int finalCount9 = count;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    percent.setText("" + finalCount9 +"%");
                }
            });
            Log.e("Progess is: ", "90");

            */

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstStart", false);
            editor.commit();
            jDatabase.close();

            //dialog.dismiss();
            StartMainOptionActivity();
        }

        SetupApp(Context context){
            this.context = context;
            jDatabase = new JokesDatabase(this.context);
            //Log.d("SetupApp", "SetupApp called :constructor:");
        }


        private void processFilesAndAddToDatabase(String filename){
            ArrayList<ArrayList<String>> allData;
            if (filename.contentEquals("double_meaning"))
                allData = parseFile(R.raw.bacche);
            else if (filename.contentEquals("hindi"))
                allData = parseFile(R.raw.hindi);
            else if (filename.contentEquals("english"))
                allData = parseFile(R.raw.english);
            else {
                System.out.println("No such file");
                allData = null;
            }

            ArrayList<String> title = allData.get(0);
            ArrayList<String> body = allData.get(1);

            for(String text: title){
                String jBody = body.get(title.indexOf(text));
                jDatabase.insertEntry(text, jBody, filename, "n", "n");
            }
        }

        private ArrayList<ArrayList<String>> parseFile(int resourceId){
            ArrayList<ArrayList<String>> allData = new ArrayList<ArrayList<String>>();

            try {
                InputStream ins = context.getResources().openRawResource(resourceId);
                BufferedReader br = new BufferedReader(new InputStreamReader(ins, "UTF-8"));

                ArrayList<String> title = new ArrayList<>();
                ArrayList<String> bodyArray = new ArrayList<>();
                String line;
                String body = "";

                while ((line = br.readLine()) != null) {
                    if (line.equals("$")) {

                        line = br.readLine();
                        title.add(line);
                        while((line = br.readLine()) !=null && !line.equals("@#"))
                            body += line+"\n";

                        bodyArray.add(body);
                        body = "";
                    }
                }

                allData.add(title);
                allData.add(bodyArray);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return allData;
        }
    }

    public static void setNotifications(Context context) {
        Log.e("handleReceiver",":start: called");
        Intent alarmIntent1 = new Intent(context, AlarmReceiver.class);
        alarmIntent1.putExtra("notificationId", 1);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 1, alarmIntent1, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent alarmIntent2 = new Intent(context, AlarmReceiver.class);
        alarmIntent2.putExtra("notificationId",2);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 2, alarmIntent2, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent alarmIntent3 = new Intent(context, AlarmReceiver.class);
        alarmIntent3.putExtra("notificationId",3);
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(context, 3, alarmIntent3, PendingIntent.FLAG_UPDATE_CURRENT);

        /*
        Intent alarmIntent4 = new Intent(context, AlarmReceiver.class);
        alarmIntent4.putExtra("notificationId",4);
        PendingIntent pendingIntent4 = PendingIntent.getBroadcast(context, 4, alarmIntent4, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent alarmIntent5 = new Intent(this, AlarmReceiver.class);
        alarmIntent5.putExtra("notificationId",5);
        PendingIntent pendingIntent5 = PendingIntent.getBroadcast(this, 5, alarmIntent5, PendingIntent.FLAG_UPDATE_CURRENT);
        */

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);

        long timeToAlarm = calendar.getTimeInMillis();

        if(timeToAlarm < System.currentTimeMillis())
            timeToAlarm += 24*60*60*1000;
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeToAlarm, pendingIntent1);

        calendar.set(Calendar.HOUR_OF_DAY, 16);
        timeToAlarm = calendar.getTimeInMillis();
        if(timeToAlarm < System.currentTimeMillis())
            timeToAlarm += 24*60*60*1000;
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeToAlarm, pendingIntent2);

        calendar.set(Calendar.HOUR_OF_DAY, 20);
        timeToAlarm = calendar.getTimeInMillis();
        if(timeToAlarm < System.currentTimeMillis())
            timeToAlarm += 24*60*60*1000;
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeToAlarm, pendingIntent3);


        //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, pendingIntent4);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent5);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 5000, pendingIntent);
        Log.e("handleReceiver",":finish: called");
    }
}
