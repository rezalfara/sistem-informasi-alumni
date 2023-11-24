package com.example.sisteminformasialumni;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
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
        ImageView imageView = findViewById(R.id.fullscreenImageView);

        Log.d("FullScreenImage", "Before getting intent");
        // Retrieve the base64 image data from the intent
        String imgUrl = getIntent().getStringExtra("imgUrl");
        String imgUrlUpdate = getIntent().getStringExtra("imgUrlUpdate");

        if (imgUrlUpdate!=null){
            Log.d("FullScreenImage", "imgUrlUpdate in FullScreenImage: " + imgUrlUpdate);
            // Use the base64Image as needed, e.g., decode it into a Bitmap and load it into an ImageView
            Bitmap decodedBitmap = decodeBase64ToBitmap(imgUrlUpdate);
            imageView.setImageBitmap(decodedBitmap);
        }else{
            Log.d("FullScreenImage", "imgUrl in FullScreenImage: " + imgUrl);
            // Load imgUrl into the ImageView using Glide
            Glide.with(this)
                    .load(imgUrl)
                    .into(imageView);
        }

    }

    private Bitmap decodeBase64ToBitmap(String base64String) {
        if (base64String != null) {
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } else {
            // Handle the case where base64String is null (return a default Bitmap or handle accordingly)
            return null;
        }
    }
}