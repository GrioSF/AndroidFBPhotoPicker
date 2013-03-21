package com.grio.fbphotopicker;


public class Constants {

    // Facebook permissions
    public static final String[] FACEBOOK_PERMS = new String[] { "user_about_me", "user_photos" };

    // Facebook Photo Album graph params
    public static final String FB_FIELDS_PARAM = "fields";
    public static final String FB_PHOTO_ALBUM_FIELDS = "albums.fields(id,name,photos.fields(id,icon,picture,source,name,height,width),count,cover_photo)";
}
