package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Inverse, LLC on 10/20/16.
 */

public class OnSite implements Parcelable {

    private int id;
    private String uniq_id;
    private String name;
    private String first_name;
    private String last_name;
    private boolean onSite;
    private Date lastCheckIn;
    private Date lastCheckOut;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUniq_id() {
        return uniq_id;
    }

    public void setUniq_id(String uniq_id) {
        this.uniq_id = uniq_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public boolean isOnSite() {
        return onSite;
    }

    public void setOnSite(boolean onSite) {
        this.onSite = onSite;
    }

    public Date getLastCheckIn() {
        return lastCheckIn;
    }

    public void setLastCheckIn(Date lastCheckIn) {
        this.lastCheckIn = lastCheckIn;
    }

    public Date getLastCheckOut() {
        return lastCheckOut;
    }

    public void setLastCheckOut(Date lastCheckOut) {
        this.lastCheckOut = lastCheckOut;
    }

    public static Creator<OnSite> getCREATOR() {
        return CREATOR;
    }

    protected OnSite(Parcel in) {
        id = in.readInt();
        uniq_id = in.readString();
        name = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        onSite = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(uniq_id);
        dest.writeString(name);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeByte((byte) (onSite ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OnSite> CREATOR = new Creator<OnSite>() {
        @Override
        public OnSite createFromParcel(Parcel in) {
            return new OnSite(in);
        }

        @Override
        public OnSite[] newArray(int size) {
            return new OnSite[size];
        }
    };
}
