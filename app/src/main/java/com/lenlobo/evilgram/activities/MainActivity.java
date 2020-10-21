package com.lenlobo.evilgram.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lenlobo.evilgram.R;
import com.lenlobo.evilgram.fragments.FeedFragment;
import com.lenlobo.evilgram.fragments.PostFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        final Fragment feedFragment = new FeedFragment();
        final Fragment postFragment = new PostFragment();

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
                        fragment = postFragment;
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
}