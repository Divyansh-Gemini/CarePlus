package com.careplus.medtracker;

// #################################################################################################
// Abhi idhr koi implementation nhi h
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
import com.careplus.medtracker.adapter.HospitalizationCardAdapter;
import com.careplus.medtracker.model.Hospital;
import com.careplus.medtracker.model.Hospitalization;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HospitalizationFragment extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton fab;

    DatabaseReference databaseReference;
    HospitalizationCardAdapter adapter;
    ArrayList<Hospitalization> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_hospitalization, container, false);
        recyclerView = myView.findViewById(R.id.recyclerView);
        fab = myView.findViewById(R.id.fab);

        databaseReference = FirebaseDatabase.getInstance().getReference("Hospitalization/Hospitalizations");
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();

        adapter = new HospitalizationCardAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

        // Pulling value from Firebase in alphabetically order of hospitalizationName
        databaseReference.orderByChild("hospitalizationName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Hospitalization hospitalization = dataSnapshot.getValue(Hospitalization.class);
                    list.add(hospitalization);    // Adding all the Hospitalization objects (that are received from the Firebase) to the list
                }
                adapter.notifyDataSetChanged();     // Notifying adapter (hospitalizationCardAdapter) that the dataset has been updated
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // On clicking "+" button, going to AddHospitalizationActivity
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddHospitalizationActivity.class));
            }
        });
        return myView;
    }
}