package com.careplus.medtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout textInputLayout1, textInputLayout2;
    TextInputEditText editTextEmail, editTextPassword;
    AppCompatImageButton btn;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textInputLayout1 = findViewById(R.id.textInputLayout1);
        textInputLayout2 = findViewById(R.id.textInputLayout2);
        editTextEmail = findViewById(R.id.editText1);
        editTextPassword = findViewById(R.id.editText2);
        btn = findViewById(R.id.button);

        // Email wale editText ko touch krne pr error wala text chla jana chahiye
        editTextEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                textInputLayout1.setError(null);
                return false;
            }
        });

        // Password wale editText ko touch krne pr error wala text chla jana chahiye
        editTextPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                textInputLayout2.setError(null);
                return false;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim().toLowerCase();
                String password = editTextPassword.getText().toString().trim();

                if (email.isEmpty()) {
                    // If Email is empty, then setting an error text on it
                    textInputLayout2.setError(null);
                    textInputLayout1.setError("Enter Email Address");}
                else if (password.isEmpty()) {
                    // If Password is empty, then setting an error text on it
                    textInputLayout1.setError(null);
                    textInputLayout2.setError("Enter Password");}
                else
                {
                    // Removing error texts (if any)
                    textInputLayout1.setError(null);
                    textInputLayout2.setError(null);

                    //#################### ACTUAL CODE FOR LOGIN ####################

//                    auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                        @Override
//                        public void onSuccess(AuthResult authResult) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(SignInActivity.this, "Login Failed!!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                }
            }
        });
    }
}