package com.wickedride.app.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Inkoniq-Admin on 01-Sep-15.
 */
public class WebContent implements Parcelable {

    String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
    }

    public WebContent() {
    }

    protected WebContent(Parcel in) {
        this.content = in.readString();
    }

    public static final Creator<WebContent> CREATOR = new Creator<WebContent>() {
        public WebContent createFromParcel(Parcel source) {
            return new WebContent(source);
        }

        public WebContent[] newArray(int size) {
            return new WebContent[size];
        }
    };
}
