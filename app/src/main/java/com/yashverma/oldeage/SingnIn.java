package com.yashverma.oldeage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SingnIn extends AppCompatActivity {
    EditText Email_Login, Password;
    Button Login;
    TextView t1;
    String emailPAttern="[a-zA-Z0-9._]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth OldAuth;
    FirebaseUser OldUser;
    SharedPreferences shp;
    private static final String Shp_Name="mypref";
    private static final String Key_Email="EMail";
    private static final String key_Pass="Password";

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singn_in);
        Email_Login=findViewById(R.id.emailLOgin);
        Password=findViewById(R.id.PAssword);
        t1=findViewById(R.id.singup);
        Login=findViewById(R.id.Login);
        progressDialog= new ProgressDialog(this);
        OldAuth=FirebaseAuth.getInstance();
        OldUser=OldAuth.getCurrentUser();
        shp= getSharedPreferences(Shp_Name,MODE_PRIVATE);
        //When Open Acitivity Check Shared Preference Data Available or not
            String name=shp.getString(Key_Email,null);
            if(name!=null)
            {
                Intent I= new Intent(SingnIn.this,GuestData.class);
                startActivity(I);
            }
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PerformAuthentication();
                SharedPreferences.Editor editor=shp.edit();//Reference Variable of shared Preference
                editor.putString(Key_Email,Email_Login.getText().toString());
                editor.putString(key_Pass,Password.getText().toString());
                editor.apply();
            }
        });
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(SingnIn.this,Singnup.class);
                startActivity(i);
            }
        });

    }

    private void PerformAuthentication() {
        String eamil=Email_Login.getText().toString();
        String pass=Password.getText().toString();
        if(!eamil.matches(emailPAttern))
        {
            Email_Login.setError("Enter Correct Email");

        }else if(pass.isEmpty()|| pass.length()<8){
            Password.setText("Enter Proper Password With atleast 8 charaters");
        }
        else
        {
            //Creating PRogess Dialogue Box
            progressDialog.setMessage("Please Wait While Logining.....");
            progressDialog.setTitle("Singin");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
         OldAuth.signInWithEmailAndPassword(eamil,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                 if(task.isSuccessful())
                 {
                     progressDialog.dismiss();
                     sendUsertoNextActivity();
                     Toast.makeText(SingnIn.this, "Login SuccessFull", Toast.LENGTH_SHORT).show();
                 }
                 else
                 {
                     progressDialog.dismiss();
                     Toast.makeText(SingnIn.this, " "+task.getException(), Toast.LENGTH_SHORT).show();
                 }
             }
         });
    }
}

    private void sendUsertoNextActivity() {
        Intent i= new Intent(SingnIn.this,GuestData.class);
        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TASK|i.FLAG_ACTIVITY_NEW_TASK);//USed Not to Show Again Once user is Logined
        startActivity(i);

    }
}