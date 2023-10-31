package com.example.ueeapp;

public class Order {
    String userEmail;
    String shop;
    String noClothes;
    String fragrance;
    String total;
    String pick;
    String delivery;

    public Order(String userEmail, String shop, String noClothes, String fragrance, String total, String pick, String delivery) {
        this.userEmail = userEmail;
        this.shop = shop;
        this.noClothes = noClothes;
        this.fragrance = fragrance;
        this.total = total;
        this.pick = pick;
        this.delivery = delivery;
    }

    public String getUserEmail() {
        return userEmail;
    }
    public String getShop() {
        return shop;
    }
    public String getNoClothes() {
        return noClothes;
    }

    public String getFragrance() {
        return fragrance;
    }

    public String getTotal() {
        return total;
    }

    public String getPick() {
        return pick;
    }

    public String getDelivery() {
        return delivery;
    }


}
