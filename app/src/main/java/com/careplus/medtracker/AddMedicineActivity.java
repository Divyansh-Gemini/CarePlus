package com.careplus.medtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.careplus.medtracker.model.Guest;
import com.careplus.medtracker.model.ID;
import com.careplus.medtracker.model.Medicine;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class AddMedicineActivity extends AppCompatActivity {
    TextInputLayout textInputLayout1;
    TextInputEditText editText1, editText2, editText3;
    Spinner spinner;
    AppCompatButton btn_minus, btn_plus;
    TextView textViewUnits;
    MaterialButton btn;
    int medicine_id;
    boolean status = true;     // if new data then true, if updation then false

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        textInputLayout1 = findViewById(R.id.textInputLayout1);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        spinner = findViewById(R.id.spinner);
        editText3 = findViewById(R.id.editText3);
        btn_minus = findViewById(R.id.buttonMinus);
        btn_plus = findViewById(R.id.buttonPlus);
        textViewUnits = findViewById(R.id.textView);
        btn = findViewById(R.id.button);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Medicine");

        // Setting values of array in Spinner using ArrayAdapter
        final String[] medicine_types = getResources().getStringArray(R.array.medicine_types);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicine_types);
        spinner.setAdapter(adapter);

        // Getting medicine_id from MedicineCardAdapter.java on clicking edit button
        // And filling it to TextFields
        Bundle b = getIntent().getExtras();
        if (b != null) {
            medicine_id = b.getInt("medicine_id");
            status = false;
            databaseReference.child("Medicines").child(Integer.toString(medicine_id)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Medicine medicine = dataSnapshot.getValue(Medicine.class);
                    editText1.setText(medicine.getMedicineName());
                    editText2.setText(medicine.getMedicineCompany());
                    List medicine_types_list = Arrays.asList(medicine_types);
                    spinner.setSelection(medicine_types_list.indexOf(medicine.getMedicineType()));
                    editText3.setText(String.valueOf(medicine.getMedicineStockCount()));
                }

                // Displaying the error msg in the Toast if fetching data from Firebase is unsuccessful
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AddMedicineActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Removing error on touching editText1
        editText1.setOnTouchListener((v, event) -> {
            textInputLayout1.setError(null);
            return false;
        });

        // Changing the unit according to selected medicine type
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            final String[] medicine_quantities = getResources().getStringArray(R.array.medicine_quantities);
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textViewUnits.setText(medicine_quantities[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Decreasing value of medicine_quantity by 1
        btn_minus.setOnClickListener(v -> {
            int medicine_quantity;
            if (editText3.getText().toString().trim().isEmpty())
                medicine_quantity = 0;
            else
                medicine_quantity = Integer.parseInt(editText3.getText().toString().trim());
            if (medicine_quantity > 0)
                medicine_quantity--;
            editText3.setText(Integer.toString(medicine_quantity));
        });

        // Increasing value of medicine_quantity by 1
        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int medicine_quantity;
                if (editText3.getText().toString().trim().isEmpty())
                    medicine_quantity = 0;
                else
                    medicine_quantity = Integer.parseInt(editText3.getText().toString().trim());
                medicine_quantity++;
                editText3.setText(Integer.toString(medicine_quantity));
            }
        });

        // Getting last_medicine_id from Firebase Database
        if (status) {
            databaseReference.child("last_medicine_id").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Getting last_medicine_id from Firebase Database
                    ID id = dataSnapshot.getValue(ID.class);
                    if (id == null)
                        medicine_id = 1;
                    else
                        medicine_id = id.getId() + 1;
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AddMedicineActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Adding new medicine
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String medicine_name = editText1.getText().toString().trim();
                String medicine_company = editText2.getText().toString().trim();
                String medicine_type = spinner.getSelectedItem().toString();

                int medicine_quantity = 0;
                if (!(editText3.getText().toString().trim().isEmpty())) {
                    medicine_quantity = Integer.parseInt(editText3.getText().toString().trim());
                }

                // Setting error if medicine_name is empty
                if (medicine_name.isEmpty())
                    textInputLayout1.setError("Enter Medicine Name");
                else
                {
                    // Creating an object of Medicine
                    Medicine medicine = new Medicine(medicine_id, medicine_name, medicine_company, medicine_type, medicine_quantity);

                    // Uploading Medicine data to Firebase Database
                    databaseReference.child("Medicines").child(Integer.toString(medicine_id)).setValue(medicine).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            if (status)
                                Toast.makeText(AddMedicineActivity.this, "New Medicine Added", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(AddMedicineActivity.this, "Medicine Data Updated", Toast.LENGTH_SHORT).show();

                            // Updating last_medicine_id on Firebase
                            if (status) {
                                databaseReference.child("last_medicine_id").setValue(new ID(medicine_id)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused)
                                    {   }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddMedicineActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            AddMedicineActivity.this.onBackPressed();    // going back on previous page
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddMedicineActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}