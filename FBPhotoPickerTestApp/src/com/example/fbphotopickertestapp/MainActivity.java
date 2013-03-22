package com.example.fbphotopickertestapp;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.grio.fbphotopicker.FBPhotoPickerActivity;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class MainActivity extends Activity {

    private LoginButton mLoginButton;
    private ImageView mImageView;
    private UiLifecycleHelper mUiLifecycleHelper;
    private static boolean mInit = true;
    
    Session.StatusCallback mCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            if (session.isOpened()) {
                showFBPhotoPicker();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.imageView_fb_photo);
        
        mImageView.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (mImageView.getVisibility() == View.VISIBLE)
                    showFBPhotoPicker();                
            }
        });
        mLoginButton = (LoginButton) findViewById(R.id.button_fblogin);
        mLoginButton.setReadPermissions(Arrays.asList(Constants.FACEBOOK_PERMS));
        mUiLifecycleHelper = new UiLifecycleHelper(this, mCallback);
        mUiLifecycleHelper.onCreate(savedInstanceState);
        
        if (Session.getActiveSession() != null && Session.getActiveSession().isOpened()) {
            if (MainActivity.mInit) {
                 showFBPhotoPicker();
                MainActivity.mInit = false;
            }
            mLoginButton.setVisibility(View.GONE);
         }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == Constants.SELECT_PHOTO) {
            
            if (data == null) return;
            
            String photoUrl = data.getStringExtra(FBPhotoPickerActivity.PHOTO_URL);
            
            if (photoUrl != null)
                UrlImageViewHelper.setUrlDrawable(mImageView, photoUrl);
                mImageView.setVisibility(View.VISIBLE);
        }
        else {
            Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
            
            if (Session.getActiveSession().isOpened())
                showFBPhotoPicker();            
        }
    }
    
    private void showFBPhotoPicker() {
        Intent intent = new Intent(MainActivity.this, FBPhotoPickerActivity.class);
        startActivityForResult(intent, Constants.SELECT_PHOTO);          
    }
}
