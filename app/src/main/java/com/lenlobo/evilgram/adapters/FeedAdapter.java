package com.lenlobo.evilgram.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lenlobo.evilgram.R;
import com.lenlobo.evilgram.activities.DetailActivity;
import com.lenlobo.evilgram.models.Post;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    public static final String TAG = "FeedAdapter";

    private Context context;
    private List<Post> posts;
    private ConstraintLayout itemContainer;

    public FeedAdapter(Context context) {
        this.context = context;
        this.posts = new ArrayList<>();
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
        public TextView tvCreatedAt;
        public ImageView ivProfilePhoto;
        public ToggleButton bHeart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
            tvCreatedAt = (TextView) itemView.findViewById(R.id.tvCreatedAt);
            ivProfilePhoto = (ImageView) itemView.findViewById(R.id.ivProfilePhoto);
            itemContainer = (ConstraintLayout) itemView.findViewById(R.id.itemContainer);
            bHeart = (ToggleButton) itemView.findViewById(R.id.bHeart);

        }

        public void bind(final Post post) {
            
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());

            //format createdAt time using PrettyTime
            PrettyTime pTime = new PrettyTime();
            tvCreatedAt.setText(pTime.format(post.getCreatedAt()));

            // get profile photo file from Parse
            ParseFile photo = post.getProfilePhoto();

            // load profile photo into view with glide
            if(post.getUser().get("profilePhoto") != null) {

                Glide.with(context).load(photo.getUrl()).circleCrop().into(ivProfilePhoto);
            }

            //load main image with Glide
            if (post.getImage() != null) {
                Glide.with(context).load(post.getImage().getUrl()).into(ivPhoto);
            }

            itemContainer.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("username", post.getUser().getUsername());
                    intent.putExtra("description", post.getDescription());
                    intent.putExtra("imageUrl", post.getImage().getUrl());
                    PrettyTime pTimeExtra = new PrettyTime();
                    intent.putExtra("createdAt", pTimeExtra.format(post.getCreatedAt()));
                    intent.putExtra("profilePhoto", post.getProfilePhoto());
                    context.startActivity(intent);
                }
            });

            bHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bHeart.isChecked()) {
                        try {
                            incrementLikes(post);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            decrementLikes(post);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });


        }
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> newPosts) {
        posts.addAll(newPosts);
        notifyDataSetChanged();
    }

    public void incrementLikes(Post post) throws ParseException {
        int count = post.getLikes();
        count++;
        post.setLikes(count);
        post.save();
    }

    public void decrementLikes(Post post) throws ParseException {
        int count = post.getLikes();
        count--;
        post.setLikes(count);
        post.save();
    }
}
