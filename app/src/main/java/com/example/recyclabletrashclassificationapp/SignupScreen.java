package com.example.recyclabletrashclassificationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.chat.databinding.ActivityAuthenticationBinding;
//import com.example.chat.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignupScreen extends AppCompatActivity {
    String name,email,pass;

    EditText edtname,edtEmail,edtPass;
    TextView btnSignUp,btnLogIn;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);

        databaseReference= FirebaseDatabase.getInstance().getReference("user");
        edtname = findViewById(R.id.username);
        edtEmail = findViewById(R.id.useremail);
        edtPass = findViewById(R.id.password);
        btnLogIn = findViewById(R.id.loginText);
        btnSignUp = findViewById(R.id.signupButton);


        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent sign = new Intent(SignupScreen.this,LoginScreen.class);
                startActivity(sign);
            }

        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name=edtname.getText().toString();
                email=edtEmail.getText().toString();
                pass = edtPass.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(),
                                    "Please enter email!!",
                                    Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(),
                                    "Please enter name!!",
                                    Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(getApplicationContext(),
                                    "Please enter password!!",
                                    Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (pass.length()<6) {
                    Toast.makeText(getApplicationContext(),
                                    "Password length can't be less then 6 char",
                                    Toast.LENGTH_LONG)
                            .show();
                    return;}
             
                signUp();
            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            startActivity(new Intent(SignupScreen.this,MainActivity.class));
            finish();
        }
    }






    private void signUp() {
        FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(email.trim(),pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        UserProfileChangeRequest userProfileChangeRequest= new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                        firebaseUser.updateProfile(userProfileChangeRequest);
                        UserModel userModel= new UserModel(FirebaseAuth.getInstance().getUid(),name,email,"0");
                        databaseReference.child(FirebaseAuth.getInstance().getUid()).setValue(userModel);
                        startActivity(new Intent(SignupScreen.this,MainActivity.class));
                        finish();
                    }

                }) .addOnFailureListener(e ->
                        Toast.makeText(SignupScreen.this, "Signup unsuccessful: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );








    }
}