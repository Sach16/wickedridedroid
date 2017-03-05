package com.wickedride.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Inkoniq-Admin on 01-Sep-15.
 */
public class City implements Parcelable {

    private int id;

    @SerializedName("city_name")
    private String city;

    private int status;

    public City() {}

    protected City(Parcel in) {
        this.id = in.readInt();
        this.city = in.readString();
        this.status = in.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String cityName) {
        this.city = cityName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.city);
        dest.writeInt(this.status);
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        public City createFromParcel(Parcel source) {
            return new City(source);
        }

        public City[] newArray(int size) {
            return new City[size];
        }
    };
}
