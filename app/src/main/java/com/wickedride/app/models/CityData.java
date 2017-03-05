package com.wickedride.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Inkoniq-Admin on 12-Nov-15.
 */
public class CityData {

    String message;

    @SerializedName("status_code")
    String statusCode;

    ArrayList<City> data;

    public ArrayList<City> getData() {
        return data;
    }

    public void setData(ArrayList<City> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

}
