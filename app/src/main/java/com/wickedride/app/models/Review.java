package com.wickedride.app.models;

/**
 * Created by Ramesh on 1/4/16.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("reviewer")
    @Expose
    private Reviewer reviewer;

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
     * The rating
     */
    public String getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     * The rating
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The review
     */
    public String getReview() {
        return review;
    }

    /**
     *
     * @param review
     * The review
     */
    public void setReview(String review) {
        this.review = review;
    }

    /**
     *
     * @return
     * The reviewer
     */
    public Reviewer getReviewer() {
        return reviewer;
    }

    /**
     *
     * @param reviewer
     * The reviewer
     */
    public void setReviewer(Reviewer reviewer) {
        this.reviewer = reviewer;
    }

}
