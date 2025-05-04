package com.example.recyclabletrashclassificationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminPanel extends AppCompatActivity {

    private TextView tvTotalUsers, tvTotalDealers, tvTotalCollectors;
    private DatabaseReference usersRef, dealersRef, collectorsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel); // match with your XML file name
//Toolbar
        Toolbar toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Admin Panel");// Show back button
        // Handle back button click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close this activity
            }
        });

        // Initialize Firebase references
        usersRef = FirebaseDatabase.getInstance().getReference("user");
        dealersRef = FirebaseDatabase.getInstance().getReference("DealerProfiles");
        collectorsRef = FirebaseDatabase.getInstance().getReference("Profiles");

        // Initialize views
        tvTotalUsers = findViewById(R.id.tv_total_users);
        tvTotalDealers = findViewById(R.id.tv_total_dealers);
        tvTotalCollectors = findViewById(R.id.tv_total_collectors);

        fetchCounts();

        // Button click listeners
    /*

        findViewById(R.id.option_dealer_profiles).setOnClickListener(v ->
                startActivity(new Intent(this, DealerProfilesActivity.class))
        );

      

        findViewById(R.id.option_collector_profiles).setOnClickListener(v ->
                startActivity(new Intent(this, CollectorProfilesActivity.class))
        );*/
        findViewById(R.id.option_dealer_applications).setOnClickListener(v ->
                startActivity(new Intent(this, DealerApplicationsView.class))
        );
        findViewById(R.id.option_collector_applications).setOnClickListener(v ->
                startActivity(new Intent(this, Administration.class))
        );
    }

    private void fetchCounts() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvTotalUsers.setText("Total Users: " + snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tvTotalUsers.setText("Total Users: Error");
            }
        });

        dealersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvTotalDealers.setText("Total Dealers: " + snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tvTotalDealers.setText("Total Dealers: Error");
            }
        });

        collectorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvTotalCollectors.setText("Total Collectors: " + snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tvTotalCollectors.setText("Total Collectors: Error");
            }
        });
    }
}