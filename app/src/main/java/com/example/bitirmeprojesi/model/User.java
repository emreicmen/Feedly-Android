package com.example.bitirmeprojesi.model;

public class User {

    private String Name;
    private String SurName;
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public User(String name, String surName, int number) {
        Name = name;
        SurName = surName;
        this.number = number;

    }



    public User(String name, String surName, String image) {
        Name = name;
        SurName = surName;

    }

    public User(String name, String surName) {
        Name = name;
        SurName = surName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurName() {
        return SurName;
    }

    public void setSurName(String surName) {
        SurName = surName;
    }
}
