package com.example.eshopadminpanel.Models;

public class CategoryModel {
    String category, category_id;

    public CategoryModel(String category) {
        this.category = category;
    }

    public CategoryModel(String category, String category_id) {
        this.category = category;
        this.category_id = category_id;
    }

    public CategoryModel() {

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
