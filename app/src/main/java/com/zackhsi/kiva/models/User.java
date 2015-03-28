package com.zackhsi.kiva.models;

import java.io.Serializable;

/**
 * Created by zackhsi on 3/21/15.
 */
public class User implements Serializable {
    private int imageId;
    private String name;
    private String loanBecause;
    private String occupationalInfo;
    private int loanCount;
    private String whereabouts;

    public static User getStubUser() {
        User user = new User();
        user.setImageId(1841451)
                .setName("Zack")
                .setLoanBecause("I want to help people stand on their own feet and be proud of what they can do, so they can help others")
                .setOccupationalInfo("Consultant")
                .setLoanCount(122)
                .setWhereabouts("San Francisco CA");

        return user;
    }

    public int getImageId() {
        return imageId;
    }

    public User setImageId(int imageId) {
        this.imageId = imageId;
        return this;
    }

    public String getImageUrl() {
        int maxImageSize = 1000;
        return "http://www.kiva.org/img/" + maxImageSize + "/" + this.getImageId() + ".jpg";
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getLoanBecause() {
        return loanBecause;
    }

    public User setLoanBecause(String loanBecause) {
        this.loanBecause = loanBecause;
        return this;
    }

    public String getOccupationalInfo() {
        return occupationalInfo;
    }

    public User setOccupationalInfo(String occupationalInfo) {
        this.occupationalInfo = occupationalInfo;
        return this;
    }

    public int getLoanCount() {
        return loanCount;
    }

    public User setLoanCount(int loanCount) {
        this.loanCount = loanCount;
        return this;
    }

    public String getWhereabouts() {
        return whereabouts;
    }

    public User setWhereabouts(String whereabouts) {
        this.whereabouts = whereabouts;
        return this;
    }
}
