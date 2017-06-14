package com.example.android.bitmnotify.ObjectClasses;

public class User {

    String uId;
    String name;
    String dpUrl;

    public User() {
    }

    public User(String uId, String name, String dpUrl) {
        this.uId = uId;
        this.name = name;
        this.dpUrl = dpUrl;
    }

    public String getuId() {
        return uId;
    }

    public String getName() {
        return name;
    }

    public String getDpUrl() {
        return dpUrl;
    }
}
