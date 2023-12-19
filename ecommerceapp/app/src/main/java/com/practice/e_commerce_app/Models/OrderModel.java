package com.practice.e_commerce_app.Models;

public class OrderModel {
    String order_id, product_id, payment_mode, total_price, data, time;

    public OrderModel(String order_id, String product_id, String payment_mode, String total_price, String data, String time) {
        this.order_id = order_id;
        this.product_id = product_id;
        this.payment_mode = payment_mode;
        this.total_price = total_price;
        this.data = data;
        this.time = time;
    }

    public OrderModel(){
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
