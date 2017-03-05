package com.wickedride.app.models;

/**
 * Created by Inkoniq-Admin on 04-Sep-15.
 */
public class BikationReview {

    int bikerImage;

    String bikerComment;

    int bikerRating;

    String bikerName;

    String bikationDate;

    String Description;

    public BikationReview(int bikerImage, String bikerComment, int bikerRating, String bikerName, String bikationDate, String description) {
        this.bikerImage = bikerImage;
        this.bikerComment = bikerComment;
        this.bikerRating = bikerRating;
        this.bikerName = bikerName;
        this.bikationDate = bikationDate;
        Description = description;
    }

    public int getBikerImage() {
        return bikerImage;
    }

    public void setBikerImage(int bikerImage) {
        this.bikerImage = bikerImage;
    }

    public String getBikerComment() {
        return bikerComment;
    }

    public void setBikerComment(String bikerComment) {
        this.bikerComment = bikerComment;
    }

    public int getBikerRating() {
        return bikerRating;
    }

    public void setBikerRating(int bikerRating) {
        this.bikerRating = bikerRating;
    }

    public String getBikerName() {
        return bikerName;
    }

    public void setBikerName(String bikerName) {
        this.bikerName = bikerName;
    }

    public String getBikationDate() {
        return bikationDate;
    }

    public void setBikationDate(String bikationDate) {
        this.bikationDate = bikationDate;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
