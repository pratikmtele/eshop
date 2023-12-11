package com.practice.e_commerce_app.Models;

public class BannerModel {
    String bannerId;
    String bannerUrls;

    public BannerModel(String bannerId, String bannerUrls) {
        this.bannerId = bannerId;
        this.bannerUrls = bannerUrls;
    }

    public BannerModel() {

    }

    public BannerModel(String bannerUrls) {
        this.bannerUrls = bannerUrls;
    }

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerUrls() {
        return bannerUrls;
    }

    public void setBannerUrls(String bannerUrls) {
        this.bannerUrls = bannerUrls;
    }
}
