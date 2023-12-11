package com.practice.e_commerce_app.Models;

public class FavoriteProductModel {
    int productImage;
    String title, price;

    public FavoriteProductModel(int productImage, String title, String price) {
        this.productImage = productImage;
        this.title = title;
        this.price = price;
    }

    public FavoriteProductModel(){
    }

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
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
