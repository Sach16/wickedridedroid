package com.wickedride.app.models;

/**
 * Created by Madhumita on 13-01-2016.
 */
public class Bookings {
    private String pickup_date;

    private String price;

    private String booking_id;

    private Items items;

    private String profile_image;

    private Pickup_area pickup_area;

    private String profile_image_id;

    private String pickup_time;

    public String getPickup_date ()
    {
        return pickup_date;
    }

    public void setPickup_date (String pickup_date)
    {
        this.pickup_date = pickup_date;
    }

    public String getPrice ()
    {
        return price;
    }

    public void setPrice (String price)
    {
        this.price = price;
    }

    public String getBooking_id ()
    {
        return booking_id;
    }

    public void setBooking_id (String booking_id)
    {
        this.booking_id = booking_id;
    }

    public Items getItems ()
    {
        return items;
    }

    public void setItems (Items items)
    {
        this.items = items;
    }

    public String getProfile_image ()
    {
        return profile_image;
    }

    public void setProfile_image (String profile_image)
    {
        this.profile_image = profile_image;
    }

    public Pickup_area getPickup_area ()
    {
        return pickup_area;
    }

    public void setPickup_area (Pickup_area pickup_area)
    {
        this.pickup_area = pickup_area;
    }

    public String getProfile_image_id ()
    {
        return profile_image_id;
    }

    public void setProfile_image_id (String profile_image_id)
    {
        this.profile_image_id = profile_image_id;
    }

    public String getPickup_time ()
    {
        return pickup_time;
    }

    public void setPickup_time (String pickup_time)
    {
        this.pickup_time = pickup_time;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [pickup_date = "+pickup_date+", price = "+price+", booking_id = "+booking_id+", items = "+items+", profile_image = "+profile_image+", pickup_area = "+pickup_area+", profile_image_id = "+profile_image_id+", pickup_time = "+pickup_time+"]";
    }
}
