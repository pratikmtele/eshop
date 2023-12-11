package com.example.eshopadminpanel.Models;

import java.util.ArrayList;

public class BannerModel {
    String bannerId;
    ArrayList<String> bannerUrls;

    public BannerModel(String bannerId, ArrayList<String> bannerUrls) {
        this.bannerId = bannerId;
        this.bannerUrls = bannerUrls;
    }

    public BannerModel(ArrayList<String> bannerUrls) {
        this.bannerUrls = bannerUrls;
    }

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public ArrayList<String> getBannerUrls() {
        return bannerUrls;
    }

    public void setBannerUrls(ArrayList<String> bannerUrls) {
        this.bannerUrls = bannerUrls;
    }
}
