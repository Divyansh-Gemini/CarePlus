package com.careplus.medtracker;

// #################################################################################################
//
// #################################################################################################

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.careplus.medtracker.model.Guest;
import com.careplus.medtracker.model.Hospital;
import com.careplus.medtracker.model.Hospitalization;
import com.careplus.medtracker.model.ID;
import com.careplus.medtracker.model.Medication;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddHospitalizationActivity extends AppCompatActivity {
    TextView textView1, textView2;
    Spinner spinner1, spinner2;
    TextInputEditText editText1, editText2, editText3;
    MaterialButton btn;
    int admit_yyyy, admit_mm, admit_dd, discharge_yyyy, discharge_mm, discharge_dd;
    List<String> guests_name_list, hospitals_name_list;
    List<Integer> guests_id_list, hospitals_id_list;

    int hospitalization_id;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceGuest, databaseReferenceHospital, databaseReferenceHospitalization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hospitalization);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        btn = findViewById(R.id.button);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceGuest = firebaseDatabase.getReference("Guest/Guests");
        databaseReferenceHospital = firebaseDatabase.getReference("Hospital/Hospitals");
        databaseReferenceHospitalization = firebaseDatabase.getReference("Hospitalization");

        // ############### Setting values to Guest Spinner ###############
        guests_name_list = new ArrayList<>();   // Created an ArrayList for guests' name
        guests_id_list = new ArrayList<>();   // Created an ArrayList for guests' IDs
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, guests_name_list);    // Linked ArrayAdapter with ArrayList
        // Fetching Guests' data from Firebase & adding Guests' name in ArrayList
        databaseReferenceGuest.orderByChild("guestName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                guests_name_list.add("--Select Guest--");
                guests_id_list.add(0);
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Guest guest = dataSnapshot.getValue(Guest.class);
                    guests_name_list.add(guest.getGuestName());
                    guests_id_list.add(guest.getGuestID());
                }
                adapter1.notifyDataSetChanged();     // Notifying adapter that the dataset has been updated
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddHospitalizationActivity.this,"" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        spinner1.setAdapter(adapter1);  // Linked spinner with ArrayAdapter

        // ############### Setting values to Hospital Spinner ###############
        hospitals_name_list = new ArrayList<>();   // Created an ArrayList for guests' name
        hospitals_id_list = new ArrayList<>();   // Created an ArrayList for guests' IDs
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hospitals_name_list);    // Linked ArrayAdapter with ArrayList
        // Fetching Hospitals' data from Firebase & adding Hospitals' name in ArrayList
        databaseReferenceHospital.orderByChild("hospitalName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hospitals_name_list.add("--Select Hospital--");
                hospitals_id_list.add(0);
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Hospital hospital = dataSnapshot.getValue(Hospital.class);
                    hospitals_name_list.add(hospital.getHospitalName());
                    hospitals_id_list.add(hospital.getHospitalID());
                }
                adapter2.notifyDataSetChanged();     // Notifying adapter that the dataset has been updated
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddHospitalizationActivity.this,"" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        spinner2.setAdapter(adapter2);  // Linked spinner with ArrayAdapter

        // Getting current date from Calendar class
        Calendar ca = Calendar.getInstance();
        admit_yyyy = discharge_yyyy = ca.get(Calendar.YEAR);
        admit_mm = discharge_mm = ca.get(Calendar.MONTH);
        admit_dd = discharge_dd = ca.get(Calendar.DATE);

        // Popping up DatePickerDialog on clicking editText1
        editText1.setOnClickListener(v ->
                new DatePickerDialog(AddHospitalizationActivity.this, listener1, admit_yyyy, admit_mm, admit_dd).show());

        // Popping up DatePickerDialog on clicking editText2
        editText2.setOnClickListener(v ->
                new DatePickerDialog(AddHospitalizationActivity.this, listener2, discharge_yyyy, discharge_mm, discharge_dd).show());

        // Getting last_hospitalization_id from Firebase Database
        databaseReferenceHospitalization.child("last_hospitalization_id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Getting last_hospitalization_id from Firebase Database
                ID id = dataSnapshot.getValue(ID.class);
                if (id == null)
                    hospitalization_id = 1;
                else
                    hospitalization_id = id.getId() + 1;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddHospitalizationActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding new medication
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int guest_id = guests_id_list.get(spinner1.getSelectedItemPosition());
                int hospital_id = hospitals_id_list.get(spinner2.getSelectedItemPosition());
                String date_of_admit = editText1.getText().toString();
                String date_of_discharge = editText2.getText().toString();
                String treatment = editText3.getText().toString();

                if (spinner1 == null || spinner1.getSelectedItem() == null)
                    textView1.setError("Select a Guest");
                else if (spinner1 == null || spinner1.getSelectedItem() == null)
                    textView2.setError("Select a Hospital");
                else
                {
                    // Creating an object of Hospitalization
                    Hospitalization hospitalization = new Hospitalization(hospitalization_id, guest_id, hospital_id,  date_of_admit, date_of_discharge, treatment);

                    // Uploading Hospitalization data to Firebase Database
                    databaseReferenceHospitalization.child("Hospitalizations").child(Integer.toString(hospitalization_id)).setValue(hospitalization).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AddHospitalizationActivity.this, "New Hospitalization Added", Toast.LENGTH_SHORT).show();

                            // Updating last_hospitalization_id on Firebase
                            databaseReferenceHospitalization.child("last_hospitalization_id").setValue(new ID(hospitalization_id)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused)
                                {   }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddHospitalizationActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            AddHospitalizationActivity.this.onBackPressed();    // going back on previous page
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddHospitalizationActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    // Setting the date in editText1 which is selected by the user in DatePickerDialog,
    // And changing values of yyyy, mm, dd variables so that if user again opens the DatePickerDialog, previously selected date will select by default.
    final DatePickerDialog.OnDateSetListener listener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            editText1.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
            admit_yyyy = year;
            admit_mm = month;
            admit_dd = dayOfMonth;
        }
    };

    // Setting the date in editText2 which is selected by the user in DatePickerDialog,
    // And changing values of yyyy, mm, dd variables so that if user again opens the DatePickerDialog, previously selected date will select by default.
    final DatePickerDialog.OnDateSetListener listener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            editText2.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
            discharge_yyyy = year;
            discharge_mm = month;
            discharge_dd = dayOfMonth;
        }
    };
}