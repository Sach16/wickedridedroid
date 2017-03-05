package com.wickedride.app.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Inkoniq-Admin on 22-Sep-15.
 */
public class Brand{

    private int id;

    private String name;

    private String status;

    @SerializedName("logo_id")
    private int logoId;

    private Logo logo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLogoId() {
        return logoId;
    }

    public void setLogoId(int logoId) {
        this.logoId = logoId;
    }

    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }
}
