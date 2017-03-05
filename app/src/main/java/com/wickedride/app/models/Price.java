package com.wickedride.app.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Inkoniq-Admin on 13-Nov-15.
 */
public class Price {

    @SerializedName("price_per_hr_weekday")
    float pricePerHrWeekday;

    @SerializedName("price_per_hr_weekend")
    float pricePerHrWeekend;

    @SerializedName("rental_amount")
    float rentalAmount;

    @SerializedName("effective_per_hr_price")
    float effectivePerHrPrice;

    public float getPricePerHrWeekday() {
        return pricePerHrWeekday;
    }

    public void setPricePerHrWeekday(float pricePerHrWeekday) {
        this.pricePerHrWeekday = pricePerHrWeekday;
    }

    public float getPricePerHrWeekend() {
        return pricePerHrWeekend;
    }

    public void setPricePerHrWeekend(float pricePerHrWeekend) {
        this.pricePerHrWeekend = pricePerHrWeekend;
    }

    public float getRentalAmount() {
        return rentalAmount;
    }

    public void setRentalAmount(float rentalAmount) {
        this.rentalAmount = rentalAmount;
    }

    public float getEffectivePerHrPrice() {
        return effectivePerHrPrice;
    }

    public void setEffectivePerHrPrice(float effectivePerHrPrice) {
        this.effectivePerHrPrice = effectivePerHrPrice;
    }
}
