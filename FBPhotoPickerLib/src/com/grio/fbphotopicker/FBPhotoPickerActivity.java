package com.grio.fbphotopicker;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.Response;
import com.facebook.Session;

@SuppressLint("NewApi")
public class FBPhotoPickerActivity extends Activity {

    public static final String PHOTO_ID = "photoId";
    public static final String PHOTO_URL = "photoUrl";

    public static String TAG = FBPhotoPickerActivity.class.getSimpleName();

    private Context mContext;
    private Boolean mPhotoGridVisible = false;

    private ListView mAlbumsList;
    private GridView mPhotosGrid;
    private List<FBPhoto> mPhotos;
    private LinearLayout mProgressOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_fb_photo_picker);
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            if (getActionBar() != null)
                getActionBar().setTitle(R.string.activity_title);

        mContext = this;

        mAlbumsList = (ListView) findViewById(R.id.listView_albums);
        mPhotosGrid = (GridView) findViewById(R.id.gridView_photos);
        mProgressOverlay = (LinearLayout) findViewById(R.id.progress_overlay);
        mProgressOverlay.setVisibility(View.VISIBLE);

        Bundle params = new Bundle();
        params.putString(Constants.FB_FIELDS_PARAM, Constants.FB_PHOTO_ALBUM_FIELDS);

        new Request(Session.getActiveSession(), "me", params, HttpMethod.GET, new Callback() {

            @Override
            public void onCompleted(Response response) {

                if (response.getGraphObject() != null) {
                    JSONObject json = response.getGraphObject().getInnerJSONObject();

                    final List<FBAlbum> fbAlbums = new ArrayList<FBAlbum>();

                    try {
                        JSONArray jsonFBAlbums = json.getJSONObject("albums").getJSONArray("data");

                        for (int i = 0; i < jsonFBAlbums.length(); i++) {

                            JSONObject jsonAlbum = jsonFBAlbums.getJSONObject(i);

                            if (!jsonAlbum.has("photos"))
                                continue;

                            FBAlbum fbAlbum = new FBAlbum();
                            fbAlbum.setName(jsonAlbum.getString("name"));
                            String coverImageId = jsonAlbum.getString("cover_photo");
                            fbAlbum.setCount(jsonAlbum.getInt("count"));

                            JSONArray jsonPhotos = jsonAlbum.getJSONObject("photos").getJSONArray(
                                    "data");

                            for (int j = 0; j < jsonPhotos.length(); j++) {
                                JSONObject jsonFBPhoto = jsonPhotos.getJSONObject(j);
                                FBPhoto fbPhoto = new FBPhoto();
                                fbPhoto.setId(jsonFBPhoto.getString("id"));
                                fbPhoto.setUrl(jsonFBPhoto.getString("picture"));
                                fbPhoto.setSource(jsonFBPhoto.getString("source"));
                                if (fbPhoto.getId().equals(coverImageId)) {
                                    fbAlbum.setCoverPhoto(fbPhoto.getUrl());
                                }
                                fbAlbum.getPhotos().add(fbPhoto);
                            }

                            fbAlbums.add(fbAlbum);
                        }

                        mAlbumsList.setAdapter(new FBAlbumArrayAdapter(mContext, 0, fbAlbums));
                        mAlbumsList.setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                                mPhotos = fbAlbums.get(position).getPhotos();
                                mPhotosGrid
                                        .setAdapter(new FBPhotoArrayAdapter(mContext, 0, mPhotos));
                                // TODO: check for API Level before animating
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
                                    mAlbumsList.animate().x(-mAlbumsList.getWidth());
                                else
                                    mAlbumsList.setVisibility(View.GONE);
                                mPhotoGridVisible = true;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                    if (getActionBar() != null)
                                        getActionBar().setTitle(fbAlbums.get(position).getName());
                            }

                        });

                        mPhotosGrid.setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                                Intent resultData = new Intent();

                                resultData.putExtra(PHOTO_ID, mPhotos.get(position).getId());
                                resultData.putExtra(PHOTO_URL, mPhotos.get(position).getSource());
                                setResult(Activity.RESULT_OK, resultData);
                                finish();
                            }

                        });
                        mProgressOverlay.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        mProgressOverlay.setVisibility(View.GONE);
                    }
                }

            }
        }).executeAsync();

        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            goBack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("NewApi")
    private void goBack() {
        if (mPhotoGridVisible) {
            mPhotosGrid.setAdapter(null);
            // TODO: check for API Level before animating
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                mAlbumsList.animate().x(0);
            else
                mAlbumsList.setVisibility(View.VISIBLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                if (getActionBar() != null)
                    getActionBar().setTitle(R.string.activity_title);

            mPhotoGridVisible = false;
        } else {
            finish();
        }

    }
}
