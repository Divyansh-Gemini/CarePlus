package com.careplus.medtracker;

// #################################################################################################
// This is the fragment where cards of Medicines are shown in RecyclerView
// #################################################################################################

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.careplus.medtracker.adapter.GuestCardAdapter;
import com.careplus.medtracker.adapter.MedicineCardAdapter;
import com.careplus.medtracker.model.Guest;
import com.careplus.medtracker.model.Medicine;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MedicinesFragment extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton fab;

    SharedPreferences pref;
    DatabaseReference databaseReference;
    MedicineCardAdapter adapter;
    ArrayList<Medicine> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_medicines, container, false);
        recyclerView = myView.findViewById(R.id.recyclerView);
        fab = myView.findViewById(R.id.fab);

        pref = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String old_age_home_name = pref.getString("old_age_home_name", "");
        databaseReference = FirebaseDatabase.getInstance().getReference(old_age_home_name + "/Medicine/Medicines");
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();

        adapter = new MedicineCardAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

        // Pulling value from Firebase in alphabetically order of medicineName
        Query query = databaseReference.orderByChild("medicineName");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Medicine medicine = dataSnapshot.getValue(Medicine.class);
                    list.add(medicine);    // Adding all the Medicine objects (that are received from the Firebase) to the list
                }
                adapter.notifyDataSetChanged();     // Notifying adapter (guestCardAdpater) that the dataset has been updated
            }

            // Showing the error msg in the Toast if fetching data from Firebase is unsuccessful
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // On clicking "+" button, going to AddMedicineActivity
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddMedicineActivity.class));
            }
        });
        return myView;
    }
}