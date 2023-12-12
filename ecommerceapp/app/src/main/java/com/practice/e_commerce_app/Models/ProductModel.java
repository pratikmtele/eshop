package com.practice.e_commerce_app.Models;

public class ProductModel {
    private String product_image, product_id;
    private String product_title, product_price;

    public ProductModel(String image, String title, String price) {
        this.product_image = image;
        this.product_title = title;
        this.product_price = price;
    }

    public ProductModel(String product_id, String product_title, String product_price, String product_image){
        this.product_id = product_id;
        this.product_title = product_title;
        this.product_price = product_price;
        this.product_image = product_image;
    }

    public ProductModel() {
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }
}
