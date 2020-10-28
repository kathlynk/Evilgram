package com.lenlobo.evilgram.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lenlobo.evilgram.R;
import com.lenlobo.evilgram.fragments.FeedFragment;
import com.lenlobo.evilgram.fragments.PostFragment;
import com.lenlobo.evilgram.fragments.SettingsFragment;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public final static int PICK_PHOTO_CODE = 1046;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        final Fragment feedFragment = new FeedFragment();
        final Fragment postFragment = new PostFragment();
        final Fragment settingsFragment = new SettingsFragment();

        fragmentManager.beginTransaction()
                .add(R.id.main_content, feedFragment)
                .commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        fragment = feedFragment;
                        break;
                    case R.id.action_post:
                        fragment = postFragment;
                        break;
                    case R.id.action_settings:
                        fragment = settingsFragment;
                        break;
                    default:
                        fragment = feedFragment;
                        break;

                }
                fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;
            case R.id.upload:
                pickPhoto();
                return true;
            default:
                return true;

        }
    }

    private void logout() {
        ParseUser.logOut();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) !=  null) {
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if(Build.VERSION.SDK_INT > 27){
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((data != null) && requestCode == PICK_PHOTO_CODE) {
            Uri photoUri = data.getData();

            // Load the image located at photoUri into selectedImage
            Bitmap selectedImage = loadFromUri(photoUri);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] image = stream.toByteArray();
            ParseFile file = new ParseFile("profile.png", image);
            ParseUser.getCurrentUser().put("profilePhoto", file);
            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(MainActivity.this, "Profile Photo Uploaded", Toast.LENGTH_SHORT).show();
                        //TODO: This is hacky...maybe launch the profile page
                        finish();
                        startActivity(getIntent());
                    } else {
                        Toast.makeText(MainActivity.this, "Error Uploading Profile Photo", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

}