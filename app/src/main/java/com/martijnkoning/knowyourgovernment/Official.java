package com.martijnkoning.knowyourgovernment;

import java.io.Serializable;

public class Official implements Serializable {

    private String office;
    private String name;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String party;
    private String phone;
    private String url;
    private String email;
    private String photo;
    private String google;
    private String facebook;
    private String twitter;
    private String youtube;

    Official(String office) {
        this.office = office;
    }

    String getOffice() {
        return office;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getAddress() {
        return address;
    }

    void setAddress(String address) {
        this.address = address;
    }

    String getCity() {
        return city;
    }

    void setCity(String city) {
        this.city = city;
    }

    String getState() {
        return state;
    }

    void setState(String state) {
        this.state = state;
    }

    String getZip() {
        return zip;
    }

    void setZip(String zip) {
        this.zip = zip;
    }

    String getParty() {
        return party;
    }

    void setParty(String party) {
        this.party = party;
    }

    String getPhone() {
        return phone;
    }

    void setPhone(String phone) {
        this.phone = phone;
    }

    String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    String getEmail() {
        return email;
    }

    void setEmail(String email) {
        this.email = email;
    }

    String getPhoto() {
        return photo;
    }

    void setPhoto(String photo) {
        this.photo = photo;
    }

    String getGoogle() {
        return google;
    }

    void setGoogle(String google) {
        this.google = google;
    }

    String getFacebook() {
        return facebook;
    }

    void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    String getTwitter() {
        return twitter;
    }

    void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    String getYoutube() {
        return youtube;
    }

    void setYoutube(String youtube) {
        this.youtube = youtube;
    }
}
