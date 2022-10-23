package com.yashverma.oldeage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yashverma.oldeage.adapter.Adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Queue;

public class GuestData extends AppCompatActivity implements Adapter.MyViewHolder.OnRecyclierViewClick {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    Adapter adapter;
    Button AddGuest;
    ArrayList<User> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_data);
        recyclerView =findViewById(R.id.GuestData);
        databaseReference = FirebaseDatabase.getInstance().getReference("Guest Info");
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<User>();
        adapter=new Adapter(this,list);
        recyclerView.setAdapter(adapter);
        AddGuest=findViewById(R.id.AddG);
        //Pulling value in sorted form
        Query query=databaseReference.orderByChild("guestName");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    User user=dataSnapshot.getValue(User.class);
                    list.add(user);
                    Toast.makeText(GuestData.this, "In recycler View", Toast.LENGTH_SHORT).show();
                }
                adapter=new Adapter(GuestData.this,list);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GuestData.this, " "+error, Toast.LENGTH_SHORT).show();

            }
        });

        AddGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(GuestData.this,Blank.class);

                startActivity(i);

            }

        });


    }

    @Override
    public void Onclick(int position) {

    }
}