package com.lenlobo.evilgram.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lenlobo.evilgram.R;
import com.lenlobo.evilgram.models.Post;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    public static final String TAG = "PostActivity";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private EditText etDescription;
    private Button bCaptureImage;
    private ImageView ivPostImage;
    private Button bPost;
    private File photoFile;
    private String photoFileName = "photo.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etDescription = findViewById(R.id.etDescription);
        bCaptureImage = findViewById(R.id.bCaptureImage);
        ivPostImage = findViewById(R.id.ivPostImage);
        bPost = findViewById(R.id.bPost);

        //click listener for take photo button
        bCaptureImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        //click listener for post button
        bPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(PostActivity.this, "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (photoFile == null || ivPostImage.getDrawable() == null) {
                    Toast.makeText(PostActivity.this, "Must take photo.", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description, currentUser, photoFile);
            }
        });

    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(PostActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

                //TODO: RESIZE Bitmap (only if necessary for memory issues)

                //Load taken image into preview
                ImageView ivPreview = (ImageView) findViewById(R.id.ivPostImage);
                ivPreview.setImageBitmap(takenImage);
            } else {
                Toast.makeText(this, "Photo wasn't taken.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.e(TAG, "failed to create photo directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

    private void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setUser(currentUser);
        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i(TAG, "Post saved");
                    etDescription.setText("");
                    ivPostImage.setImageResource(0);
                } else {
                    Log.e(TAG, "Error saving post", e);
                    Toast.makeText(PostActivity.this, "Post saved.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e == null) {
                    for(Post post: posts) {
                        Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                    }

                } else {
                    Log.e(TAG, "Error getting posts", e);
                }
            }
        });
    }
}