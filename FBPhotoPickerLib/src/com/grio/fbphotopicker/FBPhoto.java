package com.grio.fbphotopicker;

public class FBPhoto {

    private String id;
    private String url;
    private String source;
    
    public FBPhoto(String id, String url) {
        super();
        this.id = id;
        this.url = url;
    }

    public FBPhoto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        return ((FBPhoto) o).getId().equals(id); // same id = same photo.
    }
}
