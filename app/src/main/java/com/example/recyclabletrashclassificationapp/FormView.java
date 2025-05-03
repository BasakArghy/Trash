package com.example.recyclabletrashclassificationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FormView extends AppCompatActivity {
    private TextView nameValue, birthdateValue, phoneValue, emailValue, addressValue, nidValue, experienceValue,applicationid;
    private ImageView photoValue;
    private EditText id;
    private Button accept,delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_view);

        String applicationId = getIntent().getStringExtra("applicationId");

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
        addressValue = findViewById(R.id.addressValue);
        nidValue = findViewById(R.id.nidValue);
        experienceValue = findViewById(R.id.experienceValue);
        photoValue = findViewById(R.id.photoValue);
        applicationid=findViewById(R.id.applicationIdValue);
        id=findViewById(R.id.newIdEditText);
        accept=findViewById(R.id.acceptButton);
        delete=findViewById(R.id.deleteButton);


        applicationid.setText(applicationId);

        // fetch data from Firebase and fill the form
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Applications").child(applicationId);
// Fetch data from Firebase
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the ApplicationFormModel object from Firebase
                ApplicationFormModel model = dataSnapshot.getValue(ApplicationFormModel.class);

                if (model != null) {
                    // Set the data to the views
                    nameValue.setText(model.getName());
                    birthdateValue.setText(model.getBirthdate());
                    phoneValue.setText(model.getPhone());
                    emailValue.setText(model.getEmail());
                    addressValue.setText(model.getStreetAddress()+", "+model.getWard()+", "+model.getCity()+", "+model.getZip());
                    nidValue.setText(model.getId());
                    experienceValue.setText(model.getExperiences());

                    // Set the image URL to the ImageView using Glide
                    if (model.getPhotoUrl() != null && !model.getPhotoUrl().isEmpty()) {
                        Glide.with(FormView.this)
                                .load(model.getPhotoUrl())
                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                                .placeholder(R.drawable.baseline_person_24) // Default placeholder image
                                .into(photoValue);
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

        //entered id
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newid = id.getText().toString();
                if(newid.length()!=6)
                {
                    Toast.makeText(FormView.this, "ID must be 6 digit", Toast.LENGTH_SHORT).show();
                }
                else {

                   DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user")
                            .child(applicationId)
                            .child("userProfile");
                    databaseReference.setValue("2");

                    // fetch data from Firebase and fill the form
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Applications").child(applicationId);
// Fetch data from Firebase
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get the ApplicationFormModel object from Firebase
                            ApplicationFormModel model = dataSnapshot.getValue(ApplicationFormModel.class);

                            if (model != null) {
                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Profiles").child(applicationId);
                                ProfileModel profile =new ProfileModel(newid,model.getName(),model.getBirthdate(),model.getUid(),model.getPhone(),model.getEmail(),model.getStreetAddress(),model.getWard(),model.getCity(),model.getZip(),model.getId(),model.getExperiences(),model.getPhotoUrl());
                                reference2.setValue(profile).addOnSuccessListener(unused -> {
                                    Toast.makeText(FormView.this, "Accepted", Toast.LENGTH_SHORT).show();
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(FormView.this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Applications").child(applicationId);
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
    Toast.makeText(FormView.this, "Deleted", Toast.LENGTH_SHORT).show();
    finish();

}).addOnFailureListener(e -> {
                    Toast.makeText(FormView.this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
}