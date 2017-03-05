package com.wickedride.app.models.all_bike_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denny Mathew on 30,November,2015
 */
public class NotAvailableLocation {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("price")
    @Expose
    private List<String> price = new ArrayList<String>();
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;

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
     * The cityId
     */
    public String getCityId() {
        return cityId;
    }

    /**
     *
     * @param cityId
     * The city_id
     */
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    /**
     *
     * @return
     * The area
     */
    public String getArea() {
        return area;
    }

    /**
     *
     * @param area
     * The area
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The address
     */
    public Object getAddress() {
        return address;
    }

    /**
     *
     * @param address
     * The address
     */
    public void setAddress(Object address) {
        this.address = address;
    }

    /**
     *
     * @return
     * The price
     */
    public List<String> getPrice() {
        return price;
    }

    /**
     *
     * @param price
     * The price
     */
    public void setPrice(List<String> price) {
        this.price = price;
    }

    /**
     *
     * @return
     * The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     * The latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     * The longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     * The longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
