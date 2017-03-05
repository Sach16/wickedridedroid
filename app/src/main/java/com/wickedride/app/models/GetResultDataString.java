package com.wickedride.app.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ramesh on 2/16/16.
 */
public class GetResultDataString {

    @SerializedName("result")
    @Expose
    private GetDataString result;

    /**
     *
     * @return
     * The result
     */
    public GetDataString getResult() {
        return result;
    }

    /**
     *
     * @param result
     * The result
     */
    public void setResult(GetDataString result) {
        this.result = result;
    }

}
