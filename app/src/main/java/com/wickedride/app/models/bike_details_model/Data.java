package com.wickedride.app.models.bike_details_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wickedride.app.models.Accessory;
import com.wickedride.app.models.Bikations;
import com.wickedride.app.models.Bookings;
import com.wickedride.app.models.Conductor;
import com.wickedride.app.models.Exclusion;
import com.wickedride.app.models.Gallery;
import com.wickedride.app.models.Inclusion;
import com.wickedride.app.models.LastSlot;
import com.wickedride.app.models.Promocode;
import com.wickedride.app.models.Review;
import com.wickedride.app.models.Subscription;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denny Mathew on 30,November,2015
 */
public class Data {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("make_id")
    @Expose
    private String makeId;
    @SerializedName("logo_id")
    @Expose
    private String logoId;
    @SerializedName("logo")
    @Expose
    private Logo logo;
    @SerializedName("available_locations")
    @Expose
    private List<AvailableLocation> availableLocations = new ArrayList<AvailableLocation>();
    @SerializedName("not_available_locations")
    @Expose
    private List<Object> notAvailableLocations = new ArrayList<Object>();
    @SerializedName("image_id")
    @Expose
    private String imageId;
    @SerializedName("image")
    @Expose
    private Image image;

    @SerializedName("bookings")
    @Expose
    private List<Bookings> bookings = new ArrayList<Bookings>();
    @SerializedName("subscription")
    @Expose
    private Subscription subscription;
    @SerializedName("bikations")
    @Expose
    private Bikations bikations;
    @SerializedName("gallery")
    @Expose
    private List<Gallery> gallery = new ArrayList<Gallery>();
    @SerializedName("conductor")
    @Expose
    private Conductor conductor;
    @SerializedName("price")
    @Expose
    private String price;
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
    @SerializedName("total_slots")
    @Expose
    private String totalSlots;
    @SerializedName("available_slots")
    @Expose
    private String availableSlots;
    @SerializedName("meeting_point")
    @Expose
    private String meetingPoint;
    @SerializedName("recommended_bikes")
    @Expose
    private List<String> recommendedBikes = new ArrayList<String>();
    @SerializedName("inclusions")
    @Expose
    private List<Inclusion> inclusions = new ArrayList<Inclusion>();
    @SerializedName("exclusions")
    @Expose
    private List<Exclusion> exclusions = new ArrayList<Exclusion>();
    @SerializedName("ride_rules")
    @Expose
    private List<String> rideRules = new ArrayList<String>();
    @SerializedName("reviews")
    @Expose
    private List<Review> reviews = new ArrayList<Review>();
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("tariff")
    @Expose
    private String tariff;
    @SerializedName("faq")
    @Expose
    private String faq;

    @SerializedName("accessories")
    @Expose
    private List<Accessory> accessories = new ArrayList<Accessory>();
    @SerializedName("promocode")
    @Expose
    private Promocode promocode;
    @SerializedName("bike_rental_total")
    @Expose
    private Integer bikeRentalTotal;
    @SerializedName("accessories_rental_total")
    @Expose
    private Integer accessoriesRentalTotal;
    @SerializedName("total_price")
    @Expose
    private Integer totalPrice;
    @SerializedName("final_price")
    @Expose
    private Integer finalPrice;
    @SerializedName("discount")
    @Expose
    private Integer discount;
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

    @SerializedName("reference_id")
    @Expose
    private String referenceId;
    @SerializedName("instructions")
    @Expose
    private String instructions;

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("booking_id")
    @Expose
    private String bookingId;

    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("last_slot")
    @Expose
    private LastSlot lastSlot;

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("bike_id")
    @Expose
    private Integer bikeId;

    /**
     *
     * @return
     * The bikeId
     */
    public Integer getBikeId() {
        return bikeId;
    }

