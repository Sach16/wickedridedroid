package com.wickedride.app.models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ramesh on 12/17/15.
 */
public class Bikations {

    @SerializedName("upcoming")
    @Expose
    private List<Upcoming> upcoming = new ArrayList<Upcoming>();
    @SerializedName("attended")
    @Expose
    private List<Attended> attended = new ArrayList<Attended>();

    /**
     *
     * @return
     * The upcoming
     */
    public List<Upcoming> getUpcoming() {
        return upcoming;
    }

    /**
     *
     * @param upcoming
     * The upcoming
     */
    public void setUpcoming(List<Upcoming> upcoming) {
        this.upcoming = upcoming;
    }

    /**
     *
     * @return
     * The attended
     */
    public List<Attended> getAttended() {
        return attended;
    }

    /**
     *
     * @param attended
     * The attended
     */
    public void setAttended(List<Attended> attended) {
        this.attended = attended;
    }

}
