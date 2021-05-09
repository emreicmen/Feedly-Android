package com.example.bitirmeprojesi.model;

import java.util.ArrayList;

public class Post {

    private String text;
    private String photoUrl;
    private String fullName;
    private long dateTime;
    private String userProfilePhotoUrl;
    private int userId;
    private int likeCount;
    private int commentCount;
    private ArrayList<Comment> commentList;
    private  double Latitude ;
    private double longitude;

    public Post(){}

    public Post(int userId,
                String text,
                String photoUrl,
                String fullName,
                long dateTime,
                String userProfilePhotoUrl,
                int likeCount,
                int commentCount,
                ArrayList<Comment> commentList,
                double latitude,
                double longitude,
                int likedUserId) {
        this.userId = userId;
        this.text = text;
        this.photoUrl = photoUrl;
        this.fullName = fullName;
        this.dateTime = dateTime;
        this.userProfilePhotoUrl = userProfilePhotoUrl;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.commentList = commentList;
        Latitude = latitude;
        this.longitude = longitude;
        this.likedUserId = likedUserId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getUserProfilePhotoUrl() {
        return userProfilePhotoUrl;
    }

    public void setUserProfilePhotoUrl(String userProfilePhotoUrl) {
        this.userProfilePhotoUrl = userProfilePhotoUrl;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public ArrayList<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList<Comment> commentList) {
        this.commentList = commentList;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getLikedUserId() {
        return likedUserId;
    }

    public void setLikedUserId(int likedUserId) {
        this.likedUserId = likedUserId;
    }

    private int likedUserId;

}
