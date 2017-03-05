package com.wickedride.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Inkoniq-Admin on 21-Aug-15.
 */
public class Accessories {

    int accessoriesImage;
    @SerializedName("name")
    @Expose
    String accessoriesName;
    String accessoriesPrice;
    String accessoriesDescription;
    boolean checked;

    public Accessories(int accessoriesImage, String accessoriesName, String accessoriesPrice,
                       String accessoriesDescription, boolean checked) {
        this.accessoriesImage = accessoriesImage;
        this.accessoriesName = accessoriesName;
        this.accessoriesPrice = accessoriesPrice;
        this.accessoriesDescription = accessoriesDescription;
        this.checked = checked;
    }

    public int getAccessoriesImage() {
        return accessoriesImage;
    }

    public void setAccessoriesImage(int accessoriesImage) {
        this.accessoriesImage = accessoriesImage;
    }

    public String getAccessoriesName() {
        return accessoriesName;
    }

    public void setAccessoriesName(String accessoriesName) {
        this.accessoriesName = accessoriesName;
    }

    public String getAccessoriesPrice() {
        return accessoriesPrice;
    }

    public void setAccessoriesPrice(String accessoriesPrice) {
        this.accessoriesPrice = accessoriesPrice;
    }

    public String getAccessoriesDescription() {
        return accessoriesDescription;
    }

    public void setAccessoriesDescription(String accessoriesDescription) {
        this.accessoriesDescription = accessoriesDescription;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
