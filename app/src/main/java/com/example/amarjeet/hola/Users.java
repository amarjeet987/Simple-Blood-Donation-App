package com.example.amarjeet.hola;

/**
 * Created by Amarjeet on 15-04-2018.
 */

public class Users extends UserId{
    String name, image, latitude, longitude, city, phno, blood_gr;

    public Users(String name, String image, String latitude, String longitude, String city, String phno, String blood_gr) {
        this.name = name;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.phno = phno;
        this.blood_gr = blood_gr;
    }

    public Users(){}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getBlood_gr() {
        return blood_gr;
    }

    public void setBlood_gr(String blood_gr) {
        this.blood_gr = blood_gr;
    }
}
