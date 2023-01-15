package com.careplus.medtracker;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlramReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context,SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, i, PendingIntent.FLAG_MUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "BreakFast")
                .setSmallIcon(R.drawable.ic_launcher_background)    //icon which going to be Shown When notification is Displayed
                .setContentTitle("BreakFast Medicine Time") //Title of the Notification
                .setContentText("Give Medicines")   //Description of the Notification
                .setAutoCancel(true)    //user press on notification it will Cancel
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)    //se hm aapni Maan ki activity par jaa skte hai
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_MAX);  //Iska mtlb Kesa chate ho Mtlb high priority par rahe ya Kaise mtlb ki Screen Par Dikhe ya pop up ho pass
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123, builder.build());//this will show The Notification
    }
}