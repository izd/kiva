package com.zackhsi.kiva.models;

import java.io.Serializable;

public class User implements Serializable {
    public int imageId;
    public String name;
    public String loanBecause;
    public String occupationalInfo;
    public int loanCount;
    public String whereabouts;
    public long id;

    public String getImageUrl() {
        int maxImageSize = 1000;
        return "http://www.kiva.org/img/" + maxImageSize + "/" + imageId + ".jpg";
    }
}
