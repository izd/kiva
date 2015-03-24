package com.zackhsi.kiva.models;

/**
 * Created by zackhsi on 3/21/15.
 */
public class User {
    private int imageId;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        int maxImageSize = 1000;
        return "http://www.kiva.org/img/" + maxImageSize + "/" + this.getImageId() + ".jpg";
    }
}
