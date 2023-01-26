package com.careplus.medtracker;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.careplus.medtracker.model.Guest;
import com.careplus.medtracker.model.ID;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class AddGuestActivity extends AppCompatActivity {
    ImageView imageView;
    TextInputLayout textInputLayout1;
    TextInputEditText editText1, editText2, editText3, editText4, editText5, editText6;
    TextView textViewGender;
    RadioButton radio_btn_male, radio_btn_female;
    MaterialButton btn_submit;
    ImageButton btn_img;
    int guest_id;
    Uri imageURI = null;
    Bitmap bmp;
    byte[] byteArray;
    boolean status = true;     // if new data then true, if updation then false

    SharedPreferences pref;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_guest);
        imageView = findViewById(R.id.imageView);
        textInputLayout1 = findViewById(R.id.textInputLayout1);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        textViewGender = findViewById(R.id.textView2);
        radio_btn_male = findViewById(R.id.radio1);
        radio_btn_female = findViewById(R.id.radio2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);
        editText6 = findViewById(R.id.editText6);
        btn_submit = findViewById(R.id.button);
        btn_img = findViewById(R.id.button1);

        pref = getSharedPreferences("login", Context.MODE_PRIVATE);
        String old_age_home_name = pref.getString("old_age_home_name", "");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(old_age_home_name + "/Guest");
        storageReference = FirebaseStorage.getInstance().getReference();

        // Getting guest_id from GuestCardAdapter.java on clicking edit button
        // And filling it to TextFields
        Bundle b = getIntent().getExtras();
        if (b != null) {
            guest_id = b.getInt("guest_id");
            status = false;

            databaseReference.child("Guests").child(Integer.toString(guest_id)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Guest guest = dataSnapshot.getValue(Guest.class);
                    if (guest != null) {

                        // Getting guest_image from Firebase Cloud Storage and setting it to the imageView
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                        StorageReference photoReference = storageReference.child("guest_images/guest_" + guest.getGuestID() + "_" + guest.getGuestName().replace(" ", "_") + ".jpg");
                        final long ONE_MEGABYTE = 1024 * 1024 * 4;
                        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                imageView.setImageBitmap(bmp);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(getApplicationContext(), "" + exception.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                        editText1.setText(guest.getGuestName());
                        editText2.setText(String.valueOf(guest.getGuestAge()));
                        if (guest.getGuestGender().equalsIgnoreCase("male"))
                            radio_btn_male.setChecked(true);
                        else if (guest.getGuestGender().equalsIgnoreCase("female"))
                            radio_btn_female.setChecked(true);
                        editText3.setText(guest.getGuestDateOfAdmit());
                        editText4.setText(guest.getGuestKnownName());
                        editText5.setText(guest.getGuestKnownNumber());
                        editText6.setText(guest.getGuestAddress());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AddGuestActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Removing error on touching editText1
        editText1.setOnTouchListener((v, event) -> {
            textInputLayout1.setError(null);
            return false;
        });

        long today = MaterialDatePicker.todayInUtcMilliseconds();
        CalendarConstraints.Builder constraints = new CalendarConstraints.Builder();
        constraints.setOpenAt(today);   // Setting today's date when it will open first time

        // Material Date Picker
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Date of Admit");
        builder.setSelection(today);  // For default Selection means the current date ke liye hai
        builder.setCalendarConstraints(constraints.build());    // Setting constraints so that it cannot go beyond or below the start and end dates
        final MaterialDatePicker materialDatePicker = builder.build();

        // Popping up DatePickerDialog on clicking editText1
        editText3.setOnClickListener(v -> {
            materialDatePicker.show(getSupportFragmentManager(), "Date Picker");
            //new DatePickerDialog(AddHospitalizationActivity.this, listener1, admit_yyyy, admit_mm, admit_dd).show();
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                editText3.setText(materialDatePicker.getHeaderText());
            }
        });

        // Getting last_guest_id from Firebase Database
        if (status) {
            databaseReference.child("last_guest_id").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Getting last_guest_id from Firebase Database
                    ID id = dataSnapshot.getValue(ID.class);
                    if (id == null)
                        guest_id = 1;
                    else
                        guest_id = id.getId() + 1;
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AddGuestActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Adding new guest
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] guest_image_uri = new String[1];
                String guest_name = editText1.getText().toString().trim();
                int guest_age = 0;
                if (!editText2.getText().toString().trim().isEmpty())
                    guest_age = Integer.parseInt(editText2.getText().toString().trim());
                String guest_gender = "";
                if (radio_btn_male.isChecked())
                    guest_gender = radio_btn_male.getText().toString();
                else if (radio_btn_female.isChecked())
                    guest_gender = radio_btn_female.getText().toString();
                String date_of_admit = editText3.getText().toString();
                String known_name = editText4.getText().toString().trim();
                String known_mobile = editText5.getText().toString().trim();
                String known_address = editText6.getText().toString().trim();

                // Setting error if guest_name or gender is empty
                if (guest_name.isEmpty())
                    textInputLayout1.setError("Enter Guest Name");
                else if (guest_gender.isEmpty())
                    textViewGender.setError("Select a gender");
                else
                {
                    // Uploading guest_image to Firebase CLoud Storage
                    StorageReference ref = storageReference.child("guest_images/" + "guest_" + guest_id + "_" +guest_name.replace(" ", "_") + "." + getFileExtension(imageURI));
                    int finalGuest_age = guest_age;
                    String finalGuest_gender = guest_gender;
                    ref.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                            firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    guest_image_uri[0] = uri.toString();

                                    // Creating an object of Guest
                                    Guest guest = new Guest(guest_id, guest_image_uri[0], guest_name, finalGuest_age, finalGuest_gender,
                                            date_of_admit, known_name, known_mobile, known_address);

                                    // Uploading Guest data to Firebase Database
                                    databaseReference.child("Guests").child(Integer.toString(guest_id)).setValue(guest).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            if (status)
                                                Toast.makeText(AddGuestActivity.this, "New Guest Added", Toast.LENGTH_SHORT).show();
                                            else
                                                Toast.makeText(AddGuestActivity.this, "Guest Data Updated", Toast.LENGTH_SHORT).show();

                                            // Updating last_guest_id on Firebase
                                            if (status) {
                                                databaseReference.child("last_guest_id").setValue(new ID(guest_id)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused)
                                                    {   }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(AddGuestActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            AddGuestActivity.this.onBackPressed();    // going back on previous page
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AddGuestActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddGuestActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dlg = new Dialog(AddGuestActivity.this);
                dlg.setContentView(R.layout.custom_dialog_camera_gallery);
                dlg.setCanceledOnTouchOutside(false);
                dlg.show();

                ImageButton btn_cam = dlg.findViewById(R.id.imageButton1);
                ImageButton btn_gal = dlg.findViewById(R.id.imageButton2);

                btn_cam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Opening Camera using Intent
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri imagePath = createImage();
                        i.putExtra(MediaStore.EXTRA_OUTPUT, imagePath);
                        startActivityForResult(i, 101);
                        dlg.dismiss();
                    }
                });

                btn_gal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Opening Gallery using Intent
                        Intent i = new Intent();
                        i.setType("image/*");
                        i.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(i, 102);
                        dlg.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 101:
                    imageView.setImageURI(imageURI);
                    break;
                case 102:
                    imageURI = data.getData();
                    imageView.setImageURI(imageURI);
                    break;
            }
            // Uri to Bitmap
            try{bmp = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageURI);}
            catch (IOException e)   {e.printStackTrace();}

            // Bitmap to byte[]
            if (bmp != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
                byteArray = byteArrayOutputStream.toByteArray();
            }
        }
    }

    // Storing the image captured by Camera at "Pictures/CarePlus/Guests/" so that we can get its URI
    private Uri createImage() {
        ContentResolver resolver = getApplicationContext().getContentResolver();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            imageURI = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        else
            imageURI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "GUEST_" + guest_id + ".jpg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + "CarePlus/" + "Guests/");

        imageURI = resolver.insert(imageURI, contentValues);
        return imageURI;
    }

    // Returns file extension by providing URI
    public String getFileExtension(Uri imageURI) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(resolver.getType(imageURI));
    }
}