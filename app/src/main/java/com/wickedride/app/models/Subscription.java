package com.wickedride.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ramesh on 12/17/15.
 */
public class Subscription {
    @SerializedName("subscription_type")
    @Expose
    private String subscriptionType;
    @SerializedName("rides_available")
    @Expose
    private String ridesAvailable;
    @SerializedName("rides_total")
    @Expose
    private String ridesTotal;
    @SerializedName("subscription_expiry_date")
    @Expose
    private String subscriptionExpiryDate;

    /**
     *
     * @return
     * The subscriptionType
     */
    public String getSubscriptionType() {
        return subscriptionType;
    }

    /**
     *
     * @param subscriptionType
     * The subscription_type
     */
    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    /**
     *
     * @return
     * The ridesAvailable
     */
    public String getRidesAvailable() {
        return ridesAvailable;
    }

    /**
     *
     * @param ridesAvailable
     * The rides_available
     */
    public void setRidesAvailable(String ridesAvailable) {
        this.ridesAvailable = ridesAvailable;
    }

    /**
     *
     * @return
     * The ridesTotal
     */
    public String getRidesTotal() {
        return ridesTotal;
    }

    /**
     *
     * @param ridesTotal
     * The rides_total
     */
    public void setRidesTotal(String ridesTotal) {
        this.ridesTotal = ridesTotal;
    }

    /**
     *
     * @return
     * The subscriptionExpiryDate
     */
    public String getSubscriptionExpiryDate() {
        return subscriptionExpiryDate;
    }

    /**
     *
     * @param subscriptionExpiryDate
     * The subscription_expiry_date
     */
    public void setSubscriptionExpiryDate(String subscriptionExpiryDate) {
        this.subscriptionExpiryDate = subscriptionExpiryDate;
    }
}
