package com.wickedride.app.models.all_bike_models;

/**
 * Created by Ramesh on 12/18/15.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wickedride.app.models.Promocode;

import java.util.ArrayList;
import java.util.List;

public class PriceDetails {

    @SerializedName("total_price")
    @Expose
    private Integer totalPrice;
    @SerializedName("effective_price")
    @Expose
    private Integer effectivePrice;
    @SerializedName("no_of_hours")
    @Expose
    private Integer noOfHours;
    @SerializedName("price_per_day")
    @Expose
    private Integer pricePerDay;
    @SerializedName("no_of_days")
    @Expose
    private Integer noOfDays;
    @SerializedName("discounted_price")
    @Expose
    private Integer discountedPrice;
    @SerializedName("coupon")
    @Expose
    private String coupon;
    @SerializedName("coupon_status")
    @Expose
    private Boolean couponStatus;
    @SerializedName("coupon_message")
    @Expose
    private String couponMessage;

    @SerializedName("accessories")
    @Expose
    private List<Object> accessories = new ArrayList<Object>();
    @SerializedName("promocode")
    @Expose
    private Promocode promocode;
    @SerializedName("bike_rental_total")
    @Expose
    private Integer bikeRentalTotal;
    @SerializedName("accessories_rental_total")
    @Expose
    private Integer accessoriesRentalTotal;
    @SerializedName("final_price")
    @Expose
    private Integer finalPrice;
    @SerializedName("discount")
    @Expose
    private Integer discount;

    public Promocode getPromocode() {
        return promocode;
    }

    public void setPromocode(Promocode promocode) {
        this.promocode = promocode;
    }

    public Integer getBikeRentalTotal() {
        return bikeRentalTotal;
    }

    public void setBikeRentalTotal(Integer bikeRentalTotal) {
        this.bikeRentalTotal = bikeRentalTotal;
    }

    public Integer getAccessoriesRentalTotal() {
        return accessoriesRentalTotal;
    }

    public void setAccessoriesRentalTotal(Integer accessoriesRentalTotal) {
        this.accessoriesRentalTotal = accessoriesRentalTotal;
    }

    public Integer getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Integer finalPrice) {
        this.finalPrice = finalPrice;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public List<Object> getAccessories() {
        return accessories;
    }

    public void setAccessories(List<Object> accessories) {
        this.accessories = accessories;
    }

    /**
     *
     * @return
     * The totalPrice
     */
    public Integer getTotalPrice() {
        return totalPrice;
    }

    /**
     *
     * @param totalPrice
     * The total_price
     */
    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     *
     * @return
     * The effectivePrice
     */
    public Integer getEffectivePrice() {
        return effectivePrice;
    }

    /**
     *
     * @param effectivePrice
     * The effective_price
     */
    public void setEffectivePrice(Integer effectivePrice) {
        this.effectivePrice = effectivePrice;
    }

    /**
     *
     * @return
     * The noOfHours
     */
    public Integer getNoOfHours() {
        return noOfHours;
    }

    /**
     *
     * @param noOfHours
     * The no_of_hours
     */
    public void setNoOfHours(Integer noOfHours) {
        this.noOfHours = noOfHours;
    }

    /**
     *
     * @return
     * The pricePerDay
     */
    public Integer getPricePerDay() {
        return pricePerDay;
    }

    /**
     *
     * @param pricePerDay
     * The price_per_day
     */
    public void setPricePerDay(Integer pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    /**
     *
     * @return
     * The noOfDays
     */
    public Integer getNoOfDays() {
        return noOfDays;
    }

    /**
     *
     * @param noOfDays
     * The no_of_days
     */
    public void setNoOfDays(Integer noOfDays) {
        this.noOfDays = noOfDays;
    }

    /**
     *
     * @return
     * The discountedPrice
     */
    public Integer getDiscountedPrice() {
        return discountedPrice;
    }

    /**
     *
     * @param discountedPrice
     * The discounted_price
     */
    public void setDiscountedPrice(Integer discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    /**
     *
     * @return
     * The coupon
     */
    public String getCoupon() {
        return coupon;
    }

    /**
     *
     * @param coupon
     * The coupon
     */
    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    /**
     *
     * @return
     * The couponStatus
     */
    public Boolean getCouponStatus() {
        return couponStatus;
    }

    /**
     *
     * @param couponStatus
     * The coupon_status
     */
    public void setCouponStatus(Boolean couponStatus) {
        this.couponStatus = couponStatus;
    }

    /**
     *
     * @return
     * The couponMessage
     */
    public String getCouponMessage() {
        return couponMessage;
    }

    /**
     *
     * @param couponMessage
     * The coupon_message
     */
    public void setCouponMessage(String couponMessage) {
        this.couponMessage = couponMessage;
    }

}