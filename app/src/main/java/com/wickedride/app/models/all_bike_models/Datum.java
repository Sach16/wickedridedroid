package com.wickedride.app.models.all_bike_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wickedride.app.models.Conductor;
import com.wickedride.app.models.Logo;
import com.wickedride.app.models.Slot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denny Mathew on 30,November,2015
 */
public class Datum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("make_id")
    @Expose
    private String makeId;
    @SerializedName("available_locations")
    @Expose
    private List<AvailableLocation> availableLocations = new ArrayList<AvailableLocation>();
    @SerializedName("not_available_locations")
    @Expose
    private List<NotAvailableLocation> notAvailableLocations = new ArrayList<NotAvailableLocation>();
    @SerializedName("image_id")
    @Expose
    private String imageId;
    @SerializedName("image")
    @Expose
    private Image image;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("logo_id")
    @Expose
    private String logoId;
    @SerializedName("logo")
    @Expose
    private Logo logo;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("conductor")
    @Expose
    private Conductor conductor;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("start_city")
    @Expose
    private String startCity;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("rental_amount")
    @Expose
    private String rentalAmount;
    @SerializedName("amount")
    @Expose
    private Integer amount;

    private boolean checked = false;

    @SerializedName("date")
    @Expose
    private String date;


    @SerializedName("slots")
    @Expose
    private List<Slot> slots = new ArrayList<Slot>();
    @SerializedName("updated")
    @Expose
    private Boolean updated;
    private boolean isSold;

    public String getRentalAmount() {
        return rentalAmount;
    }

    public void setRentalAmount(String rentalAmount) {
        this.rentalAmount = rentalAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }



    public List<Slot> getSlots() {
        return slots;
    }

    public void setSlots(List<Slot> slots) {
        this.slots = slots;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getUpdated() {
        return updated;
    }

    public void setUpdated(Boolean updated) {
        this.updated = updated;
    }

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
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The makeId
     */
    public String getMakeId() {
        return makeId;
    }

    /**
     *
     * @param makeId
     * The make_id
     */
    public void setMakeId(String makeId) {
        this.makeId = makeId;
    }

    /**
     *
     * @return
     * The availableLocations
     */
    public List<AvailableLocation> getAvailableLocations() {
        return availableLocations;
    }

    /**
     *
     * @param availableLocations
     * The available_locations
     */
    public void setAvailableLocations(List<AvailableLocation> availableLocations) {
        this.availableLocations = availableLocations;
    }

    /**
     *
     * @return
     * The notAvailableLocations
     */
    public List<NotAvailableLocation> getNotAvailableLocations() {
        return notAvailableLocations;
    }

    /**
     *
     * @param notAvailableLocations
     * The not_available_locations
     */
    public void setNotAvailableLocations(List<NotAvailableLocation> notAvailableLocations) {
        this.notAvailableLocations = notAvailableLocations;
    }

    /**
     *
     * @return
     * The imageId
     */
    public String getImageId() {
        return imageId;
    }

    /**
     *
     * @param imageId
     * The image_id
     */
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    /**
     *
     * @return
     * The image
     */
    public Image getImage() {
        return image;
    }

    /**
     *
     * @param image
     * The image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     *
     * @return
     * The logoId
     */
    public String getLogoId() {
        return logoId;
    }

    /**
     *
     * @param logoId
     * The logo_id
     */
    public void setLogoId(String logoId) {
        this.logoId = logoId;
    }

    /**
     *
     * @return
     * The logo
     */
    public Logo getLogo() {
        return logo;
    }

    /**
     *
     * @param logo
     * The logo
     */
    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    public Conductor getConductor() {
        return conductor;
    }

    public void setConductor(Conductor conductor) {
        this.conductor = conductor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean isChecked){
        this.checked = isChecked;
    }
    
    public boolean isSoldOut(){
        return isSold;
    }
    
    public void setSoldOut(boolean isSold){
        this.isSold = isSold;
    }
}
