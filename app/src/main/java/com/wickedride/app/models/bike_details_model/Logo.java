package com.wickedride.app.models.bike_details_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Denny Mathew on 30,November,2015
 */
public class Logo {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("full")
    @Expose
    private String full;
    @SerializedName("small")
    @Expose
    private String small;
    @SerializedName("medium")
    @Expose
    private String medium;
    @SerializedName("large")
    @Expose
    private String large;

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The full
     */
    public String getFull() {
        return full;
    }

    /**
     *
     * @param full
     * The full
     */
    public void setFull(String full) {
        this.full = full;
    }

    /**
     *
     * @return
     * The small
     */
    public String getSmall() {
        return small;
    }

    /**
     *
     * @param small
     * The small
     */
    public void setSmall(String small) {
        this.small = small;
    }

    /**
     *
     * @return
     * The medium
     */
    public String getMedium() {
        return medium;
    }

    /**
     *
     * @param medium
     * The medium
     */
    public void setMedium(String medium) {
        this.medium = medium;
    }

    /**
     *
     * @return
     * The large
     */
    public String getLarge() {
        return large;
    }

    /**
     *
     * @param large
     * The large
     */
    public void setLarge(String large) {
        this.large = large;
    }

}
