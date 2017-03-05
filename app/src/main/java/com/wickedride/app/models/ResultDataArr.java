package com.wickedride.app.models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by inkoniq-admin on 2/24/16.
 */

public class ResultDataArr {
    @SerializedName("data")
    @Expose
    private List<String> data = new ArrayList<String>();
    @SerializedName("status_code")
    @Expose
    private Integer statusCode;
    @SerializedName("message")
    @Expose
    private String message;

    /**
     *
     * @return
     * The data
     */
    public List<String> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<String> data) {
        this.data = data;
    }

    /**
     *
     * @return
     * The statusCode
     */
    public Integer getStatusCode() {
        return statusCode;
    }

    /**
     *
     * @param statusCode
     * The status_code
     */
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    /**
     *
     * @return
     * The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     * The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
