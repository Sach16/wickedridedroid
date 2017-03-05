package com.wickedride.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madhumita on 13-01-2016.
 */
public class Items {
    private Bike bike;
    @SerializedName("accessories")
    @Expose
    private List<Accessories> accessories = new ArrayList<Accessories>();


    public Bike getBike ()
    {
        return bike;
    }

    public void setBike (Bike bike)
    {
        this.bike = bike;
    }

    public List<Accessories> getAccessories() {
        return accessories;
    }

    public void setAccessories(List<Accessories> accessories) {
        this.accessories = accessories;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [bike = "+bike+", accessories = "+accessories+"]";
    }
}
