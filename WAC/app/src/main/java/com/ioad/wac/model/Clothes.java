package com.ioad.wac.model;

import android.net.Uri;

public class Clothes {

    private Integer index;
    private String imageUriStr;
    private Uri imageUri;
    private String imageName;

    public Clothes() {
    }

    public Clothes(Integer index, String imageUriStr) {
        this.index = index;
        this.imageUriStr = imageUriStr;
    }

    public Clothes(String imageUriStr) {
        this.imageUriStr = imageUriStr;
    }

    public Clothes(Uri imageUri, String clothesName) {
        this.imageUri = imageUri;
        this.imageName = clothesName;
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

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
