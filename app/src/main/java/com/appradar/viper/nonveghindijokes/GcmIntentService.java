package com.appradar.viper.nonveghindijokes;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by viper on 17/5/15.
 */
public class GcmIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    JokesDatabase jDatabase;
    private static final String TAG = "GcmIntentService";

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("111111", "Send error: " + extras.toString(), "test");
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("222222", "Deleted messages on server: " +
                        extras.toString(), "test");
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                String title = extras.getString("title");
                String body = extras.getString("body");
                String category = extras.getString("category");
                sendNotification(title, body, category);
                Log.i(TAG, "Received: " + extras.toString());

                addToDatabase(title, body, category);
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void addToDatabase(String title, String body, String category){
        jDatabase = new JokesDatabase(MainOptions.getContext());
        jDatabase.open();

        jDatabase.insertEntry(title, body, category, "n", "n");

    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String title, String msg, String category) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        String content = msg;
        String _id = "0000";
        boolean isFavourite = false;

        Intent jintent = new Intent(this, DisplayJoke.class);
        jintent.putExtra("jokeTitle", title);
        jintent.putExtra("content", content);
        jintent.putExtra("_id", _id);
        jintent.putExtra("category", category);
        jintent.putExtra("isFavourite", isFavourite);
        jintent.putExtra("source", "GcmIntentService");

        PendingIntent contentIntent = PendingIntent.getActivity(this, 2, jintent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg)
                        .setAutoCancel(true);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        SplashScreen.setNotifications(this);
    }
}