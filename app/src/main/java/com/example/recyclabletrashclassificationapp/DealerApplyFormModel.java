package com.example.recyclabletrashclassificationapp;

import com.google.android.gms.maps.model.LatLng;

public class DealerApplyFormModel {

        private String applyid;
        private String date;
        private String lat;
        private String lng;

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

        private String id;
        private String experiences;
        private String photoUrl; // Optional: in case you upload photo and get URL

        public DealerApplyFormModel() {
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




    public DealerApplyFormModel(String uid, String applyid, String date, String name, String birthdate, String phone, String email,
                              String id,
                                String experiences, String photoUrl, String lat ,String lng) {
            this.uid=uid;
            this.applyid=applyid;
            this.date=date;
            this.name = name;
            this.birthdate = birthdate;
            this.phone = phone;
            this.email = email;

            this.id =id;
            this.experiences = experiences;
            this.photoUrl = photoUrl;
            this.lat=lat;
            this.lng=lng;
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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getExperiences() { return experiences; }
        public void setExperiences(String experiences) { this.experiences = experiences; }

        public String getPhotoUrl() { return photoUrl; }
        public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    }


