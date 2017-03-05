package com.wickedride.app.models;

/**
 * Created by Ramesh on 2/18/16.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BikeAvailabilityStatus {

    @SerializedName("bike_id")
    @Expose
    private String bikeId;

    /**
     *
     * @return
     * The bikeId
     */
    public String getBikeId() {
        return bikeId;
    }

    /**
     *
     * @param bikeId
     * The bike_id
     */
    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

}
