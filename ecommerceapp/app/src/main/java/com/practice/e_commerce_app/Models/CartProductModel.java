package com.practice.e_commerce_app.Models;

public class CartProductModel {
    int product_image;
    String product_name, product_price, quantity;

    public CartProductModel(int product_image, String product_name, String product_price, String quantity) {
        this.product_image = product_image;
        this.product_name = product_name;
        this.product_price = product_price;
        this.quantity = quantity;
    }

    public CartProductModel() {
    }

    public int getProduct_image() {
        return product_image;
    }

    public void setProduct_image(int product_image) {
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
