package com.example.eshopadminpanel.Models;

import java.util.ArrayList;

public class ProductModel {
    String product_name, product_id, category_id;
    ArrayList<String> productUrls;
    String product_price;

    public ProductModel(String product_name, String product_id, String category_id, ArrayList<String> productUrls, String product_price) {
        this.product_name = product_name;
        this.product_id = product_id;
        this.category_id = category_id;
        this.productUrls = productUrls;
        this.product_price = product_price;
    }

    public ProductModel() {
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public ArrayList<String> getProductUrls() {
        return productUrls;
    }

    public void setProductUrls(ArrayList<String> productUrls) {
        this.productUrls = productUrls;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }
}
