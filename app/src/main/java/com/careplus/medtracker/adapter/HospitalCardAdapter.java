package com.careplus.medtracker.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.careplus.medtracker.R;
import com.careplus.medtracker.model.Guest;
import com.careplus.medtracker.model.Hospital;

import java.util.ArrayList;

// #################################################################################################
// Adapter is something that joins 2 things. This class is a link b/w data & RecyclerView.
// The data that is visible in the cards on Guest, Medicine, and Hospital pages is with the help of this adapter.
// Implementation "Edit" & "More" button is also here.
// #################################################################################################

public class HospitalCardAdapter extends RecyclerView.Adapter<HospitalCardAdapter.HospitalViewHolder> {
    Context context;
    ArrayList<Hospital> hospital_list;

    public HospitalCardAdapter(Context context, ArrayList<Hospital> hospital_list) {
        this.context = context;
        this.hospital_list = hospital_list;
    }

    // HospitalViewHolder is inner class & HospitalCardAdapter is outer class
    public static class HospitalViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2;
        ImageView imageView;
        ImageButton btn_edit, btn_more;

        public HospitalViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            imageView = itemView.findViewById(R.id.imageView);
            btn_edit = itemView.findViewById(R.id.button1);
            btn_more = itemView.findViewById(R.id.button2);
        }
    }

    @NonNull
    @Override
    public HospitalCardAdapter.HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_guest, parent, false);
        return new HospitalCardAdapter.HospitalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalCardAdapter.HospitalViewHolder holder, int position) {
        Hospital hospital = hospital_list.get(position);
        holder.textView1.setText(hospital.getHospitalName());
        holder.textView2.setText(hospital.getHospitalAddress());
        holder.imageView.setImageResource(R.drawable.image_hospital);

        //##################### Edit Button #####################
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On clicking "Edit", Going to AddGuestActivity using Intent
                Intent i = new Intent(context, AddHospitalActivity.class);         // Sending guest_id with intent
                i.putExtra("hospital_id", hospital.getHospitalID());
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
                                Toast.makeText(context, "Unable to Delete!!", Toast.LENGTH_SHORT).show();
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
        return hospital_list.size();
    }
}