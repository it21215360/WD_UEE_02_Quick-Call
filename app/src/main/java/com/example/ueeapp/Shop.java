package com.example.ueeapp;

public class Shop {
    private String name;
    private String city;
    private String time;
    private float rating;
    private int imageResource;

    public Shop(String name, String city, String time, float rating, int imageResource) {
        this.name = name;
        this.city = city;
        this.time = time;
        this.rating = rating;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getTime() {
        return time;
    }

    public float getRating() {
        return rating;
    }

    public int getImageResource() {
        return imageResource;
    }
}
