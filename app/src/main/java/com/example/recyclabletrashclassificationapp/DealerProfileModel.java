package com.example.recyclabletrashclassificationapp;

public class DealerProfileModel {


    private String id;

    private String name;
    private String birthdate;


    private String uid;

    private String phone;
    private String email;
    private String lat;
    private String lan;

    private String nid;
    private String experiences;
    private String photoUrl; // Optional: in case you upload photo and get URL

    public DealerProfileModel() {
        // Required empty constructor for Firebase
    }

    public DealerProfileModel(String id, String name, String birthdate, String uid, String phone, String email, String nid, String experiences, String photoUrl, String lat, String lan) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
        this.uid = uid;
        this.phone = phone;
        this.email = email;
        this.lat = lat;
        this.lan = lan;
        this.nid = nid;
        this.experiences = experiences;
        this.photoUrl = photoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getExperiences() {
        return experiences;
    }

    public void setExperiences(String experiences) {
        this.experiences = experiences;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }
}