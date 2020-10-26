package com.lenlobo.evilgram.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lenlobo.evilgram.R;
import com.lenlobo.evilgram.models.Post;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    public static final String TAG = "FeedAdapter";

    private Context context;
    private List<Post> posts;

    public FeedAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.feed_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUsername;
        public TextView tvDescription;
        public ImageView ivPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);

        }

        public void bind(Post post) {
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            if (post.getImage() != null) {
                Glide.with(context).load(post.getImage().getUrl()).into(ivPhoto);
            }

        }
    }


}
