package com.example.ueeapp;

public class ShowCart_Model{
    private String Product_Name;
    private String Price;

    public ShowCart_Model(){

    }

    public ShowCart_Model(String product_Name, String price) {
        Product_Name = product_Name;
        Price = price;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
