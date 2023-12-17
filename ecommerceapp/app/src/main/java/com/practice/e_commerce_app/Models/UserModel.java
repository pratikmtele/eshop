package com.practice.e_commerce_app.Models;

import com.google.firebase.firestore.auth.User;

public class UserModel {
    String name, email, phone, address, profilePic;
    public UserModel(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public UserModel(String name, String email, String phone, String address, String profilePic) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.profilePic = profilePic;
    }

    public UserModel(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
