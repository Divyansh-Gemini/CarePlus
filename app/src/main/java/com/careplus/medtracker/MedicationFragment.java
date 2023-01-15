package com.careplus.medtracker;

// #################################################################################################
// Abhi idhr koi implementation nhi h
// #################################################################################################

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.careplus.medtracker.adapter.MedicationCardAdapter;
import com.careplus.medtracker.model.Medication;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class MedicationFragment extends Fragment {
    TextView textView;
    int dd, mm, yyyy;
    String date = "";
    String month, month_date;

    RecyclerView recyclerView;
    FloatingActionButton fab;

    SharedPreferences pref;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    MedicationCardAdapter adapter;
    ArrayList<Medication> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_medication, container, false);
        recyclerView = myView.findViewById(R.id.recyclerView);
        fab = myView.findViewById(R.id.fab);
        textView = myView.findViewById(R.id.textView);

        pref = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String old_age_home_name = pref.getString("old_age_home_name", "");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(old_age_home_name + "/Medication/Medications");
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();

        adapter = new MedicationCardAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

        // Getting today's date & setting it to the textView
        Calendar ca = Calendar.getInstance();
        yyyy = ca.get(Calendar.YEAR);
        mm = ca.get(Calendar.MONTH);
        dd = ca.get(Calendar.DATE);
        String[] months = getResources().getStringArray(R.array.months);
        textView.setText(dd + " " + months[mm] + " " + yyyy);

        // Getting date & month as String
        if (dd < 10)
            date = "0";
        date += dd;
        month = months[mm].substring(0,3);
        month_date = month + " " + date;

        Intent intent = getActivity().getIntent();
        Bundle b = intent.getExtras();
        String filter_meal = "";
        if (b != null) {
            filter_meal = b.getString("notification");
        }

        // Pulling value from Firebase in alphabetically order of medicationName
        String finalFilter_meal = filter_meal;
        databaseReference.orderByChild("medicationName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Medication medication = dataSnapshot.getValue(Medication.class);
                    switch (finalFilter_meal) {
                        case "breakfast":
                            if (medication.getSchedule().split(" ")[1].equals("Breakfast")) {
                                List dates = new ArrayList(medication.getDatesAndStatus().keySet());
                                Map<String, Boolean> dates_and_status = medication.getDatesAndStatus();

                                for (int i = 0; i < dates.size(); i++) {
                                    if (dates.get(i).equals(month_date) && dates_and_status.get(dates.get(i)) == false) {
                                        list.add(medication);    // Adding all the Medication objects (that are received from the Firebase) to the list
                                    }
                                }
                            }
                            break;
                        case "lunch":
                            if (medication.getSchedule().split(" ")[1].equals("Lunch")) {
                                List dates = new ArrayList(medication.getDatesAndStatus().keySet());
                                Map<String, Boolean> dates_and_status = medication.getDatesAndStatus();

                                for (int i = 0; i < dates.size(); i++) {
                                    if (dates.get(i).equals(month_date) && dates_and_status.get(dates.get(i)) == false) {
                                        list.add(medication);    // Adding all the Medication objects (that are received from the Firebase) to the list
                                    }
                                }
                            }
                            break;
                        case "dinner":
                            if (medication.getSchedule().split(" ")[1].equals("Dinner")) {
                                List dates = new ArrayList(medication.getDatesAndStatus().keySet());
                                Map<String, Boolean> dates_and_status = medication.getDatesAndStatus();

                                for (int i = 0; i < dates.size(); i++) {
                                    if (dates.get(i).equals(month_date) && dates_and_status.get(dates.get(i)) == false) {
                                        list.add(medication);    // Adding all the Medication objects (that are received from the Firebase) to the list
                                    }
                                }
                            }
                            break;
                        default:
                            List dates = new ArrayList(medication.getDatesAndStatus().keySet());
                            Map<String, Boolean> dates_and_status = medication.getDatesAndStatus();

                            for (int i = 0; i < dates.size(); i++) {
                                if (dates.get(i).equals(month_date) && dates_and_status.get(dates.get(i)) == false) {
                                    list.add(medication);    // Adding all the Medication objects (that are received from the Firebase) to the list
                                }
                            }
                    }
                }
                adapter.notifyDataSetChanged();     // Notifying adapter (medicationCardAdapter) that the dataset has been updated
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddMedicationActivity.class));
            }
        });
        return myView;
    }
}