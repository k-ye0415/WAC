package com.ioad.wac.model;

import android.net.Uri;

public class Clothes {

    private Uri imageUri;
    private String imageName;

    public Clothes() {
    }

    public Clothes(Uri imageUri, String clothesName) {
        this.imageUri = imageUri;
        this.imageName = clothesName;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
