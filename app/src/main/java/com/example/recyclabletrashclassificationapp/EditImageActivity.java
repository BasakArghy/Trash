package com.example.recyclabletrashclassificationapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class EditImageActivity extends AppCompatActivity {

    ImageView imageView;
    Button confirmButton, editButton;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        imageView = findViewById(R.id.previewImageView);
        confirmButton = findViewById(R.id.confirmButton);
        editButton = findViewById(R.id.editButton);

        image = GlobalBitmapHolder.bitmap;

        if (image != null) {
            imageView.setImageBitmap(image);
        }

        // Confirm button returns the image as-is
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnImage(image);
            }
        });

        // Edit button opens crop activity
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri sourceUri = ImageUtils.getImageUri(EditImageActivity.this, image);
                Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropped.jpg"));

                UCrop.of(sourceUri, destinationUri)
                        .withAspectRatio(1, 1)
                        .withOptions(getOptions())
                        .start(EditImageActivity.this);

            }
        });



        Toolbar toolbar = findViewById(R.id.tb);


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back button


// Handle back button click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close this activity
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                try {
                    Bitmap cropped = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    returnImage(cropped);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to load cropped image", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Toast.makeText(this, "Crop error: " + (cropError != null ? cropError.getMessage() : "Unknown error"), Toast.LENGTH_SHORT).show();
        }
    }

    private void returnImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("editedImage", byteArray);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
    // uCrop Options
    private UCrop.Options getOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setFreeStyleCropEnabled(true);
        options.setHideBottomControls(false);
        return options;
    }
}
