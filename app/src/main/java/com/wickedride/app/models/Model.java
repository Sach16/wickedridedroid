package com.wickedride.app.models;

/**
 * Created by Madhumita on 13-01-2016.
 */
public class Model {
    private String id;

    private String description;

    private String name;

    private String make_id;

    private Image image;

    private String image_id;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getMake_id ()
    {
        return make_id;
    }

    public void setMake_id (String make_id)
    {
        this.make_id = make_id;
    }

    public Image getImage ()
    {
        return image;
    }

    public void setImage (Image image)
    {
        this.image = image;
    }

    public String getImage_id ()
    {
        return image_id;
    }

    public void setImage_id (String image_id)
    {
        this.image_id = image_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", description = "+description+", name = "+name+", make_id = "+make_id+", image = "+image+", image_id = "+image_id+"]";
    }
}
