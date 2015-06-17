package com.appradar.viper.nonveghindijokes;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by viper on 10/5/15.
 */
public class AlarmReceiver extends BroadcastReceiver {
    JokesDatabase jDatabase;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", ":onReceive: called");
        jDatabase = new JokesDatabase(context);
        jDatabase.open();

        Calendar now = GregorianCalendar.getInstance();
        int dayOfWeek = now.get(Calendar.DATE);

        Cursor cursor = jDatabase.getRandomRow();
        String _id = cursor.getString(cursor.getColumnIndex(jDatabase.KEY_ROW_ID));
        String title = cursor.getString(cursor.getColumnIndex(jDatabase.KEY_TITLE));
        String content = cursor.getString(cursor.getColumnIndex(jDatabase.KEY_CONTENT));
        String category = cursor.getString(cursor.getColumnIndex(jDatabase.KEY_CATEGORY));
        String favourite = cursor.getString(cursor.getColumnIndex(jDatabase.KEY_FAVOURITE));

        jDatabase.markAsRead(_id);
        cursor.close();
        jDatabase.close();

        boolean isFavourite = false;

        if(favourite.contentEquals("y"))
            isFavourite = true;


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(content);


        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);

        mBuilder.setAutoCancel(true);

        Intent jintent = new Intent(context, DisplayJoke.class);
        jintent.putExtra("jokeTitle",title);
        jintent.putExtra("_id", _id);
        jintent.putExtra("category", category);
        jintent.putExtra("isFavourite", isFavourite);
        jintent.putExtra("source", "AlarmReceiver");

        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, jintent, 0);
        //mBuilder.setContentIntent(pendingIntent);

        /*
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(new Intent(context, MainOptions.class));
        taskStackBuilder.addNextIntent(jintent);
        */

        //PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent pendingIntent =  PendingIntent.getActivity(context, 2, jintent,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(pendingIntent);

        Bundle bundle = intent.getExtras();
        int id = bundle.getInt("notificationId");
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, mBuilder.build());
        //mNotificationManager.notify(2, mBuilder.build());

        SplashScreen.setNotifications(context);
    }
}