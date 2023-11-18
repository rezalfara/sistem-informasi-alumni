package com.example.sisteminformasialumni;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class FullScreenImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        ImageView fullscreenImage = findViewById(R.id.fullscreenImageView);

        // Get the image URL from the intent
        String imgUrl = getIntent().getStringExtra("imgUrl");

        // Load the image into the full-screen ImageView using Glide
        Glide.with(this)
                .load(imgUrl)
                .into(fullscreenImage);
    }
}