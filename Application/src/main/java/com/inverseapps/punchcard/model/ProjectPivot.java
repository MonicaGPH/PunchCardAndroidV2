package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Inverse, LLC on 10/19/16.
 */

public class ProjectPivot implements Parcelable {

    private int user_id;
    private int project_id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.user_id);
        dest.writeInt(this.project_id);
    }

    public ProjectPivot() {
    }

    protected ProjectPivot(Parcel in) {
        this.user_id = in.readInt();
        this.project_id = in.readInt();
    }

    public static final Parcelable.Creator<ProjectPivot> CREATOR = new Parcelable.Creator<ProjectPivot>() {
        @Override
        public ProjectPivot createFromParcel(Parcel source) {
            return new ProjectPivot(source);
        }

        @Override
        public ProjectPivot[] newArray(int size) {
            return new ProjectPivot[size];
        }
    };
}
