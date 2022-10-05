package com.yashverma.oldeage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class UserInfo extends Fragment {
    EditText e1,e2,e3,e4,e5;
    Button b1,b2;
    FirebaseDatabase rootNote;
    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView= inflater.inflate(R.layout.fragment_user_info, container, false);
        e1=myView.findViewById(R.id.Email);
        e2=myView.findViewById(R.id.Name);
        e3=myView.findViewById(R.id.MobleNo);
        e4=myView.findViewById(R.id.Age);
        e5=myView.findViewById(R.id.Gender);
        b1=myView.findViewById(R.id.button1);
        b2=myView.findViewById(R.id.button2);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNote=FirebaseDatabase.getInstance();
                reference=rootNote.getReference("User info");
                String Email= e1.getText().toString();
                String name=e2.getText().toString();
                String Mobile=e3.getText().toString();
                String Age=e4.getText().toString();
                String gender=e5.getText().toString();

                UserHelper helper=new UserHelper(Email,name,Mobile,Age,gender);
                reference.child(Mobile).setValue(helper);
               // reference.push().setValue(Email);

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name =e2.getText().toString();
                Guest guest=new Guest();
                Bundle b= new Bundle();
                b.putString("Name",name);
                guest.setArguments(b);
                Toast.makeText(getActivity(), " "+name, Toast.LENGTH_SHORT).show();
                FragmentManager fm=getFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                ft.replace(R.id.mainpage,guest);
                ft.addToBackStack("");
                ft.commit();

            }
        });
       return myView;
    }
}