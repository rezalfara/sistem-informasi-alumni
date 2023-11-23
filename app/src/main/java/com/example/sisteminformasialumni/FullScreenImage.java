package com.example.sisteminformasialumni;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class FullScreenImage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        Log.d("FullScreenImage", "Before getting intent");
        // Retrieve the photo URL from the intent
        String imgUrl = getIntent().getStringExtra("imgUrl");

        Log.d("FullScreenImage", "Before getting intent" + imgUrl);
        // Use the imgUrl as needed, e.g., load it into an ImageView
        ImageView imageView = findViewById(R.id.fullscreenImageView);

        Glide.with(this)
                .load(imgUrl)
                .into(imageView);



    }
}