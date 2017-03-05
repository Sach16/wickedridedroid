package com.wickedride.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Inkoniq-Admin on 15-Sep-15.
 */
public class AreaData{

    String message;

    @SerializedName("status_code")
    String statusCode;

    ArrayList<Area> data;

    public ArrayList<Area> getData() {
        return data;
    }

    public void setData(ArrayList<Area> data) {
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