package com.wickedride.app.models;

/**
 * Created by Madhumita on 13-01-2016.
 */
public class Pickup_area {
    private String city_id;

    private String id;

    private String area;

    private String address;

    private String status;

    private String code;

    public String getCity_id ()
    {
        return city_id;
    }

    public void setCity_id (String city_id)
    {
        this.city_id = city_id;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getArea ()
    {
        return area;
    }

    public void setArea (String area)
    {
        this.area = area;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [city_id = "+city_id+", id = "+id+", area = "+area+", address = "+address+", status = "+status+", code = "+code+"]";
    }
}
