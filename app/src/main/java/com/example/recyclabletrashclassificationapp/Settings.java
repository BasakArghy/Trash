package com.example.recyclabletrashclassificationapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class Settings extends AppCompatActivity {

    private SwitchCompat switchNotifications, switchTheme;
    public static SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        Toolbar toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back button
        getSupportActionBar().setTitle("Settings");
        // Handle back button click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close this activity
            }
        });


        //settings

        prefs = getSharedPreferences("settings_prefs", MODE_PRIVATE);

        // === Switches ===
        switchNotifications = findViewById(R.id.switch_notifications);
        switchTheme = findViewById(R.id.switch_theme);

        // Load saved preferences
        switchNotifications.setChecked(prefs.getBoolean("notifications", true));
        switchTheme.setChecked(prefs.getBoolean("dark_theme", false));

        // Handle notification toggle
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("notifications", isChecked).apply();
            Toast.makeText(this, "Notifications " + (isChecked ? "enabled" : "disabled"), Toast.LENGTH_SHORT).show();
        });

        // Handle theme toggle
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("dark_theme", isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
            recreate();
        });

        // === Clickable Options ===
        setupClick(R.id.option_view_profile, () -> {
            // Replace with your activity
            startActivity(new Intent(this, Profile.class));
        });

        setupClick(R.id.option_change_profile_pic, () -> {
            // You can launch camera or gallery picker
            startActivity(new Intent(this, ChangeProfilePic.class));
        });

        setupClick(R.id.option_change_name, this::showChangeNameDialog);

        setupClick(R.id.option_change_password,this::changePassword);

        setupClick(R.id.option_feedback, this::sendFeedbackEmail);

        setupClick(R.id.option_help, () -> {
            startActivity(new Intent(this,Help.class));
        });

        setupClick(R.id.option_invite, this::inviteFriends);

        setupClick(R.id.option_delete_account, this::confirmAndDeleteAccount);

    }



    private void setupClick(int id, Runnable action) {
        LinearLayout layout = findViewById(id);
        if (layout != null) {
            layout.setOnClickListener(v -> action.run());
        }
    }
    private void changePassword() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = auth.getCurrentUser().getEmail().toString();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Password reset email sent.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    private void showChangeNameDialog() {
        EditText input = new EditText(this);
        input.setHint("Enter new name");

        new AlertDialog.Builder(this)
                .setTitle("Change Name")
                .setView(input)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if (user != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(newName) // <-- Set new name here
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "Name updated successfully", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            String uid = FirebaseAuth.getInstance().getUid();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user");
                            databaseReference.child(uid).child("userName").setValue(newName);
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        String uidd = dataSnapshot.getKey();
                                        if (uidd.equals(uid)) {
                                            UserModel userModel = dataSnapshot.getValue(UserModel.class);
                                            if(userModel.getUserProfile().equals("2")) {

                                                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Profiles");
                                                databaseReference2.child(uid).child("name").setValue(newName);
                                            }
                                            if(userModel.getUserProfile().equals("4")) {

                                                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("DealerProfiles");
                                                databaseReference2.child(uid).child("name").setValue(newName);
                                            }
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Toast.makeText(Settings.this, "Failed to read data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        });
                        }

                    } else {
                        Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void sendFeedbackEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:basakaronno@gmail.com")); // Replace with your email
        intent.putExtra(Intent.EXTRA_SUBJECT, "App Feedback");
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
        }
    }

    private void inviteFriends() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this app!");
        intent.putExtra(Intent.EXTRA_TEXT, "Try this awesome app: https://www.trashrecycleapp.com");
        startActivity(Intent.createChooser(intent, "Invite via"));
    }

    private void confirmAndDeleteAccount() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    String uid=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();



                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Applications").child(uid);
                    reference2.removeValue();


                    StorageReference folderRef2 = FirebaseStorage.getInstance("gs://chat-55084.appspot.com")
                            .getReference()
                            .child("history")
                            .child(uid);

                    folderRef2.listAll()
                            .addOnSuccessListener(listResult -> {
                                for (StorageReference item : listResult.getItems()) {
                                    item.delete()
                                            .addOnSuccessListener(aVoid -> Log.d("Firebase", "Deleted: " + item.getName()))
                                            .addOnFailureListener(e -> Log.e("Firebase", "Failed to delete: " + item.getName(), e));
                                }
                            })
                            .addOnFailureListener(e -> Log.e("Firebase", "Failed to list files in profile/" + uid, e));

                    FirebaseStorage.getInstance("gs://chat-55084.appspot.com").getReference("Applications").child(uid).delete();
                    FirebaseStorage.getInstance("gs://chat-55084.appspot.com").getReference("DealerApplications").child(uid).delete();
                    FirebaseDatabase.getInstance().getReference("user").child(uid).removeValue();
                   FirebaseDatabase.getInstance().getReference("history").child(uid).removeValue();
                    FirebaseDatabase.getInstance().getReference("Profiles").child(uid).removeValue();
                    FirebaseDatabase.getInstance().getReference("DealerProfiles").child(uid).removeValue();
                    FirebaseAuth.getInstance().getCurrentUser().delete()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show();
                                    // Redirect to login or exit app
                                    startActivity(new Intent(this, LoginScreen.class));
                                    finish();
                                } else {
                                    Toast.makeText(this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}