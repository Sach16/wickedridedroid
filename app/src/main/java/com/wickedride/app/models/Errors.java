package com.wickedride.app.models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by Ramesh on 2/27/16.
 */
public class Errors {
    @SerializedName("email")
    @Expose
    private List<String> email = new ArrayList<String>();

    /**
     *
     * @return
     * The email
     */
    public List<String> getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(List<String> email) {
        this.email = email;
    }
}
