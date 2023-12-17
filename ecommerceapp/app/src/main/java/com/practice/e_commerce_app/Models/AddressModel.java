package com.practice.e_commerce_app.Models;

public class AddressModel {
    String fullName, phoneNumber, buildingName, area, pincode, city, state;

    public AddressModel(String fullName, String phoneNumber, String buildingName, String area, String pincode, String city, String state) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.buildingName = buildingName;
        this.area = area;
        this.pincode = pincode;
        this.city = city;
        this.state = state;
    }

    public AddressModel(){
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
