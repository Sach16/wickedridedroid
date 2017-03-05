package com.wickedride.app.models;

/**
 * Created by Ramesh on 1/6/16.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wickedride.app.models.bike_details_model.Result;

public class GetResultData {
    @SerializedName("result")
    @Expose
    private Result result;

    /**
     * @return The result
     */
    public Result getResult() {
        return result;
    }

    /**
     * @param result The result
     */
    public void setResult(Result result) {
        this.result = result;
    }
}
