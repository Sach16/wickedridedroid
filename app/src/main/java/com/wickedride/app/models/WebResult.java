package com.wickedride.app.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Inkoniq-Admin on 01-Sep-15.
 */
public class WebResult implements Parcelable {

    WebContent data;

    public WebContent getData() {
        return data;
    }

    public void setResult(WebContent data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.data, 0);
    }

    public WebResult() {
    }

    protected WebResult(Parcel in) {
        this.data = in.readParcelable(WebContent.class.getClassLoader());
    }

    public static final Creator<WebResult> CREATOR = new Creator<WebResult>() {
        public WebResult createFromParcel(Parcel source) {
            return new WebResult(source);
        }

        public WebResult[] newArray(int size) {
            return new WebResult[size];
        }
    };
}
