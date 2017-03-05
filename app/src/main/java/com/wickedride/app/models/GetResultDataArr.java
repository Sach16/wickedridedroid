package com.wickedride.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by inkoniq-admin on 2/24/16.
 */
public class GetResultDataArr {
    @SerializedName("result")
    @Expose
    private ResultDataArr result;

    /**
     *
     * @return
     * The result
     */
    public ResultDataArr getResult() {
        return result;
    }

    /**
     *
     * @param result
     * The result
     */
    public void setResult(ResultDataArr result) {
        this.result = result;
    }
}
