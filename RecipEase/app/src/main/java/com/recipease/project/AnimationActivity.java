package com.recipease.project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

/**
 * Created by andrewratz on 3/26/18.
 */

public class AnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        Toast.makeText(AnimationActivity.this, "Account successfully created", Toast.LENGTH_SHORT).show();
        Glide.with(this).load(R.raw.recipease_logo_animated_final)
                .into(new GlideDrawableImageViewTarget((ImageView) findViewById(R.id.ivAnimation)));
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(AnimationActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        }, 5000);
    }

}
