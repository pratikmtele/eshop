package com.practice.e_commerce_app.Models;

public class NotificationModel {
    String heading, desc, time;

    public NotificationModel(String heading, String desc, String time) {
        this.heading = heading;
        this.desc = desc;
        this.time = time;
    }

    public NotificationModel() {
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
