package com.example.recyclabletrashclassificationapp;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class DealerProfilesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DealerProfileAdapter adapter;
    private List<DealerProfileModel> dealerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_profiles);

        Toolbar toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dealer Profiles");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dealerList = new ArrayList<>();
        adapter = new DealerProfileAdapter(this, dealerList);
        recyclerView.setAdapter(adapter);

        loadDealerProfiles();
    }

    private void loadDealerProfiles() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("DealerProfiles");
        dealerList.clear();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    DealerProfileModel model = snap.getValue(DealerProfileModel.class);
                    if (model != null) {
                        dealerList.add(model);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DealerProfilesActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
