package com.example.kirill.placelocator.LocationActivity;


public class Place {
    double latitude;
    double longitude;
    int idTitle;
    int idInfo;
    int idDrawable;

    public Place(double latitude, double longitude, int idTitle, int idInfo, int idDrawable) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.idTitle = idTitle;
        this.idInfo = idInfo;
        this.idDrawable = idDrawable;
    }

    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getIdInfo() {
        return idInfo;
    }
    public void setIdInfo(int idInfo) {
        this.idInfo = idInfo;
    }

    public int getIdDrawable() {
        return idDrawable;
    }
    public void setIdDrawable(int idDrawable) {
        this.idDrawable = idDrawable;
    }

    public int getIdTitle() {
        return idTitle;
    }
    public void setIdTitle(int idTitle) {
        this.idTitle = idTitle;
    }
}
