package com.ioad.wac.model;

import android.net.Uri;

public class Clothes {

    private String imageUriStr;
    private Uri imageUri;

    public Clothes() {
    }

    public Clothes(String imageUriStr) {
        this.imageUriStr = imageUriStr;
    }

    public Clothes(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageUriStr() {
        return imageUriStr;
    }

    public void setImageUriStr(String imageUriStr) {
        this.imageUriStr = imageUriStr;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
