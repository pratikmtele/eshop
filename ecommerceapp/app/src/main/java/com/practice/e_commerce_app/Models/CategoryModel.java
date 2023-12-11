package com.practice.e_commerce_app.Models;

public class CategoryModel {
    String category;

    public CategoryModel(String category) {
        this.category = category;
    }

    public CategoryModel() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
