package com.example.amarjeet.hola;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Amarjeet on 18-04-2018.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{
    private static final String TAG = "Firebasemessaging";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG,"MessagingServiceStarted");

        String messageTitle = remoteMessage.getNotification().getTitle();
        String messageBody = remoteMessage.getNotification().getBody();
        String click_action = remoteMessage.getNotification().getClickAction();

        Log.d(TAG, click_action);

        String message = remoteMessage.getData().get("message");
        String sender = remoteMessage.getData().get("sender_id");
        String name = remoteMessage.getData().get("from_name");
        String phno = remoteMessage.getData().get("ph_no");

        Log.d(TAG,"messaeTitle: " + messageTitle + " messageBody:" + messageBody + " click_action:" + click_action + " message:" + message + " sender:" + sender);

        NotificationCompat.Builder mBuilder= new NotificationCompat.Builder(this,getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messageTitle)
                .setContentText(message);

        Intent resultIntent = new Intent(click_action);
        resultIntent.putExtra("message",message);
        resultIntent.putExtra("sender_id",sender);
        resultIntent.putExtra("name",name);
        resultIntent.putExtra("phno",phno);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);

        int mNotifId = (int) System.currentTimeMillis();

        NotificationManager mNotifMngr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mNotifMngr.notify(mNotifId, mBuilder.build());


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //set up the broadcast
        Log.d(TAG,"MessagingServiceDestoryed");
    }
}
























