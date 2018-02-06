package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Inverse, LLC on 10/20/16.
 */

public class Badge implements Parcelable {

    private String name;
    private int badge_id;
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

    public int getBadge_id() {
        return badge_id;
    }

    public void setBadge_id(int badge_id) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.badge_id);
        dest.writeString(this.client_name);
        dest.writeString(this.project_name);
        dest.writeString(this.avatar_location);
        dest.writeString(this.qr_location);
    }

    public Badge() {
    }

    protected Badge(Parcel in) {
        this.name = in.readString();
        this.badge_id = in.readInt();
        this.client_name = in.readString();
        this.project_name = in.readString();
        this.avatar_location = in.readString();
        this.qr_location = in.readString();
    }

    public static final Parcelable.Creator<Badge> CREATOR = new Parcelable.Creator<Badge>() {
        @Override
        public Badge createFromParcel(Parcel source) {
            return new Badge(source);
        }

        @Override
        public Badge[] newArray(int size) {
            return new Badge[size];
        }
    };
}
