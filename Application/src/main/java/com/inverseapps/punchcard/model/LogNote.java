package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Inverse, LLC on 10/20/16.
 */

public class LogNote implements Parcelable {

    private int id;
    private String uniq_id;
    private int user_id;
    private int project_id;
    private String image;
    private String type;
    private Date dateTime;
    private Date created_at;
    private Date updated_at;
    private Project project;
    private String title;
    private String note;
    private String timestamp;
    private String projectName;

    public LogNote(String title, String notes, String timestamp, String projectName) {
        this.note = notes;
        this.title = title;
        this.timestamp = timestamp;
        this.projectName = projectName;
    }


    protected LogNote(Parcel in) {
        id = in.readInt();
        uniq_id = in.readString();
        user_id = in.readInt();
        project_id = in.readInt();
        image = in.readString();
        type = in.readString();
        project = in.readParcelable(Project.class.getClassLoader());
        title = in.readString();
        note = in.readString();
        timestamp = in.readString();
        projectName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(uniq_id);
        dest.writeInt(user_id);
        dest.writeInt(project_id);
        dest.writeString(image);
        dest.writeString(type);
        dest.writeParcelable(project, flags);
        dest.writeString(title);
        dest.writeString(note);
        dest.writeString(timestamp);
        dest.writeString(projectName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LogNote> CREATOR = new Creator<LogNote>() {
        @Override
        public LogNote createFromParcel(Parcel in) {
            return new LogNote(in);
        }

        @Override
        public LogNote[] newArray(int size) {
            return new LogNote[size];
        }
    };

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public static Creator<LogNote> getCREATOR() {
        return CREATOR;
    }
}
