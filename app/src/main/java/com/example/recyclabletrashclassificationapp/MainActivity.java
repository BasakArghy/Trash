package com.example.recyclabletrashclassificationapp;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recyclabletrashclassificationapp.ml.Model;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    public static Object instance;
    private List<String> itemList;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    TextView result;
    ImageView imageView;
    Button  confidence,gallery;
    MenuItem dealer,chat,map,collect;
    ImageButton picture;
    String ss= "";
    int position=0;

    String ans ="";
    int imageSize = 224;
    private androidx.appcompat.widget.Toolbar toolbar;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply saved theme BEFORE inflating UI
        if (PreferenceManager.isDarkModeEnabled(this)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        toolbar =  findViewById(R.id.tb);
        setSupportActionBar(toolbar);

        result = findViewById(R.id.result);
        confidence = findViewById(R.id.confidencesText);
        gallery = findViewById(R.id.gallery);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.Button);



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





        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });






// List of items (Paper, Plastics, etc.)
        itemList = Arrays.asList("Paper", "Plastics", "Glass", "Metal", "Electronics", "Clothes", "Shoe");


confidence.setEnabled(false);

        confidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent confi = new Intent(MainActivity.this, Confidences.class);
                confi.putExtra("con",ss);
                startActivity(confi);*/
                Intent intent = new Intent(MainActivity.this, ReadMore.class);
                intent.putExtra("item_index", position);
               // intent.putExtra("item_name", itemList.get(position));// Pass the index
                intent.putExtra("item_name", ss);// Pass the index
                startActivity(intent);
            }
        });


        gallery.setOnClickListener(view -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, 2);
        });












        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               /* if(item.getItemId()==R.id.about)
                {
                    Intent iabout = new Intent(MainActivity.this, Reclycle_Process.class);
                    startActivity(iabout);
                }*/

              if (item.getItemId()==R.id.dealer) {

                    Intent idealer = new Intent(MainActivity.this,ApplyAsDealer.class);
                    startActivity(idealer);
                }
                else if (item.getItemId()==R.id.Settings){
                    Intent isetti = new Intent(MainActivity.this,Settings.class);
                    startActivity(isetti);
                }
                else if (item.getItemId()==R.id.admin){
                    Intent isetti = new Intent(MainActivity.this,AdminPanel.class);
                    startActivity(isetti);
                }
                else if (item.getItemId()==R.id.apply){
                    Intent isetti = new Intent(MainActivity.this,ApplyForPermit.class);
                    startActivity(isetti);
                }
                else if (item.getItemId()==R.id.chat) {

                    Intent idealer = new Intent(MainActivity.this,ChatActivity.class);
                    startActivity(idealer);
                }
                else if (item.getItemId()==R.id.map) {

                    Intent idealer = new Intent(MainActivity.this,MapView.class);
                    startActivity(idealer);
                }
                else if(item.getItemId()==R.id.alogOut) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, LoginScreen.class));
                    finish();
                }

                return false;
            }




        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

