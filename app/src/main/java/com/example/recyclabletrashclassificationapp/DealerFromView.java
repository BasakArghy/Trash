package com.example.recyclabletrashclassificationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DealerFromView extends AppCompatActivity implements OnMapReadyCallback {
    LatLng picker;
    private GoogleMap mMap;
    private TextView lat,lng;
    private TextView nameValue, birthdateValue, phoneValue, emailValue, nidValue, experienceValue,applicationid;
    private ImageView photoValue;
    private EditText id;
    private Button accept,delete;
    String applicationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_from_view);

        applicationId = getIntent().getStringExtra("applicationId");
        lat = findViewById(R.id.lat);
        lng=findViewById(R.id.lng);
        //get the supportMapFragment
        SupportMapFragment mapFragment =(SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//Toolbar
        Toolbar toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Form View");// Show back button
        // Handle back button click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close this activity
            }
        });


        // Bind views
        nameValue = findViewById(R.id.nameValue);
        birthdateValue = findViewById(R.id.birthdateValue);
        phoneValue = findViewById(R.id.phoneValue);
        emailValue = findViewById(R.id.emailValue);

        nidValue = findViewById(R.id.nidValue);
        experienceValue = findViewById(R.id.experienceValue);
        photoValue = findViewById(R.id.photoValue);
        applicationid=findViewById(R.id.applicationIdValue);
        id=findViewById(R.id.newIdEditText);
        accept=findViewById(R.id.acceptButton);
        delete=findViewById(R.id.deleteButton);


        applicationid.setText(applicationId);



        //entered id
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newid = id.getText().toString();
                if(newid.length()!=6)
                {
                    Toast.makeText(DealerFromView.this, "ID must be 6 digit", Toast.LENGTH_SHORT).show();
                }
                else {

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user")
                            .child(applicationId)
                            .child("userProfile");
                    databaseReference.setValue("5");

                    // fetch data from Firebase and fill the form
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DealerApplications").child(applicationId);
// Fetch data from Firebase
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get the ApplicationFormModel object from Firebase
                            DealerApplyFormModel model = dataSnapshot.getValue(DealerApplyFormModel.class);

                            if (model != null) {
                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("DealerProfiles").child(applicationId);
                                DealerProfileModel profile =new DealerProfileModel(newid,model.getName(),model.getBirthdate(),model.getUid(),model.getPhone(),model.getEmail(),model.getId(),model.getExperiences(),model.getPhotoUrl(),model.getLat(),model.getLng());
                                reference2.setValue(profile).addOnSuccessListener(unused -> {
                                    Toast.makeText(DealerFromView.this, "Accepted", Toast.LENGTH_SHORT).show();
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(DealerFromView.this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                });;

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle errors
                        }
                    });

                    reference.removeValue();
                    finish();

                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user")
                        .child(applicationId)
                        .child("userProfile");
                databaseReference.setValue("0");
                //delete from firebase storage
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DealerApplications").child(applicationId);
// Fetch data from Firebase
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get the ApplicationFormModel object from Firebase
                        ApplicationFormModel model = dataSnapshot.getValue(ApplicationFormModel.class);

                        if (model != null) {
                            FirebaseStorage storage = FirebaseStorage.getInstance("gs://chat-55084.appspot.com");
                            StorageReference storageRef = storage.getReferenceFromUrl(model.getPhotoUrl());
                            storageRef.delete();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle errors
                    }
                });

                reference.removeValue().addOnSuccessListener(unused -> {
                    Toast.makeText(DealerFromView.this, "Deleted", Toast.LENGTH_SHORT).show();
                    finish();

                }).addOnFailureListener(e -> {
                    Toast.makeText(DealerFromView.this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });





        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            this.finish();

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        // fetch data from Firebase and fill the form
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DealerApplications").child(applicationId);
// Fetch data from Firebase
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the ApplicationFormModel object from Firebase
                DealerApplyFormModel model = dataSnapshot.getValue(DealerApplyFormModel.class);

                if (model != null) {
                    // Set the data to the views
                    nameValue.setText(model.getName());
                    birthdateValue.setText(model.getBirthdate());
                    phoneValue.setText(model.getPhone());
                    emailValue.setText(model.getEmail());
                    lat.setText("Lat: "+model.getLat());
                    lng.setText("Lng: "+model.getLng());
                    nidValue.setText(model.getId());
                    experienceValue.setText(model.getExperiences());

                    // Set the image URL to the ImageView using Glide
                    if (model.getPhotoUrl() != null && !model.getPhotoUrl().isEmpty()) {
                        Glide.with(DealerFromView.this)
                                .load(model.getPhotoUrl())
                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                                .placeholder(R.drawable.baseline_person_24) // Default placeholder image
                                .into(photoValue);

                        mMap=googleMap;

                        LatLng sydney = new LatLng(Float.valueOf(model.getLat()),Float.valueOf(model.getLng()));
                        mMap.addMarker(new MarkerOptions().position(sydney).title(model.getName()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,12));


                    } else {
                        photoValue.setImageResource(R.drawable.baseline_person_24); // Default image if no URL
                    }



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });

    }
}