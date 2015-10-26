package com.s10r.instagramclient;

import java.util.Date;

/**
 * Created by bschmeckpeper on 10/25/15.
 */
public class Photo {
    private String username;
    private String caption;
    private String imageUrl;
    private int imageHeight;
    private int likesCount;
    private Date createdAt;

    public Photo(String username, String caption, String imageUrl, int imageHeight, int likesCount, long createdAt) {
        this.username = username;
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.imageHeight = imageHeight;
        this.likesCount = likesCount;
        this.createdAt = new Date(createdAt * 1000);
    }

    public String getUsername() {
        return username;
    }

    public String getCaption() {
        return caption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
