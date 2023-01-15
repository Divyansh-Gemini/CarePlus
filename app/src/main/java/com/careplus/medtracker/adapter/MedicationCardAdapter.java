package com.careplus.medtracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.careplus.medtracker.AddMedicationActivity;
import com.careplus.medtracker.R;
import com.careplus.medtracker.model.Guest;
import com.careplus.medtracker.model.Medicine;
import com.careplus.medtracker.model.Medication;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

// #################################################################################################
// Adapter is something that joins 2 things. This class is a link b/w data & RecyclerView.
// The data that is visible in the cards on Guest, Medicine, and Medicine pages is with the help of this adapter.
// Implementation "Edit" & "More" button is also here.
// #################################################################################################

public class MedicationCardAdapter extends RecyclerView.Adapter<MedicationCardAdapter.MedicationViewHolder> {
    Context context;
    ArrayList<Medication> medication_list;

    SharedPreferences pref_todays_date;
    SharedPreferences pref_login;
    SharedPreferences.Editor editor;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceGuest, databaseReferenceMedicine, databaseReferenceMedication;

    public MedicationCardAdapter(Context context, ArrayList<Medication> medication_list) {
        this.context = context;
        this.medication_list = medication_list;

        // for SharedPreference
        pref_todays_date = context.getSharedPreferences("todays_date", Context.MODE_PRIVATE);
        editor = pref_todays_date.edit();
        pref_login = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        String old_age_home_name = pref_login.getString("old_age_home_name", "");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceGuest = firebaseDatabase.getReference(old_age_home_name + "/Guest/Guests");
        databaseReferenceMedicine = firebaseDatabase.getReference(old_age_home_name + "/Medicine/Medicines");
        databaseReferenceMedication = firebaseDatabase.getReference(old_age_home_name + "/Medication");
    }

    // MedicationViewHolder is inner class & MedicationCardAdapter is outer class
    public static class MedicationViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3;
        ImageView imageView;
        MaterialButton btn_taken, btn_edit, btn_more;

        int dd, mm;
        String date = "";
        String month, month_date;

        public MedicationViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            imageView = itemView.findViewById(R.id.imageView);
            btn_taken = itemView.findViewById(R.id.button);
            btn_edit = itemView.findViewById(R.id.button1);
            btn_more = itemView.findViewById(R.id.button2);
        }
    }

    @NonNull
    @Override
    public MedicationCardAdapter.MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_medication_card, parent, false);
        return new MedicationCardAdapter.MedicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationCardAdapter.MedicationViewHolder holder, int position) {
        Medication medication = medication_list.get(position);

        // Getting guestName from firebase using guestID which we got from medication object and setting it to the textView
        databaseReferenceGuest.child(Integer.toString(medication.getGuest_id())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Guest guest = dataSnapshot.getValue(Guest.class);
                holder.textView1.setText(guest.getGuestName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Getting medicineName from firebase using medicineID which we got from medication object and setting it to the textView
        databaseReferenceMedicine.child(Integer.toString(medication.getMedicine_id())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Medicine medicine = dataSnapshot.getValue(Medicine.class);
                holder.textView2.setText("Medicine: " + medicine.getMedicineName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.textView3.setText(medication.getSchedule());
        holder.imageView.setImageResource(R.drawable.old_man_avatar);

        holder.month_date = pref_todays_date.getString("month_date", "0");

        //##################### Taken Button #####################
        holder.btn_taken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Taken", Toast.LENGTH_SHORT).show();
                int medication_id = medication.getMedication_id();
                int guest_id = medication.getGuest_id();
                int medicine_id = medication.getMedicine_id();
                String schedule = medication.getSchedule();
                Map<String, Boolean> dates_and_status = medication.getDatesAndStatus();
                dates_and_status.put(holder.month_date, true);

                // Creating an object of Medication
                Medication medication = new Medication(medication_id, guest_id, medicine_id, schedule, dates_and_status);

                // Updating Medication data to Firebase Database
                databaseReferenceMedication.child("Medications").child(Integer.toString(medication_id)).setValue(medication).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused)
                    {   }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //##################### Edit Button #####################
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On clicking "Edit", Going to AddMedicationActivity using Intent
                Intent i = new Intent(context, AddMedicationActivity.class);         // Sending medication_id with intent
                i.putExtra("medication_id", medication.getMedication_id());
                context.startActivity(i);
            }
        });

        //##################### Delete Button #####################
        holder.btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = databaseReferenceMedication.child("Medications").child(String.valueOf(medication.getMedication_id()));
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().removeValue();
                        Toast.makeText(context, "Medication deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return medication_list.size();
    }
}