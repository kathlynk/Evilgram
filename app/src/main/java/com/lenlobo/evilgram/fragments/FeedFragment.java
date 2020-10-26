package com.lenlobo.evilgram.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lenlobo.evilgram.R;
import com.lenlobo.evilgram.adapters.FeedAdapter;
import com.lenlobo.evilgram.models.Post;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {
    public static final String TAG = "FeedFragment";

    private RecyclerView rvFeed;
    private ProgressBar progBarFeed;
    private SwipeRefreshLayout swipeContainer;
    private FeedAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvFeed = view.findViewById(R.id.rvFeed);
        swipeContainer = view.findViewById(R.id.swipeContainer);

        progBarFeed = view.findViewById(R.id.progBarFeed);
        progBarFeed.setVisibility(View.VISIBLE);
        adapter = new FeedAdapter(getContext());

        rvFeed.setAdapter(adapter);
        rvFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        queryPosts();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPosts();
            }
        });


    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
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

    private void refreshPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.orderByDescending("createdAt");
        query.setLimit(20);
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
}