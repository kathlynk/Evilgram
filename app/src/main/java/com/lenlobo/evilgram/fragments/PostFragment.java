package com.lenlobo.evilgram.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lenlobo.evilgram.R;
import com.lenlobo.evilgram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class PostFragment extends Fragment {

    public static final String TAG = "PostActivity";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private EditText etDescription;
    private Button bCaptureImage;
    private ImageView ivPostImage;
    private Button bPost;
    private File photoFile;
    private String photoFileName = "photo.jpg";
    private ProgressBar progBarPost;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        etDescription = view.findViewById(R.id.etDescription);
        bCaptureImage = view.findViewById(R.id.bCaptureImage);
        ivPostImage = view.findViewById(R.id.ivPostImage);
        bPost = view.findViewById(R.id.bPost);
        progBarPost = view.findViewById(R.id.progBarPost);
        progBarPost.setVisibility(View.INVISIBLE);
        bPost.setVisibility(View.GONE);
        bCaptureImage.setVisibility(View.VISIBLE);
        etDescription.setVisibility(View.GONE);

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

                    return;
                }
                if (photoFile == null || ivPostImage.getDrawable() == null) {

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

        Uri fileProvider = FileProvider.getUriForFile(getActivity(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

                //TODO: RESIZE Bitmap (only if necessary for memory issues)

                //Load taken image into preview
                ivPostImage.setImageBitmap(takenImage);
                bCaptureImage.setVisibility(View.GONE);
                bPost.setVisibility(View.VISIBLE);
                etDescription.setVisibility(View.VISIBLE);
            } else {
                //TODO: some kind of error handling
            }

        }
    }

    private File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

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
        progBarPost.setVisibility(View.VISIBLE);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i(TAG, "Post saved");
                    progBarPost.setVisibility(View.INVISIBLE);
                    etDescription.setText("");
                    ivPostImage.setImageResource(0);
                    Toast.makeText(getActivity(), "Post complete", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Error saving post", e);
                    Toast.makeText(getActivity(), "Post saved.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}