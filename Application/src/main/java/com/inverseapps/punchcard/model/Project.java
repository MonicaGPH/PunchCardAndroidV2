package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Inverse, LLC on 10/19/16.
 */

public class Project implements Parcelable {

    private int id;
    private int client_id;
    private String name;
    private String uniq_id;
    private String description;
    private Date startDate;
    private Date endDate;
    private double latitude;
    private double longitude;
    private int radius;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String polyfence;
    private double smallest_distance;
    private int sector_id;
    private int industry_id;
    private String status;
    private Date created_at;
    private Date updated_at;
    private int onSite;
    private String asType;
    private String logo;
    private Client client;
    private Stats oceanStats;


    protected Project(Parcel in) {
        id = in.readInt();
        client_id = in.readInt();
        name = in.readString();
        uniq_id = in.readString();
        description = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        radius = in.readInt();
        address = in.readString();
        city = in.readString();
        state = in.readString();
        zip = in.readString();
        country = in.readString();
        polyfence = in.readString();
        smallest_distance = in.readDouble();
        sector_id = in.readInt();
        industry_id = in.readInt();
        status = in.readString();
        onSite = in.readInt();
        asType = in.readString();
        logo = in.readString();
        client = in.readParcelable(Client.class.getClassLoader());
        oceanStats = in.readParcelable(Stats.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(client_id);
        dest.writeString(name);
        dest.writeString(uniq_id);
        dest.writeString(description);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(radius);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(zip);
        dest.writeString(country);
        dest.writeString(polyfence);
        dest.writeDouble(smallest_distance);
        dest.writeInt(sector_id);
        dest.writeInt(industry_id);
        dest.writeString(status);
        dest.writeInt(onSite);
        dest.writeString(asType);
        dest.writeString(logo);
        dest.writeParcelable(client, flags);
        dest.writeParcelable(oceanStats, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniq_id() {
        return uniq_id;
    }

    public void setUniq_id(String uniq_id) {
        this.uniq_id = uniq_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPolyfence() {
        return polyfence;
    }

    public void setPolyfence(String polyfence) {
        this.polyfence = polyfence;
    }

    public double getSmallest_distance() {
        return smallest_distance;
    }

    public void setSmallest_distance(double smallest_distance) {
        this.smallest_distance = smallest_distance;
    }

    public int getSector_id() {
        return sector_id;
    }

    public void setSector_id(int sector_id) {
        this.sector_id = sector_id;
    }

    public int getIndustry_id() {
        return industry_id;
    }

    public void setIndustry_id(int industry_id) {
        this.industry_id = industry_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public int getOnSite() {
        return onSite;
    }

    public void setOnSite(int onSite) {
        this.onSite = onSite;
    }

    public String getAsType() {
        return asType;
    }

    public void setAsType(String asType) {
        this.asType = asType;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Stats getOceanStats() {
        return oceanStats;
    }

    public void setOceanStats(Stats oceanStats) {
        this.oceanStats = oceanStats;
    }
}
