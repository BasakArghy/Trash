package com.example.recyclabletrashclassificationapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CollectorProfilesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CollectorProfileAdapter adapter;
    private List<ProfileModel> collectorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collector_profiles);

        Toolbar toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Collector Profiles");
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        collectorList = new ArrayList<>();
        adapter = new CollectorProfileAdapter(this, collectorList);
        recyclerView.setAdapter(adapter);

        loadCollectors();
    }

    private void loadCollectors() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Profiles");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                collectorList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ProfileModel model = dataSnapshot.getValue(ProfileModel.class);
                    model.setId(dataSnapshot.getKey());
                    collectorList.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CollectorProfilesActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
