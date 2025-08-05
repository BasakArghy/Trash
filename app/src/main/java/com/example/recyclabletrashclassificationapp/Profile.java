package com.example.recyclabletrashclassificationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    DatabaseReference databaseReference,reff;
    String uidd;
    LinearLayout optional;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    MenuItem dealer,chat,map,collect;

    private TextView nameValue, emailValue, applicationIdValue, birthdateValue, phoneValue, addressValue, nidValue, experienceValue,addressname;
    private LinearLayout optionalSection;
    private ImageView photoValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar =  findViewById(R.id.aa);
        setSupportActionBar(toolbar);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

      // Initialize Views
        nameValue = findViewById(R.id.nameValue);
        emailValue = findViewById(R.id.emailValue);
        applicationIdValue = findViewById(R.id.applicationIdValue);
        birthdateValue = findViewById(R.id.birthdateValue);
        phoneValue = findViewById(R.id.phoneValue);
        addressValue = findViewById(R.id.addressValue);
        nidValue = findViewById(R.id.nidValue);
        experienceValue = findViewById(R.id.experienceValue);

        photoValue = findViewById(R.id.photoValue);
        addressname=findViewById(R.id.addressLabel);



        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
      String name = currentUser.getDisplayName();
        String email = currentUser.getEmail();
        emailValue.setText(email);
        nameValue.setText(name);

        //checking profile value
        optional=findViewById(R.id.optional);

        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        uidd = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String uid = dataSnapshot.getKey();
                    if (uid.equals(uidd)) {
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        if(userModel.getUserProfile().equals("2")){
                            reff=FirebaseDatabase.getInstance().getReference("Profiles");
                            optional.setVisibility(View.VISIBLE);
                           reff.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        String uid = dataSnapshot.getKey();
                                        if (uid.equals(uidd)) {
                                    ProfileModel profile=dataSnapshot.getValue(ProfileModel.class);
                                            applicationIdValue.setText(profile.getId());
                                            birthdateValue.setText(profile.getBirthdate());
                                            phoneValue.setText(profile.getPhone());
                                            String fullAddress = "Street: "+profile.getStreetAddress() + ", Ward: " + profile.getWard() + ", City: " + profile.getCity() + ", Post: " + profile.getZip();
                                            addressValue.setText(fullAddress);
                                            nidValue.setText(profile.getNid());
                                            experienceValue.setText(profile.getExperiences());
                                            if (profile.getPhotoUrl() != null && !profile.getPhotoUrl().isEmpty()) {
                                                Glide.with(Profile.this)
                                                        .load(profile.getPhotoUrl())
                                                        .placeholder(R.drawable.baseline_person_24)
                                                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))// fallback placeholder
                                                        .into(photoValue);
                                            } else {
                                                photoValue.setImageResource(R.drawable.baseline_person_24);
                                            }
                                        }


                                    }
                                }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                Toast.makeText(Profile.this, "Failed to read data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });



                        }
                        //dealer
                        else if(userModel.getUserProfile().equals("5")){
                            reff=FirebaseDatabase.getInstance().getReference("DealerProfiles");
                            optional.setVisibility(View.VISIBLE);
                            reff.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        String uid = dataSnapshot.getKey();
                                        if (uid.equals(uidd)) {
                                            DealerProfileModel profile=dataSnapshot.getValue(DealerProfileModel.class);
                                            applicationIdValue.setText(profile.getId());
                                            birthdateValue.setText(profile.getBirthdate());
                                            phoneValue.setText(profile.getPhone());
                                            addressname.setText("Location");
                                            String fullAddress = "Lat: "+profile.getLat() +"\nLng: "+profile.getLan();
                                            addressValue.setText(fullAddress);
                                            nidValue.setText(profile.getNid());
                                            experienceValue.setText(profile.getExperiences());
                                            if (profile.getPhotoUrl() != null && !profile.getPhotoUrl().isEmpty()) {
                                                Glide.with(Profile.this)
                                                        .load(profile.getPhotoUrl())
                                                        .placeholder(R.drawable.baseline_person_24)
                                                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))// fallback placeholder
                                                        .into(photoValue);
                                            } else {
                                                photoValue.setImageResource(R.drawable.baseline_person_24);
                                            }
                                        }


                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Toast.makeText(Profile.this, "Failed to read data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });



                        }


                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Profile.this, "Failed to read data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        //drawer

        drawerLayout=findViewById(R.id.layoutDrawer);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.naviDrawer);


        // Get the menu from NavigationView
        Menu menu = navigationView.getMenu();

// Find the menu item you want to show/hide
        MenuItem specialItem = menu.findItem(R.id.admin);// Replace with your actual menu item ID

        dealer= menu.findItem(R.id.dealer);
        collect= menu.findItem(R.id.apply);
        chat=menu.findItem(R.id.chat);
        map=menu.findItem(R.id.map);

// Set default selected item to Home
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.nav_history) {
                startActivity(new Intent(Profile.this, History.class));
                finish();
                overridePendingTransition(0, 0);
                return true;
            }
            else if(item.getItemId() == R.id.nav_profile) {

                return true;
            }
            else if(item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(Profile.this, MainActivity.class));
                finish();
                overridePendingTransition(0, 0);

                return true;
            }

            return false;
        });

        //administration visibility

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(userId);
            userRef.get().addOnSuccessListener(dataSnapshot -> {
                if (dataSnapshot.exists()) {
                    UserModel user = dataSnapshot.getValue(UserModel.class);
                    if (user != null) {

                        String userEmail = user.getUserEmail();


                        if(userEmail.equals("admin@gmail.com")){
                            specialItem.setVisible(true);
                            dealer.setVisible(false);
                            collect.setVisible(false);

                        }
                        else if(user.getUserProfile().equals("2")){
                            chat.setVisible(true);
                            map.setVisible(true);
                            dealer.setVisible(false);
                            collect.setVisible(false);
                        }
                        else if(user.getUserProfile().equals("5")){
                            chat.setVisible(true);
                            map.setVisible(true);
                            dealer.setVisible(false);
                            collect.setVisible(false);
                        }


                    }
                }
            }).addOnFailureListener(e -> {

            });
        }


        //navigation drawer

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
              /*  if(item.getItemId()==R.id.about)
                {
                    Intent iabout = new Intent(Profile.this, Reclycle_Process.class);
                    startActivity(iabout);
                }
*/
              if (item.getItemId()==R.id.Settings){
                    Intent isetti = new Intent(Profile.this,Settings.class);
                    startActivity(isetti);
                }
                else if (item.getItemId()==R.id.admin){
                    Intent isetti = new Intent(Profile.this, AdminPanel.class);
                    startActivity(isetti);
                }
                else if (item.getItemId()==R.id.apply){
                    Intent isetti = new Intent(Profile.this,ApplyForPermit.class);
                    startActivity(isetti);
                }
                else if (item.getItemId()==R.id.dealer) {

                    Intent idealer = new Intent(Profile.this,ApplyAsDealer.class);
                    startActivity(idealer);
                }
                else if (item.getItemId()==R.id.chat) {

                    Intent idealer = new Intent(Profile.this,ChatActivity.class);
                    startActivity(idealer);
                }
                else if (item.getItemId()==R.id.map) {

                    Intent idealer = new Intent(Profile.this,MapView.class);
                    startActivity(idealer);
                }
                else if(item.getItemId()==R.id.alogOut) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(Profile.this, LoginScreen.class));
                    finish();
                }

                return false;
            }




        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}