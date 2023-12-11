package com.practice.e_commerce_app.Models;

public class PopularProductModel {
    int image;
    String title, price;

    public PopularProductModel(int image, String title, String price) {
        this.image = image;
        this.title = title;
        this.price = price;
    }

    public PopularProductModel() {
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
