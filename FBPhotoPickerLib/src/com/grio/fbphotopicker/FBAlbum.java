package com.grio.fbphotopicker;

import java.util.ArrayList;
import java.util.List;

public class FBAlbum {

    private String name;
    private String coverPhoto;
    private int count;
    private List<FBPhoto> photos = new ArrayList<FBPhoto>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<FBPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<FBPhoto> photos) {
        this.photos = photos;
    }
}
