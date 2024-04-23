package com.careplus.medtracker;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.careplus.medtracker.model.MealsTime;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class SettingsFragment extends Fragment {
    EditText editText1, editText2, editText3;
    Button btn_setAlarm, btn_logout;
    int breakfast_hour, breakfast_minute, lunch_hour, lunch_minute, dinner_hour, dinner_minute;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private MaterialTimePicker timePicker;
    private Calendar calender1;
    private Calendar calender2;
    private Calendar calender3;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_settings, container, false);
        editText1 = myView.findViewById(R.id.editText1);
        editText2 = myView.findViewById(R.id.editText2);
        editText3 = myView.findViewById(R.id.editText3);
        btn_setAlarm =myView.findViewById(R.id.SetAlarm);
        btn_logout =myView.findViewById(R.id.button2);


        pref = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = pref.edit();
        String old_age_home_name = pref.getString("old_age_home_name", "");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(old_age_home_name + "/MealsTime");
        
        Calendar ca = Calendar.getInstance();
        createNotificationMethod1();
        createNotificationMethod2();
        createNotificationMethod3();
        
        databaseReference.child("breakfast_time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MealsTime mealsTime = dataSnapshot.getValue(MealsTime.class);
                if (mealsTime == null) {
                    breakfast_hour = ca.get(Calendar.HOUR_OF_DAY);
                    breakfast_minute = ca.get(Calendar.MINUTE);
                }
                else {
                    breakfast_hour = mealsTime.getMeal_hour();
                    breakfast_minute = mealsTime.getMeal_minute();
                }
                editText1.setText(covertTimeFormat(breakfast_hour, breakfast_minute));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        databaseReference.child("lunch_time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MealsTime mealsTime = dataSnapshot.getValue(MealsTime.class);
                if (mealsTime == null) {
                    lunch_hour = ca.get(Calendar.HOUR_OF_DAY);
                    lunch_minute = ca.get(Calendar.MINUTE);
                }
                else {
                    lunch_hour = mealsTime.getMeal_hour();
                    lunch_minute = mealsTime.getMeal_minute();
                }
                editText2.setText(covertTimeFormat(lunch_hour, lunch_minute));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        databaseReference.child("dinner_time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MealsTime mealsTime = dataSnapshot.getValue(MealsTime.class);
                if (mealsTime == null) {
                    dinner_hour = ca.get(Calendar.HOUR_OF_DAY);
                    dinner_minute = ca.get(Calendar.MINUTE);
                }
                else {
                    dinner_hour = mealsTime.getMeal_hour();
                    dinner_minute = mealsTime.getMeal_minute();
                }
                editText3.setText(covertTimeFormat(dinner_hour, dinner_minute));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        calender1 = Calendar.getInstance();
        calender1.set(Calendar.HOUR_OF_DAY, breakfast_hour);
        calender1.set(Calendar.MINUTE, breakfast_minute);
        calender1.set(Calendar.SECOND, 0);
        calender1.set(Calendar.MILLISECOND, 0);

        calender2 = Calendar.getInstance();
        calender2.set(Calendar.HOUR_OF_DAY, lunch_hour);
        calender2.set(Calendar.MINUTE, lunch_minute);
        calender2.set(Calendar.SECOND, 0);
        calender2.set(Calendar.MILLISECOND, 0);

        calender3 = Calendar.getInstance();
        calender3.set(Calendar.HOUR_OF_DAY, dinner_hour);
        calender3.set(Calendar.MINUTE, dinner_minute);
        calender3.set(Calendar.SECOND, 0);
        calender3.set(Calendar.MILLISECOND, 0);

        editText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker=new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(breakfast_hour)
                        .setMinute(breakfast_minute)
                        .setTitleText("Time For medicines")
                        .build();
                timePicker.show(requireFragmentManager(),"Careplus");
                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editText1.setText(SettingsFragment.covertTimeFormat(timePicker.getHour(), timePicker.getMinute()));
                        //Os Din Particular time Ko set Karva lega
                        calender1.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                        calender1.set(Calendar.MINUTE,timePicker.getMinute());
                        breakfast_hour = timePicker.getHour();
                        breakfast_minute = timePicker.getMinute();
                        MealsTime mealsTime = new MealsTime(breakfast_hour, breakfast_minute);
                        databaseReference.child("breakfast_time").setValue(mealsTime).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused)
                            {   }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }
        });

        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker=new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(lunch_hour)
                        .setMinute(lunch_minute)
                        .setTitleText("Time For medicines")
                        .build();
                timePicker.show(requireFragmentManager(),"Careplus");
                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editText2.setText(SettingsFragment.covertTimeFormat(timePicker.getHour(), timePicker.getMinute()));
                        //Os Din Particular time Ko set Karva lega
                        calender2.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                        calender2.set(Calendar.MINUTE,timePicker.getMinute());
                        lunch_hour = timePicker.getHour();
                        lunch_minute = timePicker.getMinute();
                        MealsTime mealsTime = new MealsTime(lunch_hour, lunch_minute);
                        databaseReference.child("lunch_time").setValue(mealsTime).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused)
                            {   }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        editText3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker=new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(dinner_hour)
                        .setMinute(dinner_minute)
                        .setTitleText("Time For medicines")
                        .build();
                timePicker.show(requireFragmentManager(),"CarePlus");
                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editText3.setText(SettingsFragment.covertTimeFormat(timePicker.getHour(), timePicker.getMinute()));
                        //Os Din Particular time Ko set Karva lega
                        calender3.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                        calender3.set(Calendar.MINUTE,timePicker.getMinute());
                        dinner_hour = timePicker.getHour();
                        dinner_minute = timePicker.getMinute();
                        MealsTime mealsTime = new MealsTime(dinner_hour, dinner_minute);
                        databaseReference.child("dinner_time").setValue(mealsTime).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused)
                            {   }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        btn_setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    setAlarm();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // clearing app data
                    Runtime runtime = Runtime.getRuntime();
                    runtime.exec("pm clear com.careplus.medtracker");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();
            }
        });

        return myView;
    }

    private void setAlarm() {
        AlarmManager alarmManager1 = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        AlarmManager alarmManager2 = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        AlarmManager alarmManager3 = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent1 = new Intent(getActivity(), AlarmReceiver1.class);
        Intent intent2 = new Intent(getActivity(), AlarmReceiver2.class);
        Intent intent3 = new Intent(getActivity(), AlarmReceiver3.class);

        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getActivity(), 1, intent1, PendingIntent.FLAG_MUTABLE);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getActivity(), 2, intent2, PendingIntent.FLAG_MUTABLE);
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(getActivity(), 3, intent3, PendingIntent.FLAG_MUTABLE);

        alarmManager1.setExact(AlarmManager.RTC_WAKEUP, calender1.getTimeInMillis(), pendingIntent1);
        alarmManager2.setExact(AlarmManager.RTC_WAKEUP, calender2.getTimeInMillis(), pendingIntent2);
        alarmManager3.setExact(AlarmManager.RTC_WAKEUP, calender3.getTimeInMillis(), pendingIntent3);

        Toast.makeText(getActivity(), "Alarm set successfully", Toast.LENGTH_SHORT).show();
    }

    public static String covertTimeFormat(int hours, int minutes)
    {
        String ampm = "PM";
        String time;

        if (hours > 12)
            hours -= 12;
        else
            ampm = "AM";

        if (minutes < 10)
            time = hours + ":0" + minutes + " " + ampm;
        else
            time = hours + ":" + minutes + " " + ampm;

        return time;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationMethod1() {
        // This Particular Code will Create the Notification Channel
        CharSequence name = "CareplusReminderChannel";            // Name of the channel
        String description = "channel for Alarm Manager";         // For what we have create the notification
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel=new NotificationChannel("BreakFast", name, importance);
        channel.setDescription(description);
        channel.enableLights(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            channel.canBubble();
        channel.canBypassDnd();
        channel.enableVibration(true);
        channel.getLockscreenVisibility();
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        channel.enableLights(true);
        NotificationManager notificationManager=getContext().getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public  void createNotificationMethod2() {
        //this Particular Code will Create the Notification Channel
        CharSequence name = "CareplusRemindrChannel";             //name of the channel
        String description = "channel for Alarm Manager";         //For what we have create the notification
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("Lunch", name, importance);
        channel.setDescription(description);
        channel.enableLights(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            channel.canBubble();
        channel.canBypassDnd();
        channel.enableVibration(true);
        channel.getLockscreenVisibility();
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        channel.enableLights(true);
        NotificationManager notificationManager=getContext().getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public  void createNotificationMethod3() {
        // This Particular Code will Create the Notification Channel
        CharSequence name = "CareplusReminderChannel";            // Name of the channel
        String description = "channel for Alarm Manager";         // For what we have create the notification
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("Dinner", name, importance);
        channel.setDescription(description);
        channel.enableLights(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            channel.canBubble();
        channel.canBypassDnd();
        channel.enableVibration(true);
        channel.getLockscreenVisibility();
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        channel.enableLights(true);
        NotificationManager notificationManager=getContext().getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}