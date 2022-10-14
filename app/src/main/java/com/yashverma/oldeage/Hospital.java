package com.yashverma.oldeage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Hospital extends Fragment {
EditText Hospital_Id,Hospital_Name,Hospital_mobile,Hospital_Email,Hospital_address;
Button add;
FirebaseDatabase rootnote6;
DatabaseReference reference6;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView=inflater.inflate(R.layout.fragment_hospital, container, false);
        Hospital_Id=myView.findViewById(R.id.hospital_id);
        Hospital_Name=myView.findViewById(R.id.Hospital_name);
        Hospital_mobile=myView.findViewById(R.id.Hospital_Number);
        Hospital_Email=myView.findViewById(R.id.Hospital_Email);
        Hospital_address=myView.findViewById(R.id.Hospital_Addres);
        add=myView.findViewById(R.id.HOspital_InfoAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootnote6=FirebaseDatabase.getInstance();
                reference6=rootnote6.getReference("Hospital_Details");
                String Hospital_id=Hospital_Id.getText().toString();
                String Hospital_name=Hospital_Name.getText().toString();
                String Hospital_Mbile=Hospital_mobile.getText().toString();
                String Hospital_email=Hospital_Email.getText().toString();
                String Hospital_Address=Hospital_address.getText().toString();
                medicinehelper Medicinehelper=new medicinehelper(Hospital_id,Hospital_name,Hospital_Mbile,Hospital_email,Hospital_Address);
                reference6.child("Hospital_Mbile").setValue(Medicinehelper);
            }
        });
        return myView;
    }
}