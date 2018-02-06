package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Inverse, LLC on 11/2/16.
 */

public class CheckInOut implements Parcelable {

    private String check_uniq_id;
    private String badge_url_visual_confirmation;
    private Badge_CheckInOut badge;

    public String getCheck_uniq_id() {
        return check_uniq_id;
    }

    public void setCheck_uniq_id(String check_uniq_id) {
        this.check_uniq_id = check_uniq_id;
    }

    public String getBadge_url_visual_confirmation() {
        return badge_url_visual_confirmation;
    }

    public void setBadge_url_visual_confirmation(String badge_url_visual_confirmation) {
        this.badge_url_visual_confirmation = badge_url_visual_confirmation;
    }

    public Badge_CheckInOut getBadge() {
        return badge;
    }

    public void setBadge(Badge_CheckInOut badge) {
        this.badge = badge;
    }

    public static Creator<CheckInOut> getCREATOR() {
        return CREATOR;
    }

    protected CheckInOut(Parcel in) {
        check_uniq_id = in.readString();
        badge_url_visual_confirmation = in.readString();
        badge = in.readParcelable(Badge_CheckInOut.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(check_uniq_id);
        dest.writeString(badge_url_visual_confirmation);
        dest.writeParcelable(badge, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CheckInOut> CREATOR = new Creator<CheckInOut>() {
        @Override
        public CheckInOut createFromParcel(Parcel in) {
            return new CheckInOut(in);
        }

        @Override
        public CheckInOut[] newArray(int size) {
            return new CheckInOut[size];
        }
    };
}
