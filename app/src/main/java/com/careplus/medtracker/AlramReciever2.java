package com.careplus.medtracker;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlramReciever2 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context,SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,i,0);
        NotificationCompat.Builder builder =new NotificationCompat.Builder(context,"Lunch")
                .setSmallIcon(R.drawable.ic_launcher_background)//icon which going to be Shown When notification is Displayed
                .setContentTitle("Lunch Medicine Time")//Title of the Notification
                .setContentText("Give Medicines")//Discription of the Notification
                .setAutoCancel(true)//user press on notificaation it will Cancle
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent) //se haam aapni Maan ki activity par jaa skte hai
                .setPriority(NotificationCompat.PRIORITY_MAX);//Eska mtlb Kaisa chate ho Mtlb high priority par rahe ya Kaise mtlb ki Screen Par Dikhe ya pop up ho pass
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123,builder.build());//this will show The Notification

    }
}
