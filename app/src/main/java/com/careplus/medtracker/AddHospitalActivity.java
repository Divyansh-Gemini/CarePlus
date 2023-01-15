package com.careplus.medtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.careplus.medtracker.model.Guest;
import com.careplus.medtracker.model.Hospital;
import com.careplus.medtracker.model.ID;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddHospitalActivity extends AppCompatActivity {
    TextInputLayout textInputLayout1;
    TextInputEditText editText1, editText2, editText3, editText4;
    MaterialButton btn;
    int hospital_id;
    boolean status = true;     // if new data then true, if updation then false

    SharedPreferences pref;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hospital);
        textInputLayout1 = findViewById(R.id.textInputLayout1);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        btn = findViewById(R.id.button);

        pref = getSharedPreferences("login", Context.MODE_PRIVATE);
        String old_age_home_name = pref.getString("old_age_home_name", "");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(old_age_home_name + "/Hospital");

        // Getting guest_id from GuestCardAdapter.java on clicking edit button
        // And filling it to TextFields
        Bundle b = getIntent().getExtras();
        if (b != null) {
            hospital_id = b.getInt("hospital_id");
            status = false;
            databaseReference.child("Hospitals").child(Integer.toString(hospital_id)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Hospital hospital = dataSnapshot.getValue(Hospital.class);
                    if (hospital != null) {
                        editText1.setText(hospital.getHospitalName());
                        editText2.setText(hospital.getHospitalAddress());
                        editText3.setText(hospital.getHospitalMobile());
                        editText4.setText(hospital.getHospitalEmail());
                    }
                }

                // Displaying the error msg in the Toast if fetching data from Firebase is unsuccessful
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AddHospitalActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Removing error on touching editText1
        editText1.setOnTouchListener((v, event) -> {
            textInputLayout1.setError(null);
            return false;
        });

        // Getting last_hospital_id from Firebase Database
        if (status) {
            databaseReference.child("last_hospital_id").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Getting last_hospital_id from Firebase Database
                    ID id = dataSnapshot.getValue(ID.class);
                    if (id == null)
                        hospital_id = 1;
                    else
                        hospital_id = id.getId() + 1;
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AddHospitalActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Adding new hospital
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hospital_name = editText1.getText().toString().trim();
                String hospital_address = editText2.getText().toString().trim();
                String hospital_telephone = editText3.getText().toString().trim();
                String hospital_email = editText4.getText().toString().trim().toLowerCase();

                // Setting error if medicine_name is empty
                if (hospital_name.isEmpty())
                    textInputLayout1.setError("Enter Hospital Name");
                else
                {
                    // Creating an object of Hospital
                    Hospital hospital = new Hospital(hospital_id, hospital_name, hospital_telephone, hospital_email, hospital_address);

                    // Uploading Hospital data to Firebase Database
                    databaseReference.child("Hospitals").child(Integer.toString(hospital_id)).setValue(hospital).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            if (status)
                                Toast.makeText(AddHospitalActivity.this, "New Hospital Added", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(AddHospitalActivity.this, "Hospital Data Updated", Toast.LENGTH_SHORT).show();

                            // Updating last_hospital_id on Firebase
                            if (status) {
                                databaseReference.child("last_hospital_id").setValue(new ID(hospital_id)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused)
                                    {   }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddHospitalActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            AddHospitalActivity.this.onBackPressed();    // going back on previous page
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddHospitalActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}