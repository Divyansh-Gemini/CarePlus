package com.careplus.medtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout textInputLayout1, textInputLayout2, textInputLayout3;
    TextInputEditText editText1, editText2, editText3;
    AppCompatImageButton btn;
    TextView textView;
    FirebaseAuth auth;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textInputLayout1 = findViewById(R.id.textInputLayout1);
        textInputLayout2 = findViewById(R.id.textInputLayout2);
        textInputLayout3 = findViewById(R.id.textInputLayout3);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        btn = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        auth = FirebaseAuth.getInstance();
        pref = getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = pref.edit();

        if (pref.getBoolean("login", false))
            startActivity(new Intent(LoginActivity.this, MainActivity.class));

        // Old Age Home Name wale editText ko touch krne pr error wala text chla jana chahiye
        editText1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                textInputLayout1.setError(null);
                return false;
            }
        });

        // Email wale editText ko touch krne pr error wala text chla jana chahiye
        editText2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                textInputLayout1.setError(null);
                return false;
            }
        });

        // Password wale editText ko touch krne pr error wala text chla jana chahiye
        editText3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                textInputLayout2.setError(null);
                return false;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_age_home_name = editText1.getText().toString().trim();
                String email = editText2.getText().toString().trim().toLowerCase();
                String password = editText3.getText().toString().trim();

                if (old_age_home_name.isEmpty()) {
                    // If old_age_home_name is empty, then setting an error text on it
                    textInputLayout2.setError(null);
                    textInputLayout3.setError(null);
                    textInputLayout1.setError("Enter Old Age Home Name");}
                else if (email.isEmpty()) {
                    // If Email is empty, then setting an error text on it
                    textInputLayout1.setError(null);
                    textInputLayout3.setError(null);
                    textInputLayout2.setError("Enter Email Address");}
                else if (password.isEmpty()) {
                    // If Password is empty, then setting an error text on it
                    textInputLayout1.setError(null);
                    textInputLayout2.setError(null);
                    textInputLayout3.setError("Enter Password");}
                else
                {
                    // Removing error texts (if any)
                    textInputLayout1.setError(null);
                    textInputLayout2.setError(null);
                    textInputLayout3.setError(null);

                    //#################### ACTUAL CODE FOR LOGIN ####################
                    auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            editor.putString("old_age_home_name", old_age_home_name);
                            editor.putBoolean("login", true);
                            editor.commit();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }
}