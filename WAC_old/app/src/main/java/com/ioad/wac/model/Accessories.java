package com.ioad.wac.model;

import android.net.Uri;

public class Accessories {

    private Uri accessoriesUri;
    private String accessoriesName;


    public Accessories(Uri accessoriesUri, String accessoriesName) {
        this.accessoriesUri = accessoriesUri;
        this.accessoriesName = accessoriesName;
    }

    public Uri getAccessoriesUri() {
        return accessoriesUri;
    }

    public void setAccessoriesUri(Uri accessoriesUri) {
        this.accessoriesUri = accessoriesUri;
    }

    public String getAccessoriesName() {
        return accessoriesName;
    }

    public void setAccessoriesName(String accessoriesName) {
        this.accessoriesName = accessoriesName;
    }
}
