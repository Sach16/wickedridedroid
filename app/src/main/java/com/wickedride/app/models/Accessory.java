package com.wickedride.app.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by Ramesh on 1/25/16.
 */
public class Accessory {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("total_price")
    @Expose
    private Integer totalPrice;

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
}
