package com.practice.e_commerce_app.Models;

public class CategoryModel {
    String category, category_id;

    public CategoryModel(String category) {
        this.category = category;
    }

    public CategoryModel() {
    }

    public CategoryModel(String category_id, String category) {
        this.category = category;
        this.category_id = category_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
