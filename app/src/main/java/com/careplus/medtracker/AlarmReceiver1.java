package com.careplus.medtracker;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver1 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("notification", "breakfast");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, i, PendingIntent.FLAG_IMMUTABLE);

        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        contentView.setTextViewText(R.id.textView1, "Breakfast Time");
        contentView.setTextViewText(R.id.textView2, "Give medicine to guests");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "BreakFast")
                .setSmallIcon(R.drawable.careplus_logo_transparent_bg)  //icon which going to be Shown When notification is Displayed
                .setContentTitle("Breakfast time")                      //Title of the Notification
                .setContentText("Give medicine to guests")              //Description of the Notification
                .setAutoCancel(true)                                    //user press on notification it will Cancel
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)                        //se hm aapni Maan ki activity par jaa skte hai
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)           //Iska mtlb Kesa chate ho Mtlb high priority par rahe ya Kaise mtlb ki Screen Par Dikhe ya pop up ho pass
                .setContent(contentView);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123, builder.build());     //this will show The Notification
    }
}