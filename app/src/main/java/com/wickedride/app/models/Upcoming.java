package com.wickedride.app.models;

/**
 * Created by Ramesh on 12/17/15.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Upcoming {

    @SerializedName("bikation_booking_id")
    @Expose
    private String bikationBookingId;
    @SerializedName("bikation_id")
    @Expose
    private String bikationId;
    @SerializedName("bikation")
    @Expose
    private Bikation bikation;

    /**
     *
     * @return
     * The bikationBookingId
     */
    public String getBikationBookingId() {
        return bikationBookingId;
    }

    /**
     *
     * @param bikationBookingId
     * The bikation_booking_id
     */
    public void setBikationBookingId(String bikationBookingId) {
        this.bikationBookingId = bikationBookingId;
    }

    /**
     *
     * @return
     * The bikationId
     */
    public String getBikationId() {
        return bikationId;
    }

    /**
     *
     * @param bikationId
     * The bikation_id
     */
    public void setBikationId(String bikationId) {
        this.bikationId = bikationId;
    }

    /**
     *
     * @return
     * The bikation
     */
    public Bikation getBikation() {
        return bikation;
    }

    /**
     *
     * @param bikation
     * The bikation
     */
    public void setBikation(Bikation bikation) {
        this.bikation = bikation;
    }

}
