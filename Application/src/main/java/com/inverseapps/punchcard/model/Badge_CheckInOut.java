package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 29-Dec-17.
 */

public class Badge_CheckInOut implements Parcelable {

    private String name;
    private String badge_id;
    private String client_name;
    private String project_name;
    private String avatar_location;
    private String qr_location;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBadge_id() {
        return badge_id;
    }

    public void setBadge_id(String badge_id) {
        this.badge_id = badge_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getAvatar_location() {
        return avatar_location;
    }

    public void setAvatar_location(String avatar_location) {
        this.avatar_location = avatar_location;
    }

    public String getQr_location() {
        return qr_location;
    }

    public void setQr_location(String qr_location) {
        this.qr_location = qr_location;
    }

    public static Creator<Badge_CheckInOut> getCREATOR() {
        return CREATOR;
    }

    protected Badge_CheckInOut(Parcel in) {
        name = in.readString();
        badge_id = in.readString();
        client_name = in.readString();
        project_name = in.readString();
        avatar_location = in.readString();
        qr_location = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(badge_id);
        dest.writeString(client_name);
        dest.writeString(project_name);
        dest.writeString(avatar_location);
        dest.writeString(qr_location);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Badge_CheckInOut> CREATOR = new Creator<Badge_CheckInOut>() {
        @Override
        public Badge_CheckInOut createFromParcel(Parcel in) {
            return new Badge_CheckInOut(in);
        }

        @Override
        public Badge_CheckInOut[] newArray(int size) {
            return new Badge_CheckInOut[size];
        }
    };
}