// Set default selected item to Home
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.nav_history) {
                startActivity(new Intent(MainActivity.this, History.class));
                finish();
                overridePendingTransition(0, 0);
                return true;
            }
            else if(item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(MainActivity.this, Profile.class));
                finish();
                overridePendingTransition(0, 0);
                return true;
            }
            else if(item.getItemId() == R.id.nav_home) {
                // Stay on Home
                return true;
            }

            return false;
        });



    }






    public void classifyImage(Bitmap image){
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            // get 1D array of 224 * 224 pixels in image
            int [] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            int pixel = 0;
            for(int i = 0; i < imageSize; i++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for(int i = 0; i < confidences.length; i++){
                if(confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            //String[] classes = {"glass_bottle","glass_jar","plastic_bottle","plastic_bucket","plastic_container","plastic_jugs_mug","newspaper","paper","shopping_bag","cardboard","Clothes","Can","NonRecyclable"};
            //String[] classes = {"glass","plastic","paper","metal","Clothes","NonRecyclable"};
            String[] classes = {"glass_bottle","glass_jar","plastic_bottle","plastic_bucket","plastic_container","plastic_jugs","plastic_mugs","newspaper","paper","shopping_bag","cardboard","Clothes","Can","NonRecyclable"};
            //String[] classes = {"Plastic Bottles","Brown Glasses","Cans","CardBoard","Clothes","Green Glasses","Leather Products","Leather Shoe","Newspaper","Polythene","White Glass","Non Recyclable"};
           String detected ="";
            if(confidences[maxPos]>.9)
           {
               if(maxPos<(classes.length-1)){
                   result.setText("Recyclable");
               }
               //result.setText(classes[maxPos]);
               else{
                   result.setText("Non Recyclable");
               }
              switch (maxPos){
                  case 0:
                  case 1:
                      position=2;
                      break;
                  case 2:
                  case 3:
                  case 4:
                  case 5:
                  case 6:
                      position=1;
                      break;
                  case 7:
                  case 8:
                  case 9:
                  case 10:
                      position=0;
                      break;
                  case 11:
                      position=5;
                      break;
                  case 12:
                      position=3;
                      break;
                  case 13:
position = -1;
                      break;

              }
           detected = classes[maxPos];
           }
           else{
               result.setText("Non Recyclable");
               detected ="Non Recyclable";
           }

            ans = classes[maxPos];

            String s = "";
            for(int i = 0; i < classes.length; i++){
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100);
            }
            ss=detected;

confidence.setEnabled(true);
            // Convert Bitmap to ByteArray
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] dataBytes = baos.toByteArray();

            // Upload to Firebase Storage
            uploadImageToFirebase(dataBytes,detected);


            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Bitmap image = null;

            if (requestCode == 1) { // Camera
                image = (Bitmap) data.getExtras().get("data");
            }  else if (requestCode == 2) { // Gallery
            try {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                if (image != null) {
                    imageView.setImageBitmap(image);
                    Bitmap resized = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                    classifyImage(resized);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
            } else if (requestCode == 3) { // After Edit
                byte[] byteArray = data.getByteArrayExtra("editedImage");
                if (byteArray != null) {
                    image = android.graphics.BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    imageView.setImageBitmap(image);
                    Bitmap resized = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                    classifyImage(resized);
                }
                return;
            }

            if (image != null && (requestCode == 1 )) {
                GlobalBitmapHolder.bitmap = image;
                Intent editIntent = new Intent(this, EditImageActivity.class);
                startActivityForResult(editIntent, 3);
            }
        }
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

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.alogOut)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this,LoginScreen.class));
            finish();
            return  true;
        } else if (item.getItemId()==R.id.aHistory) {
            Intent ihistory = new Intent(MainActivity.this,History.class);
            startActivity(ihistory);
        }
        else if (item.getItemId()==R.id.aSettings){
            Intent isetti = new Intent(MainActivity.this,Settings.class);
            startActivity(isetti);
        }
        else if(drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/




    private void uploadImageToFirebase(byte[] imageData, String detectedClass) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String imageFileName = "history/" + userId + "/" + timestamp + ".jpg";

        StorageReference storageRef = FirebaseStorage.getInstance("gs://chat-55084.appspot.com").getReference(imageFileName);

        // Upload image to Firebase Storage
        storageRef.putBytes(imageData).addOnSuccessListener(taskSnapshot ->
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();

                    saveHistoryToDatabase(userId, timestamp, imageUrl, detectedClass);
                }).addOnFailureListener(e ->
                        Toast.makeText(MainActivity.this, "Failed to retrieve image URL", Toast.LENGTH_SHORT).show()
                )
        ).addOnFailureListener(e ->
                Toast.makeText(MainActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show()
        );
    }


    private void saveHistoryToDatabase(String userId, String time, String imageUrl, String name) {
        DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference("history").child(userId);
        String historyId = historyRef.push().getKey(); // Unique ID for history entry

        if (historyId != null) {
            String formattedTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
            HistoryModel history = new HistoryModel(formattedTime, imageUrl, name,historyId);
            historyRef.child(historyId).setValue(history).addOnSuccessListener(aVoid ->
                    {}
            ).addOnFailureListener(e ->
                    {}
            );
        }
    }



}