package com.example.recyclabletrashclassificationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
import java.util.Date;
import java.util.Locale;

public class ApplyAsDealer extends AppCompatActivity  implements OnMapReadyCallback {
    TextView nameEt, emailEt,proc,permit;
    EditText birthdateEt, phoneEt, experiencesEt,idEt;
    Button uploadPhotoBtn, submitBtn;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    CardView cd;
    String uidd;
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri selectedImageUri;
    LatLng picker;
    private GoogleMap mMap;
    private TextView lat,lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_as_dealer);

lat = findViewById(R.id.lat);
lng=findViewById(R.id.lng);
        //get the supportMapFragment
        SupportMapFragment mapFragment =(SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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
                        if (userModel.getUserProfile().equals("4")) {
                            cd.setVisibility(View.GONE);
                            proc.setVisibility(View.VISIBLE);
                        } else if (userModel.getUserProfile().equals("5")) {
                            cd.setVisibility(View.GONE);
                            permit.setVisibility(View.VISIBLE);
                        }
                        else if (userModel.getUserProfile().equals("1") || userModel.getUserProfile().equals("2")) {
                            cd.setVisibility(View.GONE);
                            permit.setText("You can't apply as dealer");
                            permit.setVisibility(View.VISIBLE);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ApplyAsDealer.this, "Failed to read data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        // Initialize UI elements
        nameEt = findViewById(R.id.Name);
        birthdateEt = findViewById(R.id.birthdate);
        phoneEt = findViewById(R.id.phone);
        emailEt = findViewById(R.id.email);

        idEt = findViewById(R.id.idNum);
        experiencesEt = findViewById(R.id.certifications);
        uploadPhotoBtn = findViewById(R.id.uploadResumeBtn);
        submitBtn = findViewById(R.id.submitApplicationBtn);


        // Fetch and auto-fill user details

        String name = currentUser.getDisplayName();
        String email = currentUser.getEmail();
        emailEt.setText(email);
        nameEt.setText(name);

        uploadPhotoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1001);
        });


        progressDialog = new ProgressDialog(this);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("DealerApplications");

        submitBtn.setOnClickListener(v -> submitApplication());


    }

    /* @Override
     public void onMapReady(GoogleMap googleMap)
     {
         mMap=googleMap;
         //add a marker in Sydney,Australia and move camera
         LatLng sydney = new LatLng(-34,151);
         mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
         mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,12));
     }*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia
        LatLng khulna = new LatLng(22.828, 89.538);
        mMap.addMarker(new MarkerOptions().position(khulna).title("Marker in Khulna"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(khulna, 12));

        // Set a marker on map click
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Clear previous markers if needed
                mMap.clear();
                lat.setText(String.valueOf(latLng.latitude));
                lng.setText(String.valueOf(latLng.longitude));
                picker=latLng;
                // Add new marker
                mMap.addMarker(new MarkerOptions().position(latLng).title("Clicked Location"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            }
        });
    }
    private void submitApplication() {
        String name = nameEt.getText().toString().trim();
        String birthdate = birthdateEt.getText().toString().trim();
        String phone = phoneEt.getText().toString().trim();
        String email = emailEt.getText().toString().trim();

        String id = idEt.getText().toString().trim();
        String experiences = experiencesEt.getText().toString().trim();
        String lat = String.valueOf(picker.latitude);
        String lng = String.valueOf(picker.longitude);

        // Basic validation
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please fill Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(birthdate)) {
            Toast.makeText(this, "Please fill Birth Date", Toast.LENGTH_SHORT).show();
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
            uploadImageAndSaveData(name, birthdate, phone, email, id , experiences, selectedImageUri,lat,lng);
        } else {
            saveDataToDatabase(name, birthdate, phone, email, id , experiences, "",lat,lng);
        }
    }



    private void uploadImageAndSaveData(String name, String birthdate, String phone, String email,String id, String experiences, Uri imageUri, String lat ,String lng) {
        StorageReference storageReference = FirebaseStorage.getInstance("gs://chat-55084.appspot.com").getReference().child("DealerApplications").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        storageReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    saveDataToDatabase(name, birthdate, phone, email,id , experiences, imageUrl,lat,lng);
                }))
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Image Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void saveDataToDatabase(String name, String birthdate, String phone, String email,String id, String experiences, String imageUrl, String lat ,String lng) {


        //time
        String currentDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        String applicationId = FirebaseAuth.getInstance().getCurrentUser().getUid();


       DealerApplyFormModel application = new DealerApplyFormModel(FirebaseAuth.getInstance().getCurrentUser().getUid(),applicationId,currentDateTime, name, birthdate, phone, email,id, experiences, imageUrl,lat,lng);



        if (applicationId != null) {
            databaseReference.child(applicationId).setValue(application)
                    .addOnSuccessListener(unused -> {
                        //set user profile value
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("userProfile");

                        databaseReference.setValue("4");
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

