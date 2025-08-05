package com.example.recyclabletrashclassificationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ReadMore extends AppCompatActivity {
  /*  private RecyclerView recyclerView;
    private StepAdapter stepAdapter;
    private List<StepItemModel> stepList;*/
    private DatabaseReference databaseSteps;
    private TextView recyclingDescriptionTextView,title;
    private ImageView recyclingImageView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_more);


        recyclingImageView=findViewById(R.id.recycling_image);
        recyclingDescriptionTextView=findViewById(R.id.recycling_process);
        title = findViewById(R.id.title);

        int itemIndex =getIntent().getIntExtra("item_index", -1);  // Default value -1
        String titlename = getIntent().getStringExtra("item_name");
      
        title.setText(titlename);

       // Toast.makeText(this, String.valueOf(itemIndex), Toast.LENGTH_SHORT).show();

if(itemIndex!= -1){

    recyclingDescriptionTextView = findViewById(R.id.recycling_process);
    recyclingImageView = findViewById(R.id.recycling_image);
    databaseSteps = FirebaseDatabase.getInstance().getReference("recycling_processes");

// Get the recycling process data from Firestore using the model class
    databaseSteps.child(String.valueOf(itemIndex))
            .get()
            .addOnSuccessListener(dataSnapshot -> {
                if (dataSnapshot.exists()) {
                    // Map Firestore document to RecycleProcessModel
                    RecycleProcessModel process = dataSnapshot.getValue(RecycleProcessModel.class);
                    if (process != null) {
                        //Toast.makeText(this, process.getItemName(), Toast.LENGTH_SHORT).show();
                        recyclingDescriptionTextView.setText(process.getItemDescription());
                        Glide.with(this).load(process.getImageUrl()).into(recyclingImageView);
                    }
                }
            })
            .addOnFailureListener(e -> {
                // Log error or notify user
                Log.e("Firestore", "Failed to load recycling process data", e);
            });


}


 /*
//storing data
        databaseSteps = FirebaseDatabase.getInstance().getReference("recycling_processes");


       RecycleProcessModel Paper =new RecycleProcessModel("Plastics", "♻️ 1. Collection\n Used plastic waste is collected from households, businesses, and recycling bins by waste management services.\n\n🧼 2. Sorting\n Plastics are sorted by type and color at recycling facilities. This can be done manually or using machines.\n Types of plastic:\n• #1 PET (e.g., water bottles)\n• #2 HDPE (e.g., milk jugs)\n• #5 PP (e.g., food containers)\n\n🧹 3. Cleaning\n Plastics are washed to remove labels, food residue, and glue.\n\n🔪 4. Shredding\n Clean plastic is shredded into flakes or pellets.\n\n🔥 5. Identification and Separation\n Plastic flakes are separated by properties like density or melting point.\n\n♨️ 6. Melting and Reforming\n The plastic is melted and formed into new products or pellets.\n\n✅ 7. Manufacturing New Products\n Recycled plastic is used to make containers, textiles, automotive parts, and more.","https://firebasestorage.googleapis.com/v0/b/chat-55084.appspot.com/o/recycling_processes%2FPlastics.png?alt=media&token=f8b44971-b627-47fa-9b21-090c0f17a99c");

        databaseSteps.child("1").setValue(Paper);
        RecycleProcessModel Paper =new RecycleProcessModel("Glass", "🏠 1. Collection\n Glass waste is collected from households, restaurants, bars, and recycling points.\n\n🧪 2. Sorting\n Glass is sorted by color (clear, green, brown) and contaminants like ceramics and metals are removed.\n\n🧼 3. Cleaning\n Glass is washed to remove labels, caps, and impurities.\n\n🔨 4. Crushing\n Clean glass is crushed into small pieces called cullet.\n\n🔥 5. Melting\n Cullet is melted in a furnace at about 1400°C to form molten glass.\n\n🏭 6. Reforming\n Molten glass is molded into new bottles, jars, or other glass products.\n\n🔁 7. Reuse\n Recycled glass products are used again, and the cycle repeats.","https://firebasestorage.googleapis.com/v0/b/chat-55084.appspot.com/o/recycling_processes%2FGlass.png?alt=media&token=462a7ee8-8659-4c6c-ae28-eaa9d292f92a");
        databaseSteps.child("2").setValue(Paper);

        RecycleProcessModel Paper1 =new RecycleProcessModel("Paper","📦 1. Collection\n Waste paper is collected from homes, offices, and recycling bins.\n\n📑 2. Sorting\n Paper is sorted into categories such as newspaper, cardboard, and office paper.\n\n🧼 3. Cleaning and Shredding\n Paper is cleaned to remove ink, staples, and glue, then shredded into small pieces.\n\n💧 4. Pulping\n The shredded paper is mixed with water and chemicals to create a slurry called pulp.\n\n🧲 5. De-Inking\n The pulp is processed to remove inks and dyes using flotation, screening, or washing.\n\n🧊 6. Drying\n The clean pulp is spread into sheets and passed through rollers to remove water and dry it.\n\n📃 7. Rolling and Reuse\n The dried paper sheets are rolled and sent to manufacturers to create new paper products.","https://firebasestorage.googleapis.com/v0/b/chat-55084.appspot.com/o/recycling_processes%2FPaper.png?alt=media&token=9fb22944-2faf-4288-b922-ed2916d3efc4");
        databaseSteps.child("0").setValue(Paper1);

        RecycleProcessModel Paper2 =new RecycleProcessModel("Metal","🏠 1. Collection\n Scrap metal is collected from households, factories, construction sites, and junkyards.\n\n🧲 2. Sorting\n Metals are sorted by type (ferrous and non-ferrous) using magnets and sensors.\n\n🧼 3. Cleaning\n Metal waste is cleaned to remove dirt, paint, plastic, or other non-metallic materials.\n\n🔨 4. Shredding\n Clean metal is shredded into smaller pieces for easier melting.\n\n🔥 5. Melting\n Shredded metal is melted in large furnaces, with each metal type having its own furnace.\n\n🏭 6. Purification\n The molten metal is purified to remove impurities using methods like electrolysis or gas injection.\n\n🧊 7. Solidifying and Transport\n The purified metal is cooled, solidified into bars or sheets, and transported to manufacturers for reuse.","https://firebasestorage.googleapis.com/v0/b/chat-55084.appspot.com/o/recycling_processes%2FMetal.png?alt=media&token=a3b4a4c6-a444-4a8a-aa0d-d48cca595d3d");
        databaseSteps.child("3").setValue(Paper2);

        RecycleProcessModel Paper3 =new RecycleProcessModel("Electronics","🧺 1. Collection\n Discarded electronic devices (e-waste) are collected from households, offices, and drop-off points.\n\n🧮 2. Sorting and Categorization\n Devices are sorted into categories (e.g., computers, phones, TVs) and tested for possible reuse or repair.\n\n🔧 3. Dismantling\n Electronics are manually disassembled to separate parts like batteries, circuit boards, screens, and plastic.\n\n🧲 4. Component Separation\n Components are sorted using magnets, eddy currents, and air classifiers to extract metals, glass, and plastics.\n\n🔥 5. Recovery and Processing\n Valuable materials like gold, copper, and silver are recovered using mechanical or chemical processes.\n\n🏭 6. Recycling or Disposal\n Recovered materials are sent to manufacturers for reuse. Hazardous waste is safely disposed of.\n\n🔁 7. Reuse and Manufacturing\n Usable parts and raw materials are used to create new electronic products, reducing the need for virgin materials.","https://firebasestorage.googleapis.com/v0/b/chat-55084.appspot.com/o/recycling_processes%2Felectronics.png?alt=media&token=cf718ab7-1dbe-41bb-8cff-579660673c5d");
        databaseSteps.child("4").setValue(Paper3);

        RecycleProcessModel Paper4 =new RecycleProcessModel("Clothes","🧺 1. Collection\n Used clothes are collected from donation bins, retail stores, or recycling programs.\n\n🧵 2. Sorting\n Clothes are sorted based on condition, material type (cotton, polyester, wool), and usability.\n\n🔄 3. Reuse\n Wearable clothes are cleaned and sent to second-hand stores or charities.\n\n✂️ 4. Shredding or Cutting\n Non-wearable clothes are shredded or cut into fibers or rags.\n\n🧪 5. Processing\n The fibers are processed into raw materials for insulation, stuffing, or new yarn/fabric.\n\n🧶 6. Spinning and Reweaving\n Fibers are spun into yarn and woven into new textiles for clothing or industrial use.\n\n✅ 7. New Products\n Recycled materials are used to make new clothes, cleaning cloths, mattresses, and more.","https://firebasestorage.googleapis.com/v0/b/chat-55084.appspot.com/o/recycling_processes%2FClothes.png?alt=media&token=9be5af78-eb5b-4345-9cba-bd5f16ef5d42");
        databaseSteps.child("5").setValue(Paper4);
        RecycleProcessModel Paper5 =new RecycleProcessModel("Shoe","📥 1. Collection\n Old or unwanted shoes are collected through recycling bins, retail drop-offs, or donation drives.\n\n👀 2. Inspection and Sorting\n Shoes are inspected and sorted based on material (leather, rubber, fabric) and condition (wearable or not).\n\n♻️ 3. Reuse\n Gently used shoes are cleaned and donated or resold through second-hand programs.\n\n🔧 4. Dismantling\n Non-reusable shoes are manually or mechanically separated into components like soles, uppers, and foam.\n\n🔄 5. Material Separation\n Materials such as rubber, leather, textiles, and plastics are separated for recycling.\n\n🔥 6. Processing and Recycling\n Recovered materials are ground, melted, or processed into raw forms (like rubber granules or fibers).\n\n✅ 7. New Product Manufacturing\n Recycled shoe materials are used to make flooring, athletic surfaces, new footwear, or insulation materials.","https://firebasestorage.googleapis.com/v0/b/chat-55084.appspot.com/o/recycling_processes%2FShoe.png?alt=media&token=638261ad-b96e-4b43-ad0c-be9fd7d9f7c9");
        databaseSteps.child("6").setValue(Paper5);
*/

        Toolbar toolbar = findViewById(R.id.tb);


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back button


//Handle back button click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close this activity
            }
        });
    }

}