    /**
     *
     * @param bikeId
     * The bike_id
     */
    public void setBikeId(Integer bikeId) {
        this.bikeId = bikeId;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return
     * The endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     *
     * @param endDate
     * The end_date
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     *
     * @return
     * The lastSlot
     */
    public LastSlot getLastSlot() {
        return lastSlot;
    }

    /**
     *
     * @param lastSlot
     * The last_slot
     */
    public void setLastSlot(LastSlot lastSlot) {
        this.lastSlot = lastSlot;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTariff() {
        return tariff;
    }

    public void setTariff(String tariff) {
        this.tariff = tariff;
    }

    public String getFaq() {
        return faq;
    }

    public void setFaq(String faq) {
        this.faq = faq;
    }

    public List<Gallery> getGallery() {
        return gallery;
    }

    public void setGallery(List<Gallery> gallery) {
        this.gallery = gallery;
    }

    public Conductor getConductor() {
        return conductor;
    }

    public void setConductor(Conductor conductor) {
        this.conductor = conductor;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(String totalSlots) {
        this.totalSlots = totalSlots;
    }

    public String getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(String availableSlots) {
        this.availableSlots = availableSlots;
    }

    public String getMeetingPoint() {
        return meetingPoint;
    }

    public void setMeetingPoint(String meetingPoint) {
        this.meetingPoint = meetingPoint;
    }

    public List<String> getRecommendedBikes() {
        return recommendedBikes;
    }

    public void setRecommendedBikes(List<String> recommendedBikes) {
        this.recommendedBikes = recommendedBikes;
    }

    public List<Inclusion> getInclusions() {
        return inclusions;
    }

    public void setInclusions(List<Inclusion> inclusions) {
        this.inclusions = inclusions;
    }

    public List<Exclusion> getExclusions() {
        return exclusions;
    }

    public void setExclusions(List<Exclusion> exclusions) {
        this.exclusions = exclusions;
    }

    public List<String> getRideRules() {
        return rideRules;
    }

    public void setRideRules(List<String> rideRules) {
        this.rideRules = rideRules;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
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
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
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
    public List<Object> getNotAvailableLocations() {
        return notAvailableLocations;
    }

    /**
     *
     * @param notAvailableLocations
     * The not_available_locations
     */
    public void setNotAvailableLocations(List<Object> notAvailableLocations) {
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
     * The bookings
     */
    public List<Bookings> getBookings() {
        return bookings;
    }

    /**
     *
     * @param bookings
     * The bookings
     */
    public void setBookings(List<Bookings> bookings) {
        this.bookings = bookings;
    }

    /**
     *
     * @return
     * The subscription
     */
    public Subscription getSubscription() {
        return subscription;
    }

    /**
     *
     * @param subscription
     * The subscription
     */
    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    /**
     *
     * @return
     * The bikations
     */
    public Bikations getBikations() {
        return bikations;
    }

    /**
     *
     * @param bikations
     * The bikations
     */
    public void setBikations(Bikations bikations) {
        this.bikations = bikations;
    }


    /**
     *
     * @return
     * The accessories
     */
    public List<Accessory> getAccessories() {
        return accessories;
    }

    /**
     *
     * @param accessories
     * The accessories
     */
    public void setAccessories(List<Accessory> accessories) {
        this.accessories = accessories;
    }

    /**
     *
     * @return
     * The promocode
     */
    public Promocode getPromocode() {
        return promocode;
    }

    /**
     *
     * @param promocode
     * The promocode
     */
    public void setPromocode(Promocode promocode) {
        this.promocode = promocode;
    }

    /**
     *
     * @return
     * The bikeRentalTotal
     */
    public Integer getBikeRentalTotal() {
        return bikeRentalTotal;
    }

    /**
     *
     * @param bikeRentalTotal
     * The bike_rental_total
     */
    public void setBikeRentalTotal(Integer bikeRentalTotal) {
        this.bikeRentalTotal = bikeRentalTotal;
    }

    /**
     *
     * @return
     * The accessoriesRentalTotal
     */
    public Integer getAccessoriesRentalTotal() {
        return accessoriesRentalTotal;
    }

    /**
     *
     * @param accessoriesRentalTotal
     * The accessories_rental_total
     */
    public void setAccessoriesRentalTotal(Integer accessoriesRentalTotal) {
        this.accessoriesRentalTotal = accessoriesRentalTotal;
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
     * The finalPrice
     */
    public Integer getFinalPrice() {
        return finalPrice;
    }

    /**
     *
     * @param finalPrice
     * The final_price
     */
    public void setFinalPrice(Integer finalPrice) {
        this.finalPrice = finalPrice;
    }

    /**
     *
     * @return
     * The discount
     */
    public Integer getDiscount() {
        return discount;
    }

    /**
     *
     * @param discount
     * The discount
     */
    public void setDiscount(Integer discount) {
        this.discount = discount;
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
}
