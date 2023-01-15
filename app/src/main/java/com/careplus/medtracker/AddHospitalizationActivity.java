package com.careplus.medtracker;

// #################################################################################################
//
// #################################################################################################

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
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
    TextInputEditText editText1, editText2;
    MaterialButton btn;
    List<String> guests_name_list, hospitals_name_list;
    List<Integer> guests_id_list, hospitals_id_list;
    int hospitalization_id;
    boolean status = true;     // if new data then true, if updation then false

    SharedPreferences pref;
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
        btn = findViewById(R.id.button);

        pref = getSharedPreferences("login", Context.MODE_PRIVATE);
        String old_age_home_name = pref.getString("old_age_home_name", "");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceGuest = firebaseDatabase.getReference(old_age_home_name + "/Guest/Guests");
        databaseReferenceHospital = firebaseDatabase.getReference(old_age_home_name + "/Hospital/Hospitals");
        databaseReferenceHospitalization = firebaseDatabase.getReference(old_age_home_name + "/Hospitalization");

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

        // Getting hospitalization_id from HospitalizationCardAdapter.java on clicking edit button
        // And filling it to TextFields
        Bundle b = getIntent().getExtras();
        if (b != null) {
            hospitalization_id = b.getInt("hospitalization_id");
            status = false;
            databaseReferenceHospitalization.child("Hospitalizations").child(Integer.toString(hospitalization_id)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Hospitalization hospitalization = dataSnapshot.getValue(Hospitalization.class);
                    if (hospitalization != null) {
                        spinner1.setSelection(guests_id_list.indexOf(hospitalization.getGuestID()));
                        spinner2.setSelection(hospitals_id_list.indexOf(hospitalization.getHospitalID()));
                        editText1.setText(hospitalization.getAdmitDate());
                        editText2.setText(hospitalization.getTreatment());
                    }
                }

                // Displaying the error msg in the Toast if fetching data from Firebase is unsuccessful
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AddHospitalizationActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        long today = MaterialDatePicker.todayInUtcMilliseconds();
        CalendarConstraints.Builder constraints = new CalendarConstraints.Builder();
        constraints.setOpenAt(today);   // Setting today's date when it will open first time

        // Material Date Picker
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Date of Admit");
        builder.setSelection(today);  // For default Selection means the current date ke liye hai
        builder.setCalendarConstraints(constraints.build());    // Setting constraints so that it cannot go beyond or below the start and end dates
        final MaterialDatePicker materialDatePicker = builder.build();

        // Popping up DatePickerDialog on clicking editText1
        editText1.setOnClickListener(v -> {
            materialDatePicker.show(getSupportFragmentManager(), "Date Picker");
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                editText1.setText(materialDatePicker.getHeaderText());
            }
        });

        // Getting last_hospitalization_id from Firebase Database
        if (status) {
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
        }

        // Adding new medication
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int guest_id = guests_id_list.get(spinner1.getSelectedItemPosition());
                int hospital_id = hospitals_id_list.get(spinner2.getSelectedItemPosition());
                String date_of_admit = editText1.getText().toString();
                String treatment = editText2.getText().toString();

                if (spinner1 == null || spinner1.getSelectedItem() == null)
                    textView1.setError("Select a Guest");
                else if (spinner1 == null || spinner1.getSelectedItem() == null)
                    textView2.setError("Select a Hospital");
                else
                {
                    // Creating an object of Hospitalization
                    Hospitalization hospitalization = new Hospitalization(hospitalization_id, guest_id, hospital_id,  date_of_admit, treatment);

                    // Uploading Hospitalization data to Firebase Database
                    databaseReferenceHospitalization.child("Hospitalizations").child(Integer.toString(hospitalization_id)).setValue(hospitalization).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AddHospitalizationActivity.this, "New Hospitalization Added", Toast.LENGTH_SHORT).show();

                            // Updating last_hospitalization_id on Firebase
                            if (status) {
                                databaseReferenceHospitalization.child("last_hospitalization_id").setValue(new ID(hospitalization_id)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddHospitalizationActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
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
}