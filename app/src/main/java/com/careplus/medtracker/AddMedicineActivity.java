package com.careplus.medtracker;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddMedicineActivity extends AppCompatActivity {
    TextInputLayout textInputLayout1;
    TextInputEditText editText2, editText3;
    AutoCompleteTextView autoCompleteTextView;
    Spinner spinner;
    AppCompatButton btn_minus, btn_plus;
    TextView textViewUnits;
    MaterialButton btn;
    int medicine_id;
    boolean status = true;     // if new data then true, if updation then false
    String med_name = "";
    ArrayList<String> medicine_names = new ArrayList<>();
    ArrayList<String> manufacturer_names = new ArrayList<>();

    SharedPreferences pref;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        textInputLayout1 = findViewById(R.id.textInputLayout1);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        editText2 = findViewById(R.id.editText2);
        spinner = findViewById(R.id.spinner);
        editText3 = findViewById(R.id.editText3);
        btn_minus = findViewById(R.id.buttonMinus);
        btn_plus = findViewById(R.id.buttonPlus);
        textViewUnits = findViewById(R.id.textView);
        btn = findViewById(R.id.button);

        pref = getSharedPreferences("login", Context.MODE_PRIVATE);
        String old_age_home_name = pref.getString("old_age_home_name", "");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(old_age_home_name + "/Medicine");

        String api_url = "https://beta.myupchar.com/api/medicine/search?api_key=dcd5391705e71bbb87ffaf683b0b8f44&name=";

        RequestQueue queue = Volley.newRequestQueue(this);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                med_name = autoCompleteTextView.getText().toString();
                medicine_names.add(0, med_name);
                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, api_url + med_name,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    //Log.e("api","data " + jsonArray.length());
                                    //Log.e("api","data " + jsonArray.toString());
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        //Log.e("api","i: " + i);
                                        JSONObject singleObject = jsonArray.getJSONObject(i);
                                        //Log.e("api","Medicine: " + singleObject.getString("name"));
                                        medicine_names.add(singleObject.getString("name"));
                                        //Log.e("api","Manufacturer: " + singleObject.getJSONObject("manufacturer").getString("name"));
                                        manufacturer_names.add(singleObject.getJSONObject("manufacturer").getString("name"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("api","Error: " + error.getLocalizedMessage());
                                Toast.makeText(AddMedicineActivity.this, "" + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);

                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(AddMedicineActivity.this, android.R.layout.simple_list_item_1, medicine_names);
                autoCompleteTextView.setAdapter(adapter1);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Setting values of array in Spinner using ArrayAdapter
        final String[] medicine_types = getResources().getStringArray(R.array.medicine_types);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicine_types);
        spinner.setAdapter(adapter2);

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
                    if (medicine != null) {
                        autoCompleteTextView.setText(medicine.getMedicineName());
                        editText2.setText(medicine.getMedicineCompany());
                        List medicine_types_list = Arrays.asList(medicine_types);
                        spinner.setSelection(medicine_types_list.indexOf(medicine.getMedicineType()));
                        editText3.setText(String.valueOf(medicine.getMedicineStockCount()));
                    }
                }

                // Displaying the error msg in the Toast if fetching data from Firebase is unsuccessful
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AddMedicineActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Removing error on touching editText1
        autoCompleteTextView.setOnTouchListener((v, event) -> {
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
                String medicine_name = autoCompleteTextView.getText().toString().trim();
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