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
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.careplus.medtracker.model.Guest;
import com.careplus.medtracker.model.ID;
import com.careplus.medtracker.model.Medication;
import com.careplus.medtracker.model.Medicine;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddMedicationActivity extends AppCompatActivity {
    TextView textView1, textView2;
    Spinner spinner1, spinner2;
    NumberPicker numberPicker1, numberPicker2;
    TextInputEditText editText1, editText2;
    MaterialButton btn;

    List<String> guests_name_list, medicines_name_list;
    List<Integer> guests_id_list, medicines_id_list;
    static final long ONE_DAY = 24 * 60 * 60 * 1000L;
    int medication_id;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceGuest, databaseReferenceMedicine, databaseReferenceMedication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        numberPicker1 = findViewById(R.id.numberPicker1);
        numberPicker2 = findViewById(R.id.numberPicker2);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        btn = findViewById(R.id.button);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceGuest = firebaseDatabase.getReference("Guest/Guests");
        databaseReferenceMedicine = firebaseDatabase.getReference("Medicine/Medicines");
        databaseReferenceMedication = firebaseDatabase.getReference("Medication");

        // ############### Setting values to Guest Spinner ###############
        guests_name_list = new ArrayList<>();   // Created an ArrayList
        guests_id_list = new ArrayList<>();   // Created an ArrayList
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
                Toast.makeText(AddMedicationActivity.this,"" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        spinner1.setAdapter(adapter1);  // Linked spinner with ArrayAdapter

        // ############### Setting values to Medicine Spinner ###############
        medicines_name_list = new ArrayList<>();   // Created an ArrayList
        medicines_id_list = new ArrayList<>();   // Created an ArrayList
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicines_name_list);    // Linked ArrayAdapter with ArrayList
        // Fetching Medicines' data from Firebase & adding Medicines' name in ArrayList
        databaseReferenceMedicine.orderByChild("medicineName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                medicines_name_list.add("--Select Medicine--");
                medicines_id_list.add(0);
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Medicine medicine = dataSnapshot.getValue(Medicine.class);
                    medicines_name_list.add(medicine.getMedicineName());
                    medicines_id_list.add(medicine.getMedicineID());
                }
                adapter2.notifyDataSetChanged();     // Notifying adapter that the dataset has been updated
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddMedicationActivity.this,"" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        spinner2.setAdapter(adapter2);  // Linked spinner with ArrayAdapter

        // ############### Setting values to Before-During-After NumberPicker ###############
        final String[] bda = getResources().getStringArray(R.array.BDA);    // Created an Array
        numberPicker1.setMinValue(0);
        numberPicker1.setMaxValue(bda.length - 1);
        numberPicker1.setDisplayedValues(bda);

        // ############### Setting values to Meal NumberPicker ###############
        final String[] meal = getResources().getStringArray(R.array.meal);    // Created an Array
        numberPicker2.setMinValue(0);
        numberPicker2.setMaxValue(meal.length - 1);
        numberPicker2.setDisplayedValues(meal);

        // Getting current date from Calendar class
        Calendar ca = Calendar.getInstance();
        ca.clear();

        long today = MaterialDatePicker.todayInUtcMilliseconds();

        CalendarConstraints.Builder constraints = new CalendarConstraints.Builder();
        constraints.setStart(today);    // Setting starting month
        constraints.setOpenAt(today);   // Setting today's date when it will open first time
        constraints.setValidator(DateValidatorPointForward.now());  // Setting validator so that previous dates cannot be selected

        // Material Date Picker
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Start Date");
        builder.setSelection(today);  // For default Selection means the current date ke liye hai
        builder.setCalendarConstraints(constraints.build());    // Setting constraints so that it cannot go beyond or below the start and end dates
        final MaterialDatePicker materialDatePicker = builder.build();

        // Popping up DatePickerDialog on clicking editText1
        editText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "Date Picker");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                editText1.setText(materialDatePicker.getHeaderText());
            }
        });

        MaterialDatePicker.Builder builder2 = MaterialDatePicker.Builder.datePicker();
        builder2.setTitleText("End Date");
        builder2.setCalendarConstraints(constraints.build());    // Setting constraints so that it cannot go beyond or below the start and end dates
        final MaterialDatePicker mdp2 = builder2.build();

        // Popping up DatePickerDialog on clicking editText2
        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdp2.show(getSupportFragmentManager(), "Date Picker");
            }
        });
        mdp2.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                editText2.setText(mdp2.getHeaderText());
            }
        });

        // Getting last_medication_id from Firebase Database
        databaseReferenceMedication.child("last_medication_id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ID id = dataSnapshot.getValue(ID.class);
                if (id == null)
                    medication_id = 1;
                else
                    medication_id = id.getId() + 1;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddMedicationActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding new medication
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int guest_id = guests_id_list.get(spinner1.getSelectedItemPosition());
                int medicine_id = medicines_id_list.get(spinner2.getSelectedItemPosition());
                String schedule = bda[numberPicker1.getValue()] + " " + meal[numberPicker2.getValue()];
                String start_date = editText1.getText().toString();
                String end_date = editText2.getText().toString();

                List<String> dates = getDatesBetween(start_date, end_date);
                Map<String, Boolean> dates_and_status = new HashMap<String, Boolean>();
                for (String date : dates)
                    dates_and_status.put(date.substring(4,10), false);

                if (spinner1 == null || spinner1.getSelectedItem() == null)
                    textView1.setError("Select a Guest");
                else if (spinner1 == null || spinner1.getSelectedItem() == null)
                    textView2.setError("Select a Medicine");
                else
                {
                    // Creating an object of Medication
                    Medication medication = new Medication(medication_id, guest_id, medicine_id, schedule, dates_and_status);

                    // Uploading Medication data to Firebase Database
                    databaseReferenceMedication.child("Medications").child(Integer.toString(medication_id)).setValue(medication).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            // Updating last_medication_id on Firebase
                            databaseReferenceMedication.child("last_medication_id").setValue(new ID(medication_id)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused)
                                {   }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddMedicationActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            Toast.makeText(AddMedicationActivity.this, "New Medication Added", Toast.LENGTH_SHORT).show();
                            AddMedicationActivity.this.onBackPressed();    // going back on previous page
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddMedicationActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public static List<String> getDatesBetween(String startDate, String endDate) {
        List<String> s=new ArrayList<>();
        long  from= Date.parse(startDate);
        long to=Date.parse(endDate);
        int x = 0;

        while (from <= to) {
            x += 1;
            System.out.println ("Dates: " + new Date(from));
            Date s1 = new Date(Long.parseLong(String.valueOf(from)));
            s.add(String.valueOf(s1));
            from += ONE_DAY;
        }
        return s;
    }
}