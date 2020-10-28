package com.lenlobo.evilgram.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.lenlobo.evilgram.R;

public class DetailActivity extends AppCompatActivity {

    private String username;
    private String createdAt;
    private String description;
    private String imageUrl;

    private TextView tvUsername;
    private TextView tvCreatedAt;
    private TextView tvDescription;
    private ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        username = getIntent().getStringExtra("username");
        createdAt = getIntent().getStringExtra("createdAt");
        description = getIntent().getStringExtra("description");
        imageUrl = getIntent().getStringExtra("imageUrl");

        tvUsername = findViewById(R.id.tvUsername);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvDescription = findViewById(R.id.tvDescription);
        ivPhoto = findViewById(R.id.ivPhoto);

        tvUsername.setText(username);
        tvCreatedAt.setText(createdAt);
        tvDescription.setText(description);

        Glide.with(this).load(imageUrl).into(ivPhoto);
    }
}