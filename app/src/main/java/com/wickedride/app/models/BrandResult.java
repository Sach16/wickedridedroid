package com.wickedride.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wickedride.app.models.all_bike_models.Result;

/**
 * Created by Ramesh on 12/17/15.
 */
public class BrandResult {

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
