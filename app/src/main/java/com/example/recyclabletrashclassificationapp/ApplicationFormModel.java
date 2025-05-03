package com.example.recyclabletrashclassificationapp;
 // Change this to your actual package name

public class ApplicationFormModel {
    private String applyid;
    private String date;

    private String name;
    private String birthdate;

    public String getApplyid() {
        return applyid;
    }

    public void setApplyid(String applyid) {
        this.applyid = applyid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    private  String uid;

    private String phone;
    private String email;
    private String streetAddress;
    private String ward;
    private String city;
    private String zip;
    private String id;
    private String experiences;
    private String photoUrl; // Optional: in case you upload photo and get URL

    public ApplicationFormModel() {
        // Required empty constructor for Firebase
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ApplicationFormModel(String uid, String applyid, String date, String name, String birthdate, String phone, String email,
                                String streetAddress, String ward, String city, String zip, String id,
                                String experiences, String photoUrl) {
        this.uid=uid;
        this.applyid=applyid;
        this.date=date;
        this.name = name;
        this.birthdate = birthdate;
        this.phone = phone;
        this.email = email;
        this.streetAddress = streetAddress;
        this.ward = ward;
        this.city = city;
        this.zip = zip;
        this.id =id;
        this.experiences = experiences;
        this.photoUrl = photoUrl;
    }

    // Getters and Setters for all fields

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBirthdate() { return birthdate; }
    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStreetAddress() { return streetAddress; }
    public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }

    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getZip() { return zip; }
    public void setZip(String zip) { this.zip = zip; }

    public String getExperiences() { return experiences; }
    public void setExperiences(String experiences) { this.experiences = experiences; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
}
