package com.lenlobo.evilgram.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {
    public static final String TAG = "FeedFragment";

    List<Post> posts = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        // get reference to recycler view
        final RecyclerView rvFeed = (RecyclerView) rootView.findViewById(R.id.rvFeed);
        final ProgressBar prgoBarFeed = (ProgressBar) rootView.findViewById(R.id.progBarFeed);

        rvFeed.setLayoutManager(new LinearLayoutManager(getActivity()));

        final FeedAdapter feedAdapter = new FeedAdapter(posts);
        rvFeed.setAdapter(feedAdapter);

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        rvFeed.setVisibility(View.INVISIBLE);
        prgoBarFeed.setVisibility(View.VISIBLE);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> parsePosts, ParseException e) {
                if (e == null) {
                    posts.clear();
                    for (Post post: parsePosts) {
                        Post newPost = new Post();
                        newPost.username = post.getUser().getUsername();
                        newPost.description = post.getDescription();
                        posts.add(newPost);
                    }
                } else {
                    Log.e(TAG, "Error getting posts", e);
                }
                prgoBarFeed.setVisibility(View.INVISIBLE);
                rvFeed.setVisibility(View.VISIBLE);
                feedAdapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}