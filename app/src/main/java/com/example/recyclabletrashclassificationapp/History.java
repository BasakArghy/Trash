package com.example.recyclabletrashclassificationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class History extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<HistoryModel> historyList;
    private DatabaseReference historyRef;
    private String userId;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    MenuItem dealer,chat,map,collect;
    private androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        toolbar =  findViewById(R.id.aa);
        setSupportActionBar(toolbar);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyList = new ArrayList<>();
        adapter = new HistoryAdapter(this, historyList);
        recyclerView.setAdapter(adapter);

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
        MenuItem specialItem = menu.findItem(R.id.admin); // Replace with your actual menu item ID

         dealer= menu.findItem(R.id.dealer);
        collect= menu.findItem(R.id.apply);
        chat=menu.findItem(R.id.chat);
        map=menu.findItem(R.id.map);



        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        historyRef = FirebaseDatabase.getInstance().getReference("history").child(userId);

        loadHistory();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);


// Set default selected item to Home
        bottomNavigationView.setSelectedItemId(R.id.nav_history);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.nav_history) {

                return true;
            }
            else if(item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(History.this, Profile.class));
                finish();
                overridePendingTransition(0, 0);
                return true;
            }
            else if(item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(History.this, MainActivity.class));
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
                    Intent iabout = new Intent(History.this, Reclycle_Process.class);
                    startActivity(iabout);
                }*/

                if (item.getItemId()==R.id.Settings){
                    Intent isetti = new Intent(History.this,Settings.class);
                    startActivity(isetti);
                }
                else if (item.getItemId()==R.id.admin){
                    Intent isetti = new Intent(History.this,AdminPanel.class);
                    startActivity(isetti);
                }
                else if (item.getItemId()==R.id.apply){
                    Intent isetti = new Intent(History.this,ApplyForPermit.class);
                    startActivity(isetti);
                }
                else if (item.getItemId()==R.id.chat) {

                    Intent idealer = new Intent(History.this,ChatActivity.class);
                    startActivity(idealer);
                }
                else if (item.getItemId()==R.id.dealer) {

                    Intent idealer = new Intent(History.this,ApplyAsDealer.class);
                    startActivity(idealer);
                }
                else if (item.getItemId()==R.id.map) {

                    Intent idealer = new Intent(History.this,MapView.class);
                    startActivity(idealer);
                }
                else if(item.getItemId()==R.id.alogOut) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(History.this, LoginScreen.class));
                    finish();
                }

                return false;
            }




        });

    }

    private void loadHistory() {
        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                historyList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    HistoryModel history = dataSnapshot.getValue(HistoryModel.class);
                    if (history != null) {
                        historyList.add(history);
                    }
                }
                Collections.reverse(historyList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(History.this, "Failed to load history", Toast.LENGTH_SHORT).show();
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