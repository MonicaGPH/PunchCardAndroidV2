package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Inverse, LLC on 10/26/16.
 */

public class QRCodeInfo implements Parcelable {

    private String user_uniq_id;
    private String project_uniq_id;

    public String getUser_uniq_id() {
        return user_uniq_id;
    }

    public void setUser_uniq_id(String user_uniq_id) {
        this.user_uniq_id = user_uniq_id;
    }

    public String getProject_uniq_id() {
        return project_uniq_id;
    }

    public void setProject_uniq_id(String project_uniq_id) {
        this.project_uniq_id = project_uniq_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_uniq_id);
        dest.writeString(this.project_uniq_id);
    }

    public QRCodeInfo() {
    }

    protected QRCodeInfo(Parcel in) {
        this.user_uniq_id = in.readString();
        this.project_uniq_id = in.readString();
    }

    public static final Parcelable.Creator<QRCodeInfo> CREATOR = new Parcelable.Creator<QRCodeInfo>() {
        @Override
        public QRCodeInfo createFromParcel(Parcel source) {
            return new QRCodeInfo(source);
        }

        @Override
        public QRCodeInfo[] newArray(int size) {
            return new QRCodeInfo[size];
        }
    };
}
