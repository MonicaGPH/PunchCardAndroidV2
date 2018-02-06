package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Inverse, LLC on 10/20/16.
 */

public class Employee implements Parcelable {

    private int id;
    private String name;
    private String email;
    private Date created_at;
    private Date updated_at;
    private String uniq_id;
    private int client_id;
    private String mobileNumber;
    private String role;
    private String status;
    private UserJDoc jdoc;
    private String username;
    private String badge_location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getUniq_id() {
        return uniq_id;
    }

    public void setUniq_id(String uniq_id) {
        this.uniq_id = uniq_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserJDoc getJdoc() {
        return jdoc;
    }

    public void setJdoc(UserJDoc jdoc) {
        this.jdoc = jdoc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBadge_location() {
        return badge_location;
    }

    public void setBadge_location(String badge_location) {
        this.badge_location = badge_location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeLong(this.created_at != null ? this.created_at.getTime() : -1);
        dest.writeLong(this.updated_at != null ? this.updated_at.getTime() : -1);
        dest.writeString(this.uniq_id);
        dest.writeInt(this.client_id);
        dest.writeString(this.mobileNumber);
        dest.writeString(this.role);
        dest.writeString(this.status);
        dest.writeParcelable(this.jdoc, flags);
        dest.writeString(this.username);
        dest.writeString(this.badge_location);
    }

    public Employee() {
    }

    protected Employee(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.email = in.readString();
        long tmpCreated_at = in.readLong();
        this.created_at = tmpCreated_at == -1 ? null : new Date(tmpCreated_at);
        long tmpUpdated_at = in.readLong();
        this.updated_at = tmpUpdated_at == -1 ? null : new Date(tmpUpdated_at);
        this.uniq_id = in.readString();
        this.client_id = in.readInt();
        this.mobileNumber = in.readString();
        this.role = in.readString();
        this.status = in.readString();
        this.jdoc = in.readParcelable(UserJDoc.class.getClassLoader());
        this.username = in.readString();
        this.badge_location = in.readString();
    }

    public static final Parcelable.Creator<Employee> CREATOR = new Parcelable.Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel source) {
            return new Employee(source);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };
}
