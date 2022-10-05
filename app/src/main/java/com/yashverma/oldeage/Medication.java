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

public class Medication extends Fragment {
EditText m1,m2,m3,m4,m5,m6,m7,m8,m9;
Button btn3;
FirebaseDatabase rootNote3;
DatabaseReference reference3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview= inflater.inflate(R.layout.fragment_medication, container, false);
        Bundle b=getArguments();
        String GuestId=b.getString("Guest_Id");
        m1=myview.findViewById(R.id.medication_id);
        m2=myview.findViewById(R.id.Guest_id);
        m2.setText(GuestId);
        m3=myview.findViewById(R.id.medicine_id);
        m4=myview.findViewById(R.id.Schedule);
        m5=myview.findViewById(R.id.dose);
        m6=myview.findViewById(R.id.Start_Date);
        m7=myview.findViewById(R.id.End_Date);
        m8=myview.findViewById(R.id.Remarks);
        m9=myview.findViewById(R.id.Cause_disease_reason);
        btn3=myview.findViewById(R.id.Button3);

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Code To Store The data In the Firebase
                rootNote3=FirebaseDatabase.getInstance();
                reference3=rootNote3.getReference("Medication");
                String Medication_id=m1.getText().toString();
                String Guest_Id=m2.getText().toString();
                String Medicine_id=m3.getText().toString();
                String Schedule=m4.getText().toString();
                String Dose=m5.getText().toString();
                String Start_Date=m6.getText().toString();
                String End_date=m7.getText().toString();
                String Remarks=m8.getText().toString();
                String Cause_Disease_Reason=m9.getText().toString();
                medicationHElper medicationHelper=new medicationHElper(Medication_id,Guest_Id,Medicine_id,Schedule,Dose,Start_Date,End_date,Remarks,Cause_Disease_Reason);
                reference3.child(Medication_id).setValue(medicationHelper);
            }
        });

        return myview;
    }
}