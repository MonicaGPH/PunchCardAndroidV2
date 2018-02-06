package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Inverse, LLC on 12/1/16.
 */

public class OAuth implements Parcelable {

    private String token_type;
    private int expires_in;
    private String access_token;
    private String refresh_token;

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token_type);
        dest.writeInt(this.expires_in);
        dest.writeString(this.access_token);
        dest.writeString(this.refresh_token);
    }

    public OAuth() {
    }

    protected OAuth(Parcel in) {
        this.token_type = in.readString();
        this.expires_in = in.readInt();
        this.access_token = in.readString();
        this.refresh_token = in.readString();
    }

    public static final Parcelable.Creator<OAuth> CREATOR = new Parcelable.Creator<OAuth>() {
        @Override
        public OAuth createFromParcel(Parcel source) {
            return new OAuth(source);
        }

        @Override
        public OAuth[] newArray(int size) {
            return new OAuth[size];
        }
    };
}
