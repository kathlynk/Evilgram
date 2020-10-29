package com.lenlobo.evilgram.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lenlobo.evilgram.R;
import com.lenlobo.evilgram.activities.LoginActivity;
import com.lenlobo.evilgram.activities.MainActivity;
import com.lenlobo.evilgram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class ProfileFragment extends FeedFragment {

    @Override
    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> parsePosts, ParseException e) {
                if (e == null) {
                    adapter.clear();
                    adapter.addAll(parsePosts);
                    progBarFeed.setVisibility(View.INVISIBLE);

                } else {
                    Log.e(TAG, "Error getting posts", e);
                }

            }
        });
    }

    @Override
    protected void refreshPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.orderByDescending("createdAt");
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> parsePosts, ParseException e) {
                if (e == null) {
                    adapter.clear();
                    adapter.addAll(parsePosts);
                    swipeContainer.setRefreshing(false);

                } else {
                    Log.e(TAG, "Error getting posts", e);
                }

            }
        });
    }

    @Override
    protected void loadNextDataFromParse(int offset) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.orderByDescending("createdAt");
        query.setLimit(DISPLAY_LIMIT);
        query.setSkip(offset);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> parsePosts, ParseException e) {
                if (e == null) {
                    adapter.addAll(parsePosts);
                } else {
                    Log.e(TAG, "Error getting posts", e);
                }

            }
        });
    }
}