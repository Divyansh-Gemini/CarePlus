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

public class Medicine extends Fragment {
EditText M1,M2,M3,M4,M5;
Button AddM;
FirebaseDatabase rootnote4;
DatabaseReference reference4;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView=inflater.inflate(R.layout.fragment_medicine, container, false);
        Bundle b=getArguments();
        String MEdicineId=b.getString("Medicne_Id");
        M1=myView.findViewById(R.id.Hospital_name);
        M1.setText(MEdicineId);
        M2=myView.findViewById(R.id.Hospital_Number);
        M3=myView.findViewById(R.id.hospital_id);
        M4=myView.findViewById(R.id.Hospital_Email);
        M5=myView.findViewById(R.id.Hospital_Addres);
        AddM=myView.findViewById(R.id.HOspital_InfoAdd);
        AddM.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                rootnote4=FirebaseDatabase.getInstance();
                reference4=rootnote4.getReference("MedicineS_Info");
                String Med_Id=M1.getText().toString();
                String Med_name=M2.getText().toString();
                String Med_Company=M3.getText().toString();
                String Med_Type=M4.getText().toString();
                String Med_stock_count=M5.getText().toString();
                medicinehelper Medicinehelper=new medicinehelper(Med_Id,Med_name,Med_Company,Med_Type,Med_stock_count);
                reference4.child("Med_Id").setValue(Medicinehelper);
            }
        });

        return myView;
    }


}