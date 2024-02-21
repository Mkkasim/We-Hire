package com.mk.wehire.User.models;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.mk.wehire.Company.fragments.ApplicantsFragment;
import com.mk.wehire.R;
import com.mk.wehire.SplashActivity;
import com.mk.wehire.User.fragments.ApplicationsFragment;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    NotificationManager mNotificationManager;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        try {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TAG2", "onMessageReceived: " + e.toString());
        }



        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
//                .build();
//        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//        r.play();
//
//        SahilRingtone sr = new SahilRingtone(r,notification);
//        SahilRingtone.play();


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            r.setLooping(false);
//        }
//
//        // vibration
//        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//        long[] pattern = {100, 300, 300, 300};
//        v.vibrate(pattern, -1);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "com.mk.wehire.ANDROID");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder.setSmallIcon(R.drawable.icontrans);
            builder.setSmallIcon(R.drawable.wehire_logo);
        } else {
//            builder.setSmallIcon(R.drawable.icon_kritikar);
            builder.setSmallIcon(R.drawable.wehire_logo);
        }

        if(remoteMessage.getData().get("click_action").equals("application"))
        {
            Log.d("notichat","chat");
            Intent resultIntent = new Intent(this, SplashActivity.class);
            resultIntent.putExtra("notiFor","application");
            PendingIntent pendingIntent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity
                        (this, 0, resultIntent, PendingIntent.FLAG_MUTABLE);
            }
            else
            {
                pendingIntent = PendingIntent.getActivity
                        (this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            }



            builder.setContentTitle(remoteMessage.getData().get("title"));
            builder.setContentText(remoteMessage.getData().get("body"));
            builder.setVibrate(new long[]{0, 500, 1000});
            builder.setSound(notification);
            builder.setContentIntent(pendingIntent);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("body")));
            builder.setAutoCancel(true);
            builder.setPriority(Notification.PRIORITY_MAX);
        }else if(remoteMessage.getData().get("click_action").equals("status"))
        {
            Log.d("notichat","chat");
            Intent resultIntent = new Intent(this, SplashActivity.class);
            resultIntent.putExtra("notiFor","status");

            PendingIntent pendingIntent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity
                        (this, 0, resultIntent, PendingIntent.FLAG_MUTABLE);
            }
            else
            {
                pendingIntent = PendingIntent.getActivity
                        (this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            }



            builder.setContentTitle(remoteMessage.getData().get("title"));
            builder.setContentText(remoteMessage.getData().get("body"));
            builder.setVibrate(new long[]{0, 500, 1000});
            builder.setSound(notification);
            builder.setContentIntent(pendingIntent);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("body")));
            builder.setAutoCancel(true);
            builder.setPriority(Notification.PRIORITY_MAX);
        }else
        {
            Intent resultIntent = new Intent(this, SplashActivity.class);
            PendingIntent pendingIntent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity
                        (this, 0, resultIntent, PendingIntent.FLAG_MUTABLE);
            }
            else
            {
                pendingIntent = PendingIntent.getActivity
                        (this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            }
            //PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentTitle(remoteMessage.getData().get("title"));
            builder.setContentText(remoteMessage.getData().get("body"));
            builder.setSound(notification);
            builder.setContentIntent(pendingIntent);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("body")));
            builder.setAutoCancel(true);
            builder.setPriority(Notification.PRIORITY_MAX);
        }


        mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "com.mk.wehire.ANDROID";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);

            builder.setChannelId(channelId);
        }

        try {
            mNotificationManager.notify(100, builder.build());
        }
        catch(Exception ex)
        {

        }



    }

}
