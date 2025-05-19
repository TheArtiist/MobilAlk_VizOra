package com.example.onlinevizoralejelentes;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class Notifications {
    private static final String CHANNEL_ID = "waterMeter_notification_channel";
    private final int NOTIFICATION_ID = 0;
    private NotificationManager mManager;
    private Context context;

    public Notifications(Context context){
        this.context = context;
        this.mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }

    private void createChannel(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                "WaterMeter Notification",
                NotificationManager.IMPORTANCE_DEFAULT);

        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.RED);
        channel.setDescription("Notification from SZTE_Viz");
        this.mManager.createNotificationChannel(channel);

    }

    public void send(String message){

        Intent intent = new Intent(context, LocationInfoActivity.class);
        PendingIntent pending = PendingIntent.getActivity(context,0,intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setContentTitle("SZTE_Viz")
                .setContentText(message)
                .setContentIntent(pending)
                .setSmallIcon(R.drawable.sztelogo);

        this.mManager.notify(NOTIFICATION_ID, builder.build());

    }

    public void cancel(){
        this.mManager.cancel(NOTIFICATION_ID);
    }


}
