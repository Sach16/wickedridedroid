package com.wickedride.app.models;

/**
 * Created by Ramesh on 12/17/15.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Conductor {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image_id")
    @Expose
    private String imageId;
    @SerializedName("image")
    @Expose
    private Image image;
    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @SerializedName("avg_rating")
    @Expose
    private String avgRating;

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
     * The contactNumber
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     *
     * @param contactNumber
     * The contact_number
     */
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    /**
     *
     * @return
     * The avgRating
     */
    public String getAvgRating() {
        return avgRating;
    }

    /**
     *
     * @param avgRating
     * The avg_rating
     */
    public void setAvgRating(String avgRating) {
        this.avgRating = avgRating;
    }

}
