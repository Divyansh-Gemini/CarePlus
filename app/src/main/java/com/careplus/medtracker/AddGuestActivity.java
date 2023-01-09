package com.careplus.medtracker;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.careplus.medtracker.model.Guest;
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

import java.util.Calendar;

public class AddGuestActivity extends AppCompatActivity {
    ImageView imageView;
    TextInputLayout textInputLayout1;
    TextInputEditText editText1, editText2, editText3, editText4, editText5, editText6;
    TextView textViewGender;
    RadioButton radio_btn_male, radio_btn_female;
    MaterialButton btn;
    int yyyy, mm, dd;
    int guest_id;
    boolean status = true;     // if new data then true, if updation then false

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_guest);
        imageView = findViewById(R.id.imageView);
        textInputLayout1 = findViewById(R.id.textInputLayout1);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        textViewGender = findViewById(R.id.textView2);
        radio_btn_male = findViewById(R.id.radio1);
        radio_btn_female = findViewById(R.id.radio2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);
        editText6 = findViewById(R.id.editText6);
        btn = findViewById(R.id.button);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Guest");

        // Getting guest_id from GuestCardAdapter.java on clicking edit button
        // And filling it to TextFields
        Bundle b = getIntent().getExtras();
        if (b != null) {
            guest_id = b.getInt("guest_id");
            status = false;
            databaseReference.child("Guests").child(Integer.toString(guest_id)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Guest guest = dataSnapshot.getValue(Guest.class);
                    editText1.setText(guest.getGuestName());
                    editText2.setText(String.valueOf(guest.getGuestAge()));
                    if (guest.getGuestGender().equalsIgnoreCase("male"))
                        radio_btn_male.setChecked(true);
                    else if (guest.getGuestGender().equalsIgnoreCase("female"))
                        radio_btn_female.setChecked(true);
                    editText3.setText(guest.getGuestDateOfAdmit());
                    editText4.setText(guest.getGuestKnownName());
                    editText5.setText(guest.getGuestKnownNumber());
                    editText6.setText(guest.getGuestAddress());
                }

                // Displaying the error msg in the Toast if fetching data from Firebase is unsuccessful
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AddGuestActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Getting current date from Calendar class
        Calendar ca = Calendar.getInstance();
        yyyy = ca.get(Calendar.YEAR);
        mm = ca.get(Calendar.MONTH);
        dd = ca.get(Calendar.DATE);

        // Removing error on touching editText1
        editText1.setOnTouchListener((v, event) -> {
            textInputLayout1.setError(null);
            return false;
        });

        // Popping up DatePickerDialog on clicking editText3
        editText3.setOnClickListener(v ->
                new DatePickerDialog(AddGuestActivity.this, listener, yyyy, mm, dd).show());

        // Getting last_guest_id from Firebase Database
        if (status) {
            databaseReference.child("last_guest_id").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Getting last_guest_id from Firebase Database
                    ID id = dataSnapshot.getValue(ID.class);
                    if (id == null)
                        guest_id = 1;
                    else
                        guest_id = id.getId() + 1;
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AddGuestActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Adding new guest
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String guest_name = editText1.getText().toString().trim();
                int guest_age = 0;
                if (!editText2.getText().toString().trim().isEmpty())
                    guest_age = Integer.parseInt(editText2.getText().toString().trim());
                String guest_gender = "";
                if (radio_btn_male.isChecked())
                    guest_gender = radio_btn_male.getText().toString();
                else if (radio_btn_female.isChecked())
                    guest_gender = radio_btn_female.getText().toString();
                String date_of_admit = editText3.getText().toString();
                String known_name = editText4.getText().toString().trim();
                String known_mobile = editText5.getText().toString().trim();
                String known_address = editText6.getText().toString().trim();

                // Setting error if guest_name or gender is empty
                if (guest_name.isEmpty())
                    textInputLayout1.setError("Enter Guest Name");
                else if (guest_gender.isEmpty())
                    textViewGender.setError("Select a gender");
                else
                {
                    // Creating an object of Guest
                    Guest guest = new Guest(guest_id, guest_name, guest_age, guest_gender,
                            date_of_admit, known_name, known_mobile, known_address);

                    // Uploading Guest data to Firebase Database
                    databaseReference.child("Guests").child(Integer.toString(guest_id)).setValue(guest).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            if (status)
                                Toast.makeText(AddGuestActivity.this, "New Guest Added", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(AddGuestActivity.this, "Guest Data Updated", Toast.LENGTH_SHORT).show();

                            // Updating last_guest_id on Firebase
                            if (status) {
                                databaseReference.child("last_guest_id").setValue(new ID(guest_id)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused)
                                    {   }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddGuestActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            AddGuestActivity.this.onBackPressed();    // going back on previous page
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddGuestActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    // Setting the date in editText3 which is selected by the user in DatePickerDialog,
    // And changing values of yyyy, mm, dd variables so that if user again opens the DatePickerDialog, previously selected date will select by default.
    final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            editText3.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
            yyyy = year;
            mm = month;
            dd = dayOfMonth;
        }
    };
}