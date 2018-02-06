package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;



public class Clients implements Parcelable {

    private int id;
    private int child_of_id;

    private String subdomain;
    private String name;
    private String fein;
    private String dba;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String phone;
    private String fax;
    private String website;
    private String status;
    private String facelistid;
    private String created_at;
    private String plan_test;
    private String creditCardDeactivateDate;


    protected Clients(Parcel in) {
        id = in.readInt();
        child_of_id = in.readInt();
        subdomain = in.readString();
        name = in.readString();
        fein = in.readString();
        dba = in.readString();
        address = in.readString();
        city = in.readString();
        state = in.readString();
        zip = in.readString();
        phone = in.readString();
        fax = in.readString();
        website = in.readString();
        status = in.readString();
        facelistid = in.readString();
        created_at = in.readString();
        plan_test = in.readString();
        creditCardDeactivateDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(child_of_id);
        dest.writeString(subdomain);
        dest.writeString(name);
        dest.writeString(fein);
        dest.writeString(dba);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(zip);
        dest.writeString(phone);
        dest.writeString(fax);
        dest.writeString(website);
        dest.writeString(status);
        dest.writeString(facelistid);
        dest.writeString(created_at);
        dest.writeString(plan_test);
        dest.writeString(creditCardDeactivateDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Clients> CREATOR = new Creator<Clients>() {
        @Override
        public Clients createFromParcel(Parcel in) {
            return new Clients(in);
        }

        @Override
        public Clients[] newArray(int size) {
            return new Clients[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChild_of_id() {
        return child_of_id;
    }

    public void setChild_of_id(int child_of_id) {
        this.child_of_id = child_of_id;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFein() {
        return fein;
    }

    public void setFein(String fein) {
        this.fein = fein;
    }

    public String getDba() {
        return dba;
    }

    public void setDba(String dba) {
        this.dba = dba;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFacelistid() {
        return facelistid;
    }

    public void setFacelistid(String facelistid) {
        this.facelistid = facelistid;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getPlan_test() {
        return plan_test;
    }

    public void setPlan_test(String plan_test) {
        this.plan_test = plan_test;
    }

    public String getCreditCardDeactivateDate() {
        return creditCardDeactivateDate;
    }

    public void setCreditCardDeactivateDate(String creditCardDeactivateDate) {
        this.creditCardDeactivateDate = creditCardDeactivateDate;
    }

    public static Creator<Clients> getCREATOR() {
        return CREATOR;
    }
}


