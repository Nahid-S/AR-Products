package com.example.pika3d;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;
    private ActivityResultLauncher<Intent> takePictureLauncher;
    private ActivityResultLauncher<String> pickImageLauncher;
    private Uri imageUri; // Store the last selected image URI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views
        CardView btnPic = findViewById(R.id.btnPic);
        CardView btnGal = findViewById(R.id.btnGal); // New CardView for gallery
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        // Activity Result Launcher for capturing an image
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Bundle extras = data.getExtras();
                        if (extras != null && extras.containsKey("data")) {
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            imageView.setImageBitmap(imageBitmap);
                            // Store the captured image URI for processing
                            // Example: imageUri = getImageUri(getApplicationContext(), imageBitmap);
                        }
                    }
                });

        // Activity Result Launcher for picking image from gallery
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        imageUri = result;
                        imageView.setImageURI(imageUri);
                    }
                });

        // Set click listener on the CardView for capturing an image
        btnPic.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureLauncher.launch(takePictureIntent);
        });

        // Set click listener on the CardView for picking image from gallery
        btnGal.setOnClickListener(v -> {
            pickImageLauncher.launch("image/*");
        });
    }
}
