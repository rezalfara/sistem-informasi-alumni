package com.example.sisteminformasialumni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class FullScreenImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        ImageView fullscreenImage = findViewById(R.id.fullscreenImageView);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("imgUrl")) {
            String imgUrl = intent.getStringExtra("imgUrl");

            // Menggunakan Glide untuk menampilkan gambar dari URL ke ImageView
            Glide.with(this)
                    .load(imgUrl)
                    .into(fullscreenImage);
        }
    }
}