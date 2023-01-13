package com.careplus.medtracker;

// #################################################################################################
// Abhi idhr koi implementation nhi h
// #################################################################################################

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.careplus.medtracker.R;
import com.careplus.medtracker.model.ID;
import com.careplus.medtracker.model.MealsTime;
import com.careplus.medtracker.model.Medicine;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
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

    MaterialTimePicker materialTimePicker1, materialTimePicker2, materialTimePicker3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_settings, container, false);
        editText1 = myView.findViewById(R.id.editText1);
        editText2 = myView.findViewById(R.id.editText2);
        editText3 = myView.findViewById(R.id.editText3);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("MealsTime");
        Calendar ca = Calendar.getInstance();

        materialTimePicker1 = new MaterialTimePicker.Builder()
                .setTitleText("Breakfast Time")
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(breakfast_hour)
                .setMinute(breakfast_minute)
                .build();

        materialTimePicker2 = new MaterialTimePicker.Builder()
                .setTitleText("Lunch Time")
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(lunch_hour)
                .setMinute(lunch_minute)
                .build();

        materialTimePicker3 = new MaterialTimePicker.Builder()
                .setTitleText("Dinner Time")
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(dinner_hour)
                .setMinute(dinner_minute)
                .build();

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
//                materialTimePicker1.setHour(breakfast_hour);
//                materialTimePicker1.setMinute(breakfast_minute);
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
//                materialTimePicker2.setHour(lunch_hour);
//                materialTimePicker2.setMinute(lunch_minute);
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
//                materialTimePicker3.setHour(dinner_hour);
//                materialTimePicker3.setMinute(dinner_minute);
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
                materialTimePicker1.show(requireFragmentManager(), "fragment_tag");
            }
        });

        materialTimePicker1.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                breakfast_hour = materialTimePicker1.getHour();
                breakfast_minute = materialTimePicker1.getMinute();

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
                editText1.setText(covertTimeFormat(breakfast_hour, breakfast_minute));
            }
        });

        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialTimePicker2.show(requireFragmentManager(), "fragment_tag");
            }
        });

        materialTimePicker2.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lunch_hour = materialTimePicker2.getHour();
                lunch_minute = materialTimePicker2.getMinute();

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
                editText2.setText(covertTimeFormat(lunch_hour, lunch_minute));
            }
        });

        editText3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialTimePicker3.show(requireFragmentManager(), "fragment_tag");
            }
        });

        materialTimePicker3.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dinner_hour = materialTimePicker3.getHour();
                dinner_minute = materialTimePicker3.getMinute();

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
                editText3.setText(covertTimeFormat(dinner_hour, dinner_minute));
            }
        });
        return myView;
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
}