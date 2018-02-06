package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Inverse, LLC on 10/18/16.
 */

public class  User implements Parcelable {

    private int id;
    private String name;
    private String first_name;
    private String last_name;
    private String email;
    private String uniq_id;
    private int client_id;
    private String mobileNumber;
    private String role;
    private String status;
    private UserJDoc jdoc;
    private String created_at;
    private String username;
    private String tier;
    private PermissionsDemo permissions;
    private String tos_agree_date;
    private String avatar;
    private Clients client;
    private String badge_location;
    private String subdomain;
    private String created_dateParent;


    protected User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        email = in.readString();
        uniq_id = in.readString();
        client_id = in.readInt();
        mobileNumber = in.readString();
        role = in.readString();
        status = in.readString();
        jdoc = in.readParcelable(UserJDoc.class.getClassLoader());
        created_at = in.readString();
        username = in.readString();
        tier = in.readString();
        permissions = in.readParcelable(PermissionsDemo.class.getClassLoader());
        tos_agree_date = in.readString();
        avatar = in.readString();
        client = in.readParcelable(Clients.class.getClassLoader());
        badge_location = in.readString();
        subdomain = in.readString();
        created_dateParent = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(email);
        dest.writeString(uniq_id);
        dest.writeInt(client_id);
        dest.writeString(mobileNumber);
        dest.writeString(role);
        dest.writeString(status);
        dest.writeParcelable(jdoc, flags);
        dest.writeString(created_at);
        dest.writeString(username);
        dest.writeString(tier);
        dest.writeParcelable(permissions, flags);
        dest.writeString(tos_agree_date);
        dest.writeString(avatar);
        dest.writeParcelable(client, flags);
        dest.writeString(badge_location);
        dest.writeString(subdomain);
        dest.writeString(created_dateParent);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public PermissionsDemo getPermissions() {
        return permissions;
    }

    public void setPermissions(PermissionsDemo permissions) {
        this.permissions = permissions;
    }

    public String getTos_agree_date() {
        return tos_agree_date;
    }

    public void setTos_agree_date(String tos_agree_date) {
        this.tos_agree_date = tos_agree_date;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Clients getClient() {
        return client;
    }

    public void setClient(Clients client) {
        this.client = client;
    }

    public String getBadge_location() {
        return badge_location;
    }

    public void setBadge_location(String badge_location) {
        this.badge_location = badge_location;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public String getCreated_dateParent() {
        return created_dateParent;
    }

    public void setCreated_dateParent(String created_dateParent) {
        this.created_dateParent = created_dateParent;
    }
}




