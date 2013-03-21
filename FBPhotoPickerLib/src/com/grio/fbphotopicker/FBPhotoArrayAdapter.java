package com.grio.fbphotopicker;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class FBPhotoArrayAdapter extends ArrayAdapter<FBPhoto> {
    private LayoutInflater mInflater;

    public FBPhotoArrayAdapter(Context context, int textViewResourceId, List<FBPhoto> objects) {
        super(context, textViewResourceId, objects);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.view_photo_grid_item, parent, false);
            holder = new PhotoHolder();
            holder.photo = (ImageView) convertView.findViewById(R.id.imageView_photo);
            convertView.setTag(holder);
        } else {
            holder = (PhotoHolder) convertView.getTag();
        }

        FBPhoto photo = getItem(position);
        UrlImageViewHelper.setUrlDrawable(holder.photo, photo.getUrl());

        return convertView;
    }

    /**
     * Holder pattern to make ListView more efficient.
     * 
     * @author dkadlecek
     * 
     */
    static class PhotoHolder {
        ImageView photo;
    }
}
