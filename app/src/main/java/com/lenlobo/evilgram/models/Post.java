package com.lenlobo.evilgram.models;

import android.graphics.Bitmap;
import android.util.Log;

import com.lenlobo.evilgram.R;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.Date;
import java.util.List;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_USER = "user";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_PROFILE_IMAGE = "profilePhoto";

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseFile getProfilePhoto() { return this.getUser().getParseFile("profilePhoto");}

}
