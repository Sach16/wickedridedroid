package com.wickedride.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Inkoniq-Admin on 15-Sep-15.
 */
public class BrandData {

    String message;

    @SerializedName("status_code")
    String statusCode;

    ArrayList<Brand> data;

    public ArrayList<Brand> getData() {
        return data;
    }

    public void setData(ArrayList<Brand> data) {
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