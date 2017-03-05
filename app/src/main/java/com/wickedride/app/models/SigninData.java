package com.wickedride.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Inkoniq-Admin on 15-Sep-15.
 */
public class SigninData {

    String message;

    @SerializedName("status_code")
    String statusCode;

    @SerializedName("errors")
    @Expose
    private Errors errors;

    SignIn data;

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }

    public SignIn getData() {
        return data;
    }

    public void setData(SignIn data) {
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