package com.practice.e_commerce_app.Models;

import java.util.ArrayList;

public class ProductModel {
    private String product_image, product_id;
    private String product_title, product_price, stock, total_price;
    private ArrayList<String> imageUrls;
    ArrayList<ProductModel> product_ids;

    public ProductModel(String product_id, String product_title, String product_price, String product_image, String stock) {
        this.product_id = product_id;
        this.product_title = product_title;
        this.product_price = product_price;
        this.product_image = product_image;
        this.stock = stock;
    }

    public ProductModel() {
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public ArrayList<ProductModel> getProduct_ids() {
        return product_ids;
    }

    public void setProduct_ids(ArrayList<ProductModel> product_ids) {
        this.product_ids = product_ids;
    }
}
