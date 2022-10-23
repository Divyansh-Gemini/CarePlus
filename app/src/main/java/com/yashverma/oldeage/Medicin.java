package com.yashverma.oldeage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Medicin extends Fragment {
    EditText Medid,MedName,MedComapny,MedType,MedStockcount;
    Button MedAdd;
    FirebaseDatabase Med;
    DatabaseReference MedReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View myview= inflater.inflate(R.layout.fragment_medicin, container, false);
      Bundle b=getArguments();
      String mEdId=b.getString("Medicne_Id");
      Medid=myview.findViewById(R.id.Medi_id);
      Medid.setText(mEdId);
      MedName=myview.findViewById(R.id.Med_Name);
      MedComapny=myview.findViewById(R.id.Med_Company);
      MedType=myview.findViewById(R.id.Med_type);
      MedStockcount=myview.findViewById(R.id.Med_Tsock_Count);
      MedAdd=myview.findViewById(R.id.MedAdd);
        Med=FirebaseDatabase.getInstance();
        MedReference=Med.getReference("MedicineInfo");
      MedAdd.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              String Med_id=Medid.getText().toString();
              String Med_name=MedName.getText().toString();
              String Med_company=MedComapny.getText().toString();
              String Med_Type=MedType.getText().toString();
              String Med_Stock=MedStockcount.getText().toString();
              medicinehelper Mhelper=new medicinehelper(Med_id,Med_name,Med_company,Med_Type,Med_Stock);
              MedReference.child(Med_id).setValue(Mhelper).addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void unused) {
                      Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                  }

              }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      Toast.makeText(getActivity(), "unsaved"+e.getMessage(), Toast.LENGTH_SHORT).show();
                  }
              });

          }
      });
      return myview;
    }
}