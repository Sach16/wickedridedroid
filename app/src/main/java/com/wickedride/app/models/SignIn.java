package com.wickedride.app.models;

/**
 * Created by Inkoniq-Admin on 22-Sep-15.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wickedride.app.models.bike_details_model.Image;

public class SignIn {

    private String id;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    private String email;

    private String dob;

    private String mobile;

    private String token;
    @SerializedName("image")
    @Expose
    private com.wickedride.app.models.bike_details_model.Image image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    /**
     *
     * @return
     * The image
     */
    public com.wickedride.app.models.bike_details_model.Image getImage() {
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

}
