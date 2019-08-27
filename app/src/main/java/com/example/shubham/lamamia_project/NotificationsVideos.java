package com.example.shubham.lamamia_project;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.URL;

import retrofit2.http.Url;

public class NotificationsVideos extends FirebaseMessagingService {

    private NotificationManager notifyManager;
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Data values
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String module_id = remoteMessage.getData().get("module_id");
        String order_id = remoteMessage.getData().get("order_id");
        String content_type = remoteMessage.getData().get("content_type");
        String video_id = remoteMessage.getData().get("video_id");
        String no_type = remoteMessage.getData().get("no_type");
        String image_url = remoteMessage.getData().get("image_url");
        String image_icon = remoteMessage.getData().get("icon");

        URL course_image_url;
        Bitmap image_bitmap = null;
        URL icon_url;
        Bitmap icon_bitmap = null;
        try{
            course_image_url = new URL(image_url);
            image_bitmap = BitmapFactory.decodeStream(course_image_url.openConnection().getInputStream());
            icon_url = new URL(image_icon);
            icon_bitmap = BitmapFactory.decodeStream(icon_url.openConnection().getInputStream());
        }catch (Exception e){
            e.printStackTrace();
        }

        final int NOTIFY_ID = 0; // ID of notification
        String channel_id = getApplicationContext().getString(R.string.default_notification_channel_id); // default_channel_id
        String channel_title = getApplicationContext().getString(R.string.default_notification_channel_title); // Default Channel
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        if (notifyManager == null) {
            notifyManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            assert notifyManager != null;
            NotificationChannel mChannel = notifyManager.getNotificationChannel(channel_id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(channel_id, channel_title, importance);
                mChannel.setLightColor(Color.YELLOW);
                mChannel.setVibrationPattern(new long[] {0,200,100,200});
                mChannel.enableVibration(true);
                mChannel.enableLights(true);
                notifyManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(getApplicationContext(), channel_id);

            // Notification type
            if (no_type.equals("1")){
                intent = new Intent(this, CourseModuleVideoActivity.class);
            } else if (no_type.equals("2")){
                intent = new Intent(this, UpdateApplicationActivity.class);
            } else {
                intent = new Intent(this, UpdateApplicationActivity.class);
            }// Notification type

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("module_id", module_id);
            intent.putExtra("order_id", order_id);
            intent.putExtra("content_type", content_type);
            intent.putExtra("video_id", video_id);
            // Create the TaskStackBuilder and add the intent, which inflates the back stack
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(intent);

            // Get the PendingIntent containing the entire back stack
            pendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentTitle(title)         // required
                    .setSmallIcon(R.drawable.logo_small)   // required
                    .setContentText(body)
                    .setContentIntent(pendingIntent)
                    .setColor(getResources().getColor(R.color.yellow, null))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setLights(Color.YELLOW, 2000, 2000)
                    .setAutoCancel(true)
                    .setLargeIcon(icon_bitmap)
                    .setStyle(new NotificationCompat.BigPictureStyle() .bigPicture(image_bitmap))
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[] {0,200,100,200});

        } else {

            builder = new NotificationCompat.Builder(getApplicationContext(), channel_id);

            // Notification type
            if (no_type.equals("1")){
                intent = new Intent(this, CourseModuleVideoActivity.class);
            } else if (no_type.equals("2")){
                intent = new Intent(this, UpdateApplicationActivity.class);
            } else {
                intent = new Intent(this, UpdateApplicationActivity.class);
            }// Notification type

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("module_id", module_id);
            intent.putExtra("order_id", order_id);
            intent.putExtra("content_type", content_type);
            intent.putExtra("video_id", video_id);
            // Create the TaskStackBuilder and add the intent, which inflates the back stack
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(intent);

            // Get the PendingIntent containing the entire back stack
            pendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentTitle(title)         // required
                    .setSmallIcon(R.drawable.logo_small)   // required
                    .setContentText(body)
                    .setContentIntent(pendingIntent)
                    .setColor(getResources().getColor(R.color.yellow))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setLights(Color.YELLOW, 2000, 2000)
                    .setAutoCancel(true)
                    .setLargeIcon(icon_bitmap)
                    .setStyle(new NotificationCompat.BigPictureStyle() .bigPicture(image_bitmap))
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[] {0,200,100,200});
        }
        Notification notification = builder.build();
        notifyManager.notify(NOTIFY_ID, notification);
    }
}