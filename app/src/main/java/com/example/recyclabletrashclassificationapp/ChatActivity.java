package com.example.recyclabletrashclassificationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CollectorChatAdapter adapter;
    private DealerChatAdapter adapterD;
    private List<ProfileModel> collectorList;
    private List<DealerProfileModel> dealerList;

    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(" Chat ");
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.rv_collectors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        usersRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        usersRef.child("userProfile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String role = snapshot.getValue(String.class);
                if ("5".equalsIgnoreCase(role)) {
                    loadCollectorsFromFirebase();
                } else if("2".equalsIgnoreCase(role)) {

                    loadDealerFromFirebase();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, "Failed to fetch user role.", Toast.LENGTH_SHORT).show();
            }
        });

        collectorList = new ArrayList<>();
        adapter = new CollectorChatAdapter(this, collectorList);
        recyclerView.setAdapter(adapter);


    }

    private void loadCollectorsFromFirebase() {
        collectorList = new ArrayList<>();
        adapter = new CollectorChatAdapter(this, collectorList);
        recyclerView.setAdapter(adapter);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Profiles");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                collectorList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ProfileModel model = ds.getValue(ProfileModel.class);
                    collectorList.add(model);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadDealerFromFirebase() {
        dealerList = new ArrayList<>();
        adapterD = new DealerChatAdapter(this, dealerList);
        recyclerView.setAdapter(adapterD);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("DealerProfiles");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dealerList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    DealerProfileModel model = ds.getValue(DealerProfileModel.class);
                    dealerList.add(model);
                    
                }
                adapterD.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
