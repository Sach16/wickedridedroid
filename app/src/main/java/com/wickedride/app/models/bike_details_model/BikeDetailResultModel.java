package com.wickedride.app.models.bike_details_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Denny Mathew on 30,November,2015
 */
public class BikeDetailResultModel {

    @SerializedName("result")
    @Expose
    private Result result;

    /**
     *
     * @return
     * The result
     */
    public Result getResult() {
        return result;
    }

    /**
     *
     * @param result
     * The result
     */
    public void setResult(Result result) {
        this.result = result;
    }

}
