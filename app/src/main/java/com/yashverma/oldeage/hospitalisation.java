package com.yashverma.oldeage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class hospitalisation extends Fragment {
EditText Hplisation_Id,Guest_id,Hp_Id,admit_date,Discharge_Date,treatment;
Button Add,Medicine;
FirebaseDatabase rootnote5;
DatabaseReference reference5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview6=inflater.inflate(R.layout.fragment_hospitalisation2, container, false);
        Bundle b=getArguments();
        String Guestid=b.getString("Guest_Id");
    Hplisation_Id = myview6.findViewById(R.id.hospitalisation_id);
    Guest_id=myview6.findViewById(R.id.guest_id);
    Guest_id.setText(Guestid);
    Hp_Id=myview6.findViewById(R.id.Hospital_id);
    admit_date=myview6.findViewById(R.id.Admit_date);
    Discharge_Date=myview6.findViewById(R.id.Discharge_Date);
    treatment=myview6.findViewById(R.id.Treatement);
    Add=myview6.findViewById(R.id.h0spital_InfoAdd);
    rootnote5=FirebaseDatabase.getInstance();
    reference5=rootnote5.getReference("Hospitalization");
    Medicine=myview6.findViewById(R.id.HospitaleDetails);
    Add.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String Hospitalization_Id=Hplisation_Id.getText().toString();
            String Guest_Id=Guest_id.getText().toString();
            String AdmitDate=admit_date.getText().toString();
            String Discarge_date=Discharge_Date.getText().toString();
            String Treatment=treatment.getText().toString();
            String HpId=Hp_Id.getText().toString();
         Hospitalization_Info hospitalization_info=new Hospitalization_Info(Hospitalization_Id,Guest_Id,AdmitDate,Discarge_date,Treatment,HpId);
         reference5.child(Hospitalization_Id).setValue(hospitalization_info);

        }
    });
    Medicine.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String HpId=Hp_Id.getText().toString();
            Hospital hospital= new Hospital();
            Bundle b=new Bundle();
            b.putString("Hp_Id",HpId);
           hospital.setArguments(b);
            FragmentManager fm=getFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.Blank,hospital);
            ft.commit();

        }
    });
        return myview6;
    }
}