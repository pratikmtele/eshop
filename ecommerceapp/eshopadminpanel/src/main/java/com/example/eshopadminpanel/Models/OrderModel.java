package com.example.eshopadminpanel.Models;

import java.util.ArrayList;

public class OrderModel {
    String order_id, product_id, payment_mode, total_price, date, time, address_id;
    ArrayList<ProductModel> product_ids;

    public OrderModel(String order_id, String product_id, String payment_mode, String total_price, String date, String time, String address_id) {
        this.order_id = order_id;
        this.product_id = product_id;
        this.payment_mode = payment_mode;
        this.total_price = total_price;
        this.date = date;
        this.time = time;
        this.address_id = address_id;
    }

    public OrderModel(String order_id, String payment_mode, String total_price, String date, String time, String address_id, ArrayList<ProductModel> product_ids) {
        this.order_id = order_id;
        this.payment_mode = payment_mode;
        this.total_price = total_price;
        this.date = date;
        this.time = time;
        this.address_id = address_id;
        this.product_ids = product_ids;
    }

    public OrderModel() {
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<ProductModel> getProduct_ids() {
        return product_ids;
    }

    public void setProduct_ids(ArrayList<ProductModel> product_ids) {
        this.product_ids = product_ids;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }
}
