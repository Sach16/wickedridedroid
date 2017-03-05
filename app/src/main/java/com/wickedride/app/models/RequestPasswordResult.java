package com.wickedride.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Inkoniq-Admin on 24-Sep-15.
 */
public class RequestPasswordResult {


    @SerializedName("result")
    @Expose
    private ResultWithStringData result;

    /**
     *
     * @return
     * The result
     */
    public ResultWithStringData getResult() {
        return result;
    }

    /**
     *
     * @param result
     * The resultwithstringdata
     */
    public void setResult(ResultWithStringData result) {
        this.result = result;
    }

}
