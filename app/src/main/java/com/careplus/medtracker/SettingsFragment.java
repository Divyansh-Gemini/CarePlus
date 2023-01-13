package com.careplus.medtracker;

// #################################################################################################
// Abhi idhr koi implementation nhi h
// #################################################################################################

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.careplus.medtracker.R;
import com.careplus.medtracker.model.ID;
import com.careplus.medtracker.model.MealsTime;
import com.careplus.medtracker.model.Medicine;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class SettingsFragment extends Fragment {
    EditText editText1, editText2, editText3;
    int breakfast_hour, breakfast_minute, lunch_hour, lunch_minute, dinner_hour, dinner_minute;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private MaterialTimePicker timePicker;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager2;
    private PendingIntent pendingIntent2;
    private AlarmManager alarmManager3;
    private PendingIntent pendingIntent3;
    private  Calendar calender;
    private  Calendar calender2;
    private  Calendar calender3;
    Button SetAlarm;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_settings, container, false);
        editText1 = myView.findViewById(R.id.editText1);
        editText2 = myView.findViewById(R.id.editText2);
        editText3 = myView.findViewById(R.id.editText3);
        SetAlarm=myView.findViewById(R.id.SetAlarm);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("MealsTime");
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
//                editText2.setText(lunch_hour+" : "+lunch_minute+SettingsFragment.covertTimeFormat(lunch_hour,lunch_minute));
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

                //editText2.setText(lunch_hour+" : "+lunch_minute+SettingsFragment.covertTimeFormat(lunch_hour,lunch_minute));
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
                        if(timePicker.getHour()>12){
                            editText1.setText(String.format((timePicker.getHour()-12)+" : "+String.format("%02d",timePicker.getMinute())+" PM"));
                        }
                        else{
                            editText1.setText(timePicker.getHour()+" : "+timePicker.getMinute()+" AM");
                        }
                        //Os Din Particular time Ko set Karva lega
                        calender= Calendar.getInstance();
                        calender.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                        calender.set(Calendar.MINUTE,timePicker.getMinute());
                        calender.set(Calendar.SECOND,0);
                        calender.set(Calendar.MILLISECOND,0);
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
                        if(timePicker.getHour()>12){
                            editText2.setText(String.format((timePicker.getHour()-12)+" : "+String.format("%02d",timePicker.getMinute())+" PM"));
                        }
                        else{
                            editText2.setText(timePicker.getHour()+" : "+timePicker.getMinute()+" AM");
                        }
                        //Os Din Particular time Ko set Karva lega
                        calender2= Calendar.getInstance();
                        calender2.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                        calender2.set(Calendar.MINUTE,timePicker.getMinute());
                        calender2.set(Calendar.SECOND,0);
                        calender2.set(Calendar.MILLISECOND,0);
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
                timePicker.show(requireFragmentManager(),"Careplus");
                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(timePicker.getHour()>12){
                            editText3.setText(String.format((timePicker.getHour()-12)+" : "+String.format("%02d",timePicker.getMinute())+" PM"));
                        }
                        else{
                            editText3.setText(timePicker.getHour()+" : "+timePicker.getMinute()+" AM");
                        }
                        //Os Din Particular time Ko set Karva lega
                        calender3= Calendar.getInstance();
                        calender3.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                        calender3.set(Calendar.MINUTE,timePicker.getMinute());
                        calender3.set(Calendar.SECOND,0);
                        calender3.set(Calendar.MILLISECOND,0);
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
        SetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlarmbreakfast();
                setAlarmlunch();
                setAlarmdinner();
            }
        });
        return myView;
    }

    private void setAlarmbreakfast() {
        alarmManager= (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(getActivity(),AlramReciever.class);
        pendingIntent=PendingIntent.getBroadcast(getActivity(),0,intent,0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calender.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,pendingIntent);
        Toast.makeText(getActivity(), "Alram Set SuccessFully for breakFastTime: "+calender.getTime(), Toast.LENGTH_SHORT).show();
    }
 private void setAlarmlunch() {
        alarmManager2= (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent2=new Intent(getActivity(),AlramReciever2.class);
         pendingIntent2=PendingIntent.getBroadcast(getActivity(),0,intent2,0);
        alarmManager2.setInexactRepeating(AlarmManager.RTC_WAKEUP,calender2.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,pendingIntent);
        Toast.makeText(getActivity(), "Alram Set SuccessFully for LunchTime: "+calender2.getTime(), Toast.LENGTH_SHORT).show();
    }
    private void setAlarmdinner() {
        alarmManager3= (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent3=new Intent(getActivity(),AlramReciever3.class);
        pendingIntent3=PendingIntent.getBroadcast(getActivity(),0,intent3,0);
        alarmManager3.setInexactRepeating(AlarmManager.RTC_WAKEUP,calender3.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,pendingIntent);
        Toast.makeText(getActivity(), "Alram Set SuccessFully for DinnerTime: "+calender3.getTime(), Toast.LENGTH_SHORT).show();
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
//       return ampm;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationMethod1() {
        //this Particular Code will Create the Notification Channel
            CharSequence name="CareplusReminderChannel";//name of the channel
            String description="channel for Alarm Manager";//For what we have create the notification
            int importance= NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel=new NotificationChannel("BreakFast",name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager=getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public  void createNotificationMethod2() {
        //this Particular Code will Create the Notification Channel
            CharSequence name="CareplusReminderChannel";//name of the channel
            String description="channel for Alarm Manager";//For what we have create the notification
            int importance= NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel=new NotificationChannel("Lunch",name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager=getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public  void createNotificationMethod3() {
        //this Particular Code will Create the Notification Channel
            CharSequence name="CareplusReminderChannel";//name of the channel
            String description="channel for Alarm Manager";//For what we have create the notification
            int importance= NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel=new NotificationChannel("Dinner",name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager=getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

    }

}