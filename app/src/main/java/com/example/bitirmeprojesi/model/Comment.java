package com.example.bitirmeprojesi.model;

public class Comment {
    private String text;
    private String fullName;
    private String userProfilePhotoUri;
    private String userId;
    private long dateTime;


    public Comment(){}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserProfilePhotoUri() {
        return userProfilePhotoUri;
    }

    public void setUserProfilePhotoUri(String userProfilePhotoUri) {
        this.userProfilePhotoUri = userProfilePhotoUri;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public Comment(String text, String fullName, String userProfilePhotoUri, String userId, long dateTime) {
        this.text = text;
        this.fullName = fullName;
        this.userProfilePhotoUri = userProfilePhotoUri;
        this.userId = userId;
        this.dateTime = dateTime;
    }
}
