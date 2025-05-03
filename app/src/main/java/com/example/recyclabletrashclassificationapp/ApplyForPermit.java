package com.example.recyclabletrashclassificationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ApplyForPermit extends AppCompatActivity {
    TextView nameEt, emailEt,proc,permit;
    EditText  birthdateEt, phoneEt, streetEt, wardEt, cityEt, zipEt, experiencesEt,idEt;
    Button uploadPhotoBtn, submitBtn;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    CardView cd;
    String uidd;
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri selectedImageUri;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_permit);
//Toolbar
        Toolbar toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back button
        getSupportActionBar().setTitle("Application Details");
        // Handle back button click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close this activity
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        cd = findViewById(R.id.formCard);
        proc = findViewById(R.id.proccessTxt);
        permit = findViewById(R.id.permitTxt);

        //checking profile value
        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        uidd = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String uid = dataSnapshot.getKey();
                    if (uid.equals(uidd)) {
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        if (userModel.getUserProfile().equals("1")) {
                            cd.setVisibility(View.GONE);
                            proc.setVisibility(View.VISIBLE);
                        } else if (userModel.getUserProfile().equals("2")) {
                            cd.setVisibility(View.GONE);
                            permit.setVisibility(View.VISIBLE);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ApplyForPermit.this, "Failed to read data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


//Date picker
/*

        EditText birthdateEditText = findViewById(R.id.birthdate);

        birthdateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ApplyForPermit.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                String date = String.format(Locale.getDefault(), "%02d/%02d/%04d",
                                        selectedDay, selectedMonth + 1, selectedYear);
                                birthdateEditText.setText(date);
                            }
                        },
                        year, month, day
                );
                datePickerDialog.show();
            }
        });
*/



        // Initialize UI elements
        nameEt = findViewById(R.id.Name);
        birthdateEt = findViewById(R.id.birthdate);
        phoneEt = findViewById(R.id.phone);
        emailEt = findViewById(R.id.email);
        streetEt = findViewById(R.id.streetAddress);
        wardEt = findViewById(R.id.ward);
        cityEt = findViewById(R.id.city);
        zipEt = findViewById(R.id.zip);
        idEt = findViewById(R.id.idNum);
        experiencesEt = findViewById(R.id.certifications);
        uploadPhotoBtn = findViewById(R.id.uploadResumeBtn);
        submitBtn = findViewById(R.id.submitApplicationBtn);


        // Fetch and auto-fill user details

        String name = currentUser.getDisplayName();
        String email = currentUser.getEmail();
        emailEt.setText(email);
        nameEt.setText(name);

       /* databaseReference = FirebaseDatabase.getInstance().getReference("user");
      String uidd = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String uid = dataSnapshot.getKey();
                    if (uid.equals(uidd)) {
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        Toast.makeText(ApplyForPermit.this, userModel.getUserEmail(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ApplyForPermit.this, "Failed to read data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
                                                });

*/

        uploadPhotoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1001);
        });


        progressDialog = new ProgressDialog(this);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Applications");

        submitBtn.setOnClickListener(v -> submitApplication());


    }


        private void submitApplication() {
            String name = nameEt.getText().toString().trim();
            String birthdate = birthdateEt.getText().toString().trim();
            String phone = phoneEt.getText().toString().trim();
            String email = emailEt.getText().toString().trim();
            String street = streetEt.getText().toString().trim();
            String ward = wardEt.getText().toString().trim();
            String city = cityEt.getText().toString().trim();
            String zip = zipEt.getText().toString().trim();
            String id = idEt.getText().toString().trim();
            String experiences = experiencesEt.getText().toString().trim();

            // Basic validation
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(this, "Please fill Phone Number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(birthdate)) {
                Toast.makeText(this, "Please fill Birth Date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(ward)) {
                Toast.makeText(this, "Please fill Ward no", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(street)) {
                Toast.makeText(this, "Please fill Street Address", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(city)) {
                Toast.makeText(this, "Please fill City Name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(zip)) {
                Toast.makeText(this, "Please fill Zip Code", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(id)) {
                Toast.makeText(this, "Please fill ID Number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(experiences)) {
                experiences = "No";
            }

            progressDialog.setMessage("Submitting application...");
            progressDialog.show();

            if (selectedImageUri != null) {
                uploadImageAndSaveData(name, birthdate, phone, email, street, ward, city, zip, id , experiences, selectedImageUri);
            } else {
                saveDataToDatabase(name, birthdate, phone, email, street, ward, city, zip, id , experiences, "");
            }
        }



    private void uploadImageAndSaveData(String name, String birthdate, String phone, String email, String street, String ward, String city, String zip,String id, String experiences, Uri imageUri) {
        StorageReference storageReference = FirebaseStorage.getInstance("gs://chat-55084.appspot.com").getReference().child("Applications").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        storageReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    saveDataToDatabase(name, birthdate, phone, email, street, ward, city, zip,id , experiences, imageUrl);
                }))
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Image Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void saveDataToDatabase(String name, String birthdate, String phone, String email, String street, String ward, String city, String zip,String id, String experiences, String imageUrl) {


       //time
        String currentDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        String applicationId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        ApplicationFormModel application = new ApplicationFormModel(FirebaseAuth.getInstance().getCurrentUser().getUid(),applicationId,currentDateTime,
                name, birthdate, phone, email, street, ward, city, zip,id, experiences, imageUrl
        );



        if (applicationId != null) {
            databaseReference.child(applicationId).setValue(application)
                    .addOnSuccessListener(unused -> {
                        //set user profile value
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("userProfile");

                        databaseReference.setValue("1");
                        progressDialog.dismiss();
                        Toast.makeText(this, "Application submitted successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();  // save the selected image URI
            Toast.makeText(this, "Image Selected!", Toast.LENGTH_SHORT).show();
        }
    }

}