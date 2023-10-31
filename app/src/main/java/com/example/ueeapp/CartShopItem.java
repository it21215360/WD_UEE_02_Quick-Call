package com.example.ueeapp;

public class CartShopItem {

    private String shopName;
    private String shopCity;
    private String shopTime;
    private float shopRating;
    private int imageResource;

    public CartShopItem(String shopName,String shopCity,String shopTime,float shopRating,int imageResource) {
        this.shopName = shopName;
        this.shopCity = shopCity;
        this.shopTime = shopTime;
        this.shopRating = shopRating;
        this.imageResource = imageResource;
    }

    public CartShopItem() {
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopCity() {
        return shopCity;
    }

    public void setShopCity(String shopCity) {
        this.shopCity = shopCity;
    }

    public String getShopTime() {
        return shopTime;
    }

    public void setShopTime(String shopTime) {
        this.shopTime = shopTime;
    }

    public float getShopRating() {
        return shopRating;
    }

    public void setShopRating(float shopRating) {
        this.shopRating = shopRating;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
