package com.wickedride.app.models;

/**
 * Created by Madhumita on 13-01-2016.
 */
public class Bike {
    private String id;

    private Model model;

    private String model_id;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public Model getModel ()
    {
        return model;
    }

    public void setModel (Model model)
    {
        this.model = model;
    }

    public String getModel_id ()
    {
        return model_id;
    }

    public void setModel_id (String model_id)
    {
        this.model_id = model_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", model = "+model+", model_id = "+model_id+"]";
    }
}
