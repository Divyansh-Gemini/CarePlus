package com.careplus.medtracker;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlramReciever3 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent=PendingIntent.getActivity(context, 3, i, PendingIntent.FLAG_MUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"Dinner")
                .setSmallIcon(R.drawable.ic_launcher_background)    // Icon which going to be Shown When notification is Displayed
                .setContentTitle("Dinner Medicine Time")            // Title of the Notification
                .setContentText("Give Medicines")                   // Description of the Notification
                .setAutoCancel(true)                                // User press on notification it will Cancel
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)                    // se hm apni Maan ki activity par jaa skte hai
                .setPriority(NotificationCompat.PRIORITY_MAX);      // Iska mtlb Kaisa chahte ho mtlb high priority par rahe ya Kaise mtlb ki Screen Par Dikhe ya pop up ho pass
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(125, builder.build());  // This will show The Notification
    }
}