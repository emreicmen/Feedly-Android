package com.example.bitirmeprojesi.model;

public class User {

    private String id;
    private String fullName;
    private String emailAddress;
    private String profilePhotoUrl;

    public User() {}

    public User(String id, String fullName, String emailAddress, String profilePhotoUrl) {
        this.id = id;
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }
}
