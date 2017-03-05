package com.wickedride.app.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Inkoniq-Admin on 24-Sep-15.
 */
public class RequestPassword implements Parcelable {

    boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(status ? (byte) 1 : (byte) 0);
    }

    public RequestPassword() {
    }

    protected RequestPassword(Parcel in) {
        this.status = in.readByte() != 0;
    }

    public static final Creator<RequestPassword> CREATOR = new Creator<RequestPassword>() {
        public RequestPassword createFromParcel(Parcel source) {
            return new RequestPassword(source);
        }

        public RequestPassword[] newArray(int size) {
            return new RequestPassword[size];
        }
    };
}
