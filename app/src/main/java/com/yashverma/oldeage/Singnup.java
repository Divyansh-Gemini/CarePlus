package com.yashverma.oldeage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.app.ProgressDialog;
import android.content.Intent;
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

public class Singnup extends AppCompatActivity {
    EditText Email_Login, Password,conFirmPAssword;
    Button Register;
    TextView t1;
    String emailPAttern="[a-zA-Z0-9._]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth OldAuth;
    FirebaseUser OldUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singnup);
        Email_Login=findViewById(R.id.emailLOgin);
        Password=findViewById(R.id.PAssword);
        conFirmPAssword=findViewById(R.id.ConfirmPAssword);
        Register=findViewById(R.id.Login);
        t1=findViewById(R.id.singin);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(Singnup.this,SingnIn.class);
                startActivity(i);
            }
        });
        progressDialog= new ProgressDialog(this);
        OldAuth=FirebaseAuth.getInstance();
        OldUser=OldAuth.getCurrentUser();
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAuthentication();
            }
        });

    }

    private void PerformAuthentication() {
        String eamil=Email_Login.getText().toString();
        String pass=Password.getText().toString();
        String confirmPass=conFirmPAssword.getText().toString();
        if(!eamil.matches(emailPAttern))
        {
            Email_Login.setError("Enter Correct Email");

        }else if(pass.isEmpty()|| pass.length()<8){
            Password.setText("Enter Proper Password With atleast 8 charaters");
        }
        else if(!pass.equals(confirmPass))
        {
            conFirmPAssword.setError("Passowrd Not match");
        }
        else
        {
            //Creating PRogess Dialogue Box
            progressDialog.setMessage("Please Wait While Registering.....");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            OldAuth.createUserWithEmailAndPassword(eamil,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        sendUsertoNextActivity();
                        Toast.makeText(Singnup.this, "Registration SuccessFull", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(Singnup.this, " "+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    private void sendUsertoNextActivity() {
       Intent i= new Intent(Singnup.this,SingnIn.class);
       i.setFlags(i.FLAG_ACTIVITY_CLEAR_TASK|i.FLAG_ACTIVITY_NEW_TASK);
       startActivity(i);
       finish();
        Toast.makeText(this, "Login With Your Registered Email id Password", Toast.LENGTH_SHORT).show();

    }
}