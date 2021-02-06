package com.uniquedatacom.i_permit_res.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.activities.SecurityNavActivity;

public class FCMService extends FirebaseMessagingService {
    String TAG = "FCMService";
    public static String Token;
    private int mStatusCode = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
                Log.d(TAG, "onMessageReceived: " + remoteMessage.getData());
            } else {
                // Handle message within 10 seconds
//                handleNow();
                Log.d(TAG, "handle " + remoteMessage.getData());
            }
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.mipmap.ic_launcher);
//            sendNotification(icon, remoteMessage.getNotification().getBody());
            sendMyNotification(remoteMessage.getNotification().getBody());
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @Override
    public void onNewToken(String s) {
        Log.d(TAG, "Refreshed token: " + s);
        int maxLogSize = 1000;
        for (int i = 0; i <= s.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > s.length() ? s.length() : end;
            Log.v(TAG + "token", s.substring(start, end));
        }
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Token", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Dev_Token", s);
        editor.apply();
        editor.commit();
        sendRegistrationToServer(s);
        Token = s;
    }

    /**
     * @param token
     */
    public void sendRegistrationToServer(String token) {
        //SendDeviceTokenToServer(token);
    }

//    private void sendNotification(Bitmap bitmap, String message) {
//
//        Intent contentIntent = new Intent(this, NotificationsListScreen.class);
//        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
//        style.bigPicture(bitmap);
//
//        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
////        Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.correct_answer);
//        Intent intent = new Intent(getApplicationContext(), SecurityNavActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, contentIntent,0);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        String NOTIFICATION_CHANNEL_ID = "101";
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            @SuppressLint("WrongConstant")
//            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);
////Configure Notification Channel
//            notificationChannel.setDescription("Game Notifications");
//            notificationChannel.enableLights(true);
//            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//            notificationChannel.enableVibration(true);
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("T-Permit")
//                .setAutoCancel(true)
//                .setSound(defaultSound)
//                .setContentText(message)
//                .setContentIntent(pendingIntent)
//                .setStyle(style)
//                // .setLargeIcon(bitmap)
//                .setWhen(System.currentTimeMillis())
//                .setPriority(Notification.PRIORITY_MAX);
//        notificationManager.notify(1, notificationBuilder.build());
//    }



    private void sendNotification(Bitmap bitmap, String message)
    {
//        NotificationCompat.MessagingStyle style = new NotificationCompat.MessagingStyle();
//        style(bitmap);

//        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.notification_ring);  //Here is FILE_NAME is the name of file that you want to play

        Intent intent = new Intent(getApplicationContext(), SecurityNavActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
//Configure Notification Channel
            notificationChannel.setDescription("Game Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationChannel.setSound(sound, attributes); // This is IMPORTANT
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("I-Permit Residential")
                .setAutoCancel(true)
//                .setSound(sound)
                .setContentText(message)
                .setContentIntent(pendingIntent)
//                .setStyle(style)
                // .setLargeIcon(bitmap)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX);
        notificationManager.notify(1, notificationBuilder.build());


    }


    private void sendMyNotification(String message) {

        Intent intent = new Intent(this, SecurityNavActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.notification_ring);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "CH_ID")
                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            if(soundUri != null){
                // Changing Default mode of notification
                notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
                // Creating an Audio Attribute
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build();

                // Creating Channel
                NotificationChannel notificationChannel = new NotificationChannel("CH_ID","Testing_Audio",NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setSound(soundUri,audioAttributes);
                mNotificationManager.createNotificationChannel(notificationChannel);
            }
        }
        mNotificationManager.notify(0, notificationBuilder.build());
    }
}