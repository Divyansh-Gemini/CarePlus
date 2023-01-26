package com.careplus.medtracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.careplus.medtracker.AddGuestActivity;
import com.careplus.medtracker.AddHospitalActivity;
import com.careplus.medtracker.AddHospitalizationActivity;
import com.careplus.medtracker.R;
import com.careplus.medtracker.model.Guest;
import com.careplus.medtracker.model.Hospital;
import com.careplus.medtracker.model.Hospitalization;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

// #################################################################################################
// Adapter is something that joins 2 things. This class is a link b/w data & RecyclerView.
// The data that is visible in the cards on Guest, Medicine, and Hospital pages is with the help of this adapter.
// Implementation "Edit" & "More" button is also here.
// #################################################################################################

public class HospitalizationCardAdapter extends RecyclerView.Adapter<HospitalizationCardAdapter.HospitalizationViewHolder> {
    Context context;
    ArrayList<Hospitalization> hospitalization_list;
    SharedPreferences pref;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceGuest, databaseReferenceHospital, databaseReferenceHospitalization;

    public HospitalizationCardAdapter(Context context, ArrayList<Hospitalization> hospitalization_list) {
        this.context = context;
        this.hospitalization_list = hospitalization_list;

        pref = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        String old_age_home_name = pref.getString("old_age_home_name", "");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceGuest = firebaseDatabase.getReference(old_age_home_name + "/Guest/Guests");
        databaseReferenceHospital = firebaseDatabase.getReference(old_age_home_name + "/Hospital/Hospitals");
        databaseReferenceHospitalization = firebaseDatabase.getReference(old_age_home_name + "/Hospitalization");
    }

    // HospitalizationViewHolder is inner class & HospitalizationCardAdapter is outer class
    public static class HospitalizationViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3;
        ImageView imageView;
        ImageButton btn_edit, btn_more;

        public HospitalizationViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            imageView = itemView.findViewById(R.id.imageView);
            btn_edit = itemView.findViewById(R.id.button1);
            btn_more = itemView.findViewById(R.id.button2);
        }
    }

    @NonNull
    @Override
    public HospitalizationCardAdapter.HospitalizationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_card, parent, false);
        return new HospitalizationCardAdapter.HospitalizationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalizationCardAdapter.HospitalizationViewHolder holder, int position) {
        Hospitalization hospitalization = hospitalization_list.get(position);

        // Getting guestName from firebase using guestID which we got from hospitalization object and setting it to the textView
        databaseReferenceGuest.child(Integer.toString(hospitalization.getGuestID())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Guest guest = dataSnapshot.getValue(Guest.class);

                // Getting guest_image from Firebase Cloud Storage and setting it to the imageView
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference photoReference = storageReference.child("guest_images/guest_" + guest.getGuestID() + "_" + guest.getGuestName().replace(" ", "_") + ".jpg");
                final long ONE_MEGABYTE = 1024 * 1024 * 4;
                photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        holder.imageView.setImageBitmap(bmp);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(context.getApplicationContext(), "" + exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

                holder.textView1.setText(guest.getGuestName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Getting hospitalName from firebase using hospitalID which we got from hospitalization object and setting it to the textView
        databaseReferenceHospital.child(Integer.toString(hospitalization.getHospitalID())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Hospital hospital = dataSnapshot.getValue(Hospital.class);
                holder.textView2.setText("Admitted in " + hospital.getHospitalName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.textView3.setText("from " + hospitalization.getAdmitDate());
        holder.imageView.setImageResource(R.drawable.old_man_avatar);

        //##################### Edit Button #####################
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On clicking "Edit", Going to AddHospitalizationActivity using Intent
                Intent i = new Intent(context, AddHospitalizationActivity.class);         // Sending hospitalization_id with intent
                i.putExtra("hospitalization_id", hospitalization.getHospitalizationID());
                context.startActivity(i);
            }
        });

        //##################### More Button #####################
        holder.btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu p = new PopupMenu(context, holder.btn_more);
                p.getMenuInflater().inflate(R.menu.more_menu, p.getMenu());
                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            //##################### Delete Button #####################
                            case R.id.item1:
                                Query query = databaseReferenceHospitalization.child("Hospitalizations").orderByChild("hospitalizationID").equalTo(hospitalization.getHospitalizationID());
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                            childSnapshot.getRef().removeValue();
                                        }
                                        Toast.makeText(context, "Hospitalization deleted successfully", Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                notifyDataSetChanged();
                                break;
                        }
                        return false;
                    }
                });
                p.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return hospitalization_list.size();
    }
}