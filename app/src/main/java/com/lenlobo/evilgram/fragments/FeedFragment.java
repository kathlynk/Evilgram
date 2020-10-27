package com.lenlobo.evilgram.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lenlobo.evilgram.EndlessRecyclerViewScrollListener;
import com.lenlobo.evilgram.R;
import com.lenlobo.evilgram.activities.DetailActivity;
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
    private EndlessRecyclerViewScrollListener scrollListener;
    private final int DISPLAY_LIMIT = 20;

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvFeed.setLayoutManager(linearLayoutManager);

        // gets initial list of posts
        queryPosts();

        // listener for swipe to refresh
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPosts();
            }
        });

        //scroll listener for infinite scroll
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromParse(totalItemsCount);
            }
        };

        rvFeed.addOnScrollListener(scrollListener);

    }

    // queries Parse for initial post list display
    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(DISPLAY_LIMIT);
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

    // refreshes post list for pull to refresh
    private void refreshPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.orderByDescending("createdAt");
        query.setLimit(DISPLAY_LIMIT);
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

    // adds additional Parse Posts to the adapter list for infinite scroll
    public void loadNextDataFromParse(int offset) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
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