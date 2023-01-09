package com.careplus.medtracker;

// #################################################################################################
// This is the fragment where cards of Hospitals are shown in RecyclerView
// #################################################################################################

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.careplus.medtracker.adapter.HospitalCardAdapter;
import com.careplus.medtracker.model.Hospital;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HospitalsFragment extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton fab;

    DatabaseReference databaseReference;
    HospitalCardAdapter adapter;
    ArrayList<Hospital> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_hospitals, container, false);
        recyclerView = myView.findViewById(R.id.recyclerView);
        fab = myView.findViewById(R.id.fab);

        databaseReference = FirebaseDatabase.getInstance().getReference("Hospital/Hospitals");
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();

        adapter = new HospitalCardAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

        // Pulling value from Firebase in alphabetically order of hospitalName
        Query query = databaseReference.orderByChild("hospitalName");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Hospital hospital = dataSnapshot.getValue(Hospital.class);
                    list.add(hospital);    // Adding all the Hospital objects (that are received from the Firebase) to the list
                }
                adapter.notifyDataSetChanged();     // Notifying adapter (guestCardAdpater) that the dataset has been updated
            }

            // Showing the error msg in the Toast if fetching data from Firebase is unsuccessful
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"" + error, Toast.LENGTH_SHORT).show();
            }
        });

        // On clicking "+" button, going to AddHospitalActivity
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddHospitalActivity.class));
            }
        });
        return myView;
    }
}