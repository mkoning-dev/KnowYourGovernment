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

    private static int ctr = 1;

    public Official(String office, String name, String address, String city, String state, String zip, String party, String phone, String url, String email, String photo, String google, String facebook, String twitter, String youtube) {
        this.office = office + ctr;
        this.name = name + ctr;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.party = party;
        this.phone = phone;
        this.url = url;
        this.email = email;
        this.photo = photo;
        this.google = google;
        this.facebook = facebook;
        this.twitter = twitter;
        this.youtube = youtube;
        ctr++;
    }


    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getGoogle() {
        return google;
    }

    public void setGoogle(String google) {
        this.google = google;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    @Override
    public String toString() {
        return "Official{" +
                "office='" + office + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", party='" + party + '\'' +
                ", phone='" + phone + '\'' +
                ", url='" + url + '\'' +
                ", email='" + email + '\'' +
                ", photo='" + photo + '\'' +
                ", google='" + google + '\'' +
                ", facebook='" + facebook + '\'' +
                ", twitter='" + twitter + '\'' +
                ", youtube='" + youtube + '\'' +
                '}';
    }
}
