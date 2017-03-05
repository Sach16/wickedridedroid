package com.wickedride.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Inkoniq-Admin on 15-Sep-15.
 */
public class Area implements Parcelable{

    private String id;

    @SerializedName("city_id")
    private String cityId;

    private String area;

    private String address;

    private String latitude;

    private String longitude;

    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.cityId);
        dest.writeString(this.area);
        dest.writeString(this.status);
        dest.writeString(this.address);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
    }

    public Area() {
    }

    protected Area(Parcel in) {
        this.id = in.readString();
        this.cityId = in.readString();
        this.area = in.readString();
        this.status = in.readString();
        this.address = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
    }

    public static final Creator<Area> CREATOR = new Creator<Area>() {
        public Area createFromParcel(Parcel source) {
            return new Area(source);
        }

        public Area[] newArray(int size) {
            return new Area[size];
        }
    };
}