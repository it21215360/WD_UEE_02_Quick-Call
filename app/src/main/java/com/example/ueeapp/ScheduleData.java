package com.example.ueeapp;

import java.io.Serializable;

public class ScheduleData implements Serializable {
    private String title;
    private String time;
    private String cusId;
    private String address;
    private String date;
    private long Id;

    public ScheduleData() {
        // Default constructor required for Firebase
    }

    public ScheduleData(String title, String time, String cusId, String address, String date) {
        this.title = title;
        this.time = time;
        this.cusId = cusId;
        this.address = address;
        this.date = date;
    }

    // Getters and setters for the data fields

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getId() {
        return Id;
    }
}
