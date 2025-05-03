package com.example.recyclabletrashclassificationapp;

public class HistoryModel {
    private String time;
    private String pic;
    private String name;

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    private String historyId;
    // Empty constructor for Firebase
    public HistoryModel() {
    }

    // Constructor with parameters
    public HistoryModel(String time, String pic, String name,String historyId) {
        this.time = time;
        this.pic = pic;
        this.name = name;
        this.historyId = historyId;
    }

    // Getter and Setter for Time
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    // Getter and Setter for Pic (Image URL)
    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    // Getter and Setter for Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
