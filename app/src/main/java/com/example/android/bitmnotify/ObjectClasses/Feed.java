package com.example.android.bitmnotify.ObjectClasses;

public class Feed {
    String title;
    String content;
    String imageUrl;
    Object timeStamp;
    String userId;
    String username;
    String dpUrl;

    public Feed() {}

    public Feed(String t, String c, String i, Object ts, String userId, String un, String dp) {
        title = t;
        content = c;
        imageUrl = i;
        timeStamp = ts;
        this.userId = userId;
        username = un;
        dpUrl = dp;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getDpUrl() {
        return dpUrl;
    }
}
