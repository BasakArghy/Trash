package com.example.recyclabletrashclassificationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DealerApplicationsView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_applications_view);

        //Toolbar

        Toolbar toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Collector Applications");// Show back button
        // Handle back button click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close this activity
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        // Reload your data here
        // For example:
        loadDataAgain();
    }

    private void loadDataAgain() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<DealerApplyFormModel> list = new ArrayList<>();
        DealerFormAdapter adapter = new DealerFormAdapter(this,DealerApplicationsView.this, list);
        recyclerView.setAdapter(adapter);

// Now fetch data from Firebase and update list
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DealerApplications");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DealerApplyFormModel applicationFormModel = dataSnapshot.getValue(DealerApplyFormModel.class);
                    list.add(applicationFormModel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DealerApplicationsView.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